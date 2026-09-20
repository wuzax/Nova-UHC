package net.novaproject.uhc3945.groups;

import net.novaproject.novauhc.UHCManager;
import net.novaproject.novauhc.ability.AbilitySuppression;
import net.novaproject.novauhc.display.DisplayService;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.player.UHCPlayerManager;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.novauhc.scenario.role.camps.Camps;
import net.novaproject.novauhc.utils.cooldown.CooldownService;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Scenario3945;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class GroupLimitService {

    private static final GroupLimitService INSTANCE = new GroupLimitService();
    private static final Logger LOG = Bukkit.getLogger();
    private static final int DEBUFF_TICKS = 60;
    private static final double FLEE_DELTA = 2.0;

    private final Map<UUID, Watch> watches = new HashMap<>();
    private final Map<String, Double> lastSpread = new HashMap<>();
    private final Set<String> revealedClusters = new HashSet<>();
    private final Set<UUID> suppressed = new HashSet<>();
    private boolean meetup;
    private boolean meetupAnnounced;
    private boolean listenerRegistered;

    private GroupLimitService() {
    }

    public static GroupLimitService get() {
        return INSTANCE;
    }

    public boolean isMeetup() {
        return meetup;
    }

    public void registerListener() {
        if (listenerRegistered) return;
        listenerRegistered = true;
        Bukkit.getPluginManager().registerEvents(new GroupLimitListener(), plugin());
    }

    public void onMeetup() {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario == null || !scenario.isRunning()) return;
        if (meetup) return;
        meetup = true;
        announceMeetupIfNeeded();
        GroupLimitSettings cfg = scenario.groupLimitSettings();
        if (cfg.disableAtMeetup()) {
            clearAll();
        }
    }

    public void reset() {
        clearAll();
        meetup = false;
        meetupAnnounced = false;
        lastSpread.clear();
        revealedClusters.clear();
    }

    public void tick() {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario == null || !scenario.isRunning()) return;

        GroupLimitSettings cfg = scenario.groupLimitSettings();
        if (!cfg.enabled()) return;

        UHCManager uhc = UHCManager.get();
        if (!meetup && uhc.getTimer() >= uhc.getBorderTimer()) {
            onMeetup();
        }
        if (meetup && cfg.disableAtMeetup()) {
            announceMeetupIfNeeded();
            return;
        }

        List<Member> alive = collectMembers(scenario);
        Map<String, List<Member>> byCamp = new LinkedHashMap<>();
        boolean rolesGiven = scenario.isRolesDistributed();
        for (Member member : alive) {
            Camps camp = rolesGiven ? rootCamp(member.camp) : null;
            String key = camp == null ? "PRE" : camp.getName();
            byCamp.computeIfAbsent(key, k -> new ArrayList<>()).add(member);
        }

        Set<UUID> violating = new HashSet<>();
        Set<String> liveClusters = new HashSet<>();
        for (Map.Entry<String, List<Member>> entry : byCamp.entrySet()) {
            Camps camp = campOf(entry.getValue());
            int max = cfg.limitFor(camp, rolesGiven, meetup);
            for (List<Member> cluster : clusters(entry.getValue(), cfg.radiusSquared())) {
                if (cluster.size() <= max) continue;
                String clusterKey = clusterKey(entry.getKey(), cluster);
                liveClusters.add(clusterKey);
                handleCluster(scenario, cfg, camp, max, cluster, clusterKey, violating);
            }
        }

        lastSpread.keySet().removeIf(key -> !liveClusters.contains(key));
        revealedClusters.removeIf(key -> !liveClusters.contains(key));
        List<UUID> stale = new ArrayList<>();
        for (UUID uuid : watches.keySet()) {
            if (!violating.contains(uuid)) stale.add(uuid);
        }
        for (UUID uuid : stale) {
            clearWatch(uuid);
        }
    }

    private void handleCluster(Scenario3945 scenario, GroupLimitSettings cfg, Camps camp, int max,
                               List<Member> cluster, String clusterKey, Set<UUID> violating) {
        boolean combat = cfg.ignoreCombat() && cluster.stream().anyMatch(Member::inCombat);
        double spread = meanDistanceToCentroid(cluster);
        Double previous = lastSpread.put(clusterKey, spread);
        boolean fleeing = cfg.ignoreFleeing() && previous != null && spread - previous >= FLEE_DELTA;
        boolean paused = combat || fleeing;

        List<String> names = cluster.stream().map(m -> m.player.getName()).sorted().toList();
        for (Member member : cluster) {
            violating.add(member.uuid);
            Watch watch = watches.computeIfAbsent(member.uuid, id -> new Watch());
            Stage previousStage = watch.stage;
            if (!paused) {
                watch.violationSeconds++;
            }
            Stage stage = stageOf(watch.violationSeconds, cfg);
            watch.stage = stage;

            if (paused) {
                if (stage.ordinal() >= Stage.WARN.ordinal()) {
                    actionBar(member.player, Lang3945.GROUP_PAUSE, Map.of(
                            "%reason%", combat
                                    ? LangManager.get().get(Lang3945.GROUP_PAUSE_COMBAT, member.player)
                                    : LangManager.get().get(Lang3945.GROUP_PAUSE_FLEE, member.player)));
                }
                if (suppressed.contains(member.uuid)) {
                    AbilitySuppression.restore(member.uuid);
                    suppressed.remove(member.uuid);
                }
                continue;
            }

            Map<String, Object> placeholders = placeholders(scenario, camp, cluster.size(), max, 0);
            if (stage == Stage.WARN || stage == Stage.COUNTDOWN) {
                int remaining = Math.max(1, cfg.graceSeconds() + cfg.countdownSeconds() - watch.violationSeconds + 1);
                placeholders.put("%seconds%", remaining);
                if (previousStage == Stage.NONE || previousStage == Stage.GRACE) {
                    LangManager.get().send(Lang3945.GROUP_WARN, member.player, placeholders);
                    log("avertissement", camp, max, names);
                }
                actionBar(member.player, Lang3945.GROUP_COUNTDOWN, placeholders);
            } else if (stage == Stage.DEBUFF) {
                if (previousStage != Stage.DEBUFF) {
                    LangManager.get().send(Lang3945.GROUP_DEBUFF, member.player, placeholders);
                    log("sanction", camp, max, names);
                }
                actionBar(member.player, Lang3945.GROUP_DEBUFF_BAR, placeholders);
                applyDebuff(cfg, member);
            }
        }

        if (!paused && cfg.revealZone() && cluster.stream().anyMatch(m -> watches.get(m.uuid) != null
                && watches.get(m.uuid).stage == Stage.DEBUFF)
                && revealedClusters.add(clusterKey)) {
            Location centroid = centroid(cluster);
            int precision = Math.max(1, cfg.revealPrecision());
            int x = (int) Math.round(centroid.getX() / precision) * precision;
            int z = (int) Math.round(centroid.getZ() / precision) * precision;
            LangManager.get().sendAll(Lang3945.GROUP_ZONE, Map.of(
                    "%camp%", campLabel(scenario, camp),
                    "%x%", x,
                    "%z%", z));
            log("zone ~" + x + " " + z, camp, max, names);
        }
    }

    private void applyDebuff(GroupLimitSettings cfg, Member member) {
        Player player = member.player;
        applyEffect(player, PotionEffectType.WEAKNESS, cfg.weaknessLevel());
        applyEffect(player, PotionEffectType.SLOW_DIGGING, cfg.fatigueLevel());
        applyEffect(player, PotionEffectType.SLOW, cfg.slownessLevel());
        if (cfg.suppressAbilities() && suppressed.add(member.uuid)) {
            AbilitySuppression.suppress(member.uhcPlayer, 4, false);
        } else if (cfg.suppressAbilities() && suppressed.contains(member.uuid)
                && AbilitySuppression.remainingSeconds(member.uuid) <= 2) {
            AbilitySuppression.suppress(member.uhcPlayer, 4, false);
        }
    }

    private static void applyEffect(Player player, PotionEffectType type, int level) {
        if (player == null || type == null || level <= 0) return;
        player.addPotionEffect(new PotionEffect(type, DEBUFF_TICKS, level - 1, true, false), true);
    }

    private Stage stageOf(int seconds, GroupLimitSettings cfg) {
        int grace = Math.max(0, cfg.graceSeconds());
        int countdown = Math.max(1, cfg.countdownSeconds());
        if (seconds <= grace) return seconds <= 0 ? Stage.NONE : Stage.GRACE;
        if (seconds <= grace + countdown) return seconds == grace + 1 ? Stage.WARN : Stage.COUNTDOWN;
        return Stage.DEBUFF;
    }

    private List<Member> collectMembers(Scenario3945 scenario) {
        List<Member> members = new ArrayList<>();
        for (UHCPlayer uhcPlayer : UHCPlayerManager.get().getPlayingOnlineUHCPlayers()) {
            if (uhcPlayer == null || uhcPlayer.isSpec() || !uhcPlayer.isPlaying()) continue;
            Player player = uhcPlayer.getPlayer();
            if (player == null || !player.isOnline() || player.isDead()) continue;
            Location loc = player.getLocation();
            if (loc == null || loc.getWorld() == null) continue;
            Role role = scenario.getRoleByUHCPlayer(uhcPlayer);
            members.add(new Member(uhcPlayer, player, loc, role == null ? null : role.getCamp()));
        }
        return members;
    }

    private List<List<Member>> clusters(List<Member> members, double radiusSq) {
        int n = members.size();
        if (n == 0) return List.of();
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
        for (int i = 0; i < n; i++) {
            Member a = members.get(i);
            for (int j = i + 1; j < n; j++) {
                Member b = members.get(j);
                if (!a.world.equals(b.world)) continue;
                if (a.location.distanceSquared(b.location) <= radiusSq) {
                    union(parent, i, j);
                }
            }
        }
        Map<Integer, List<Member>> grouped = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            grouped.computeIfAbsent(find(parent, i), k -> new ArrayList<>()).add(members.get(i));
        }
        return new ArrayList<>(grouped.values());
    }

    private static int find(int[] parent, int i) {
        while (parent[i] != i) {
            parent[i] = parent[parent[i]];
            i = parent[i];
        }
        return i;
    }

    private static void union(int[] parent, int a, int b) {
        int ra = find(parent, a);
        int rb = find(parent, b);
        if (ra != rb) parent[rb] = ra;
    }

    private static Camps rootCamp(Camps camp) {
        Camps current = camp;
        int guard = 0;
        while (current != null && current.getParent() != null && guard++ < 64) {
            current = current.getParent();
        }
        return current;
    }

    private static Camps campOf(List<Member> members) {
        if (members.isEmpty()) return null;
        return rootCamp(members.get(0).camp);
    }

    private static String clusterKey(String campKey, List<Member> cluster) {
        return campKey + ":" + cluster.stream()
                .map(m -> m.uuid.toString())
                .sorted()
                .collect(Collectors.joining(","));
    }

    private static Location centroid(List<Member> cluster) {
        World world = cluster.get(0).world;
        double x = 0;
        double y = 0;
        double z = 0;
        for (Member member : cluster) {
            x += member.location.getX();
            y += member.location.getY();
            z += member.location.getZ();
        }
        int n = cluster.size();
        return new Location(world, x / n, y / n, z / n);
    }

    private static double meanDistanceToCentroid(List<Member> cluster) {
        Location center = centroid(cluster);
        double sum = 0;
        for (Member member : cluster) {
            sum += Math.sqrt(member.location.distanceSquared(center));
        }
        return sum / cluster.size();
    }

    private Map<String, Object> placeholders(Scenario3945 scenario, Camps camp, int count, int max, int seconds) {
        Map<String, Object> extra = new HashMap<>();
        extra.put("%camp%", campLabel(scenario, camp));
        extra.put("%count%", count);
        extra.put("%max%", max);
        extra.put("%seconds%", seconds);
        return extra;
    }

    private static String campLabel(Scenario3945 scenario, Camps camp) {
        if (camp == null) {
            return "§favant les rôles";
        }
        String color = camp.getColor() != null ? camp.getColor() : scenario.getColor();
        return color + camp.getName();
    }

    private void actionBar(Player player, Lang3945 key, Map<String, Object> extra) {
        DisplayService.actionBar(player, LangManager.get().get(key, player, extra));
    }

    private void announceMeetupIfNeeded() {
        if (meetupAnnounced) return;
        GroupLimitSettings cfg = settings();
        if (cfg == null || !cfg.enabled()) return;
        meetupAnnounced = true;
        if (cfg.disableAtMeetup()) {
            LangManager.get().sendAll(Lang3945.GROUP_MEETUP_OFF);
            LOG.info("[UHC 39-45] Limites de groupe désactivées (meetup).");
        } else {
            LangManager.get().sendAll(Lang3945.GROUP_MEETUP_RAISE, Map.of(
                    "%axe%", cfg.meetupAxisMax(),
                    "%resistance%", cfg.meetupResistanceMax(),
                    "%civilians%", cfg.meetupCivilianMax()));
            LOG.info("[UHC 39-45] Limites de groupe assouplies (meetup).");
        }
    }

    private GroupLimitSettings settings() {
        Scenario3945 scenario = Scenario3945.get();
        return scenario == null ? null : scenario.groupLimitSettings();
    }

    private void clearWatch(UUID uuid) {
        watches.remove(uuid);
        if (suppressed.remove(uuid)) {
            AbilitySuppression.restore(uuid);
        }
    }

    private void clearAll() {
        for (UUID uuid : new HashSet<>(suppressed)) {
            AbilitySuppression.restore(uuid);
        }
        suppressed.clear();
        watches.clear();
        lastSpread.clear();
        revealedClusters.clear();
    }

    private static org.bukkit.plugin.Plugin plugin() {
        net.novaproject.uhc3945.Main module = net.novaproject.uhc3945.Main.get();
        return module != null ? module : net.novaproject.novauhc.Main.get();
    }

    private void log(String kind, Camps camp, int max, List<String> names) {
        String campName = camp == null ? "pré-rôles" : camp.getName();
        LOG.info("[UHC 39-45] Groupe " + kind + " | camp=" + campName
                + " n=" + names.size() + " max=" + max + " | " + String.join(", ", names));
    }

    private enum Stage {
        NONE, GRACE, WARN, COUNTDOWN, DEBUFF
    }

    private static final class Watch {
        private int violationSeconds;
        private Stage stage = Stage.NONE;
    }

    private static final class Member {
        private final UHCPlayer uhcPlayer;
        private final Player player;
        private final UUID uuid;
        private final Location location;
        private final World world;
        private final Camps camp;

        private Member(UHCPlayer uhcPlayer, Player player, Location location, Camps camp) {
            this.uhcPlayer = uhcPlayer;
            this.player = player;
            this.uuid = uhcPlayer.getUniqueId();
            this.location = location;
            this.world = location.getWorld();
            this.camp = camp;
        }

        private boolean inCombat() {
            return CooldownService.CombatTracker.isInCombat(uuid);
        }
    }
}
