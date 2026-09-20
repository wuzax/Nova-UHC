# Nova-UHC (fork)

Ce dépôt est un **fork** de [lezombie3D/Nova-UHC](https://github.com/lezombie3D/Nova-UHC), un plugin UHC Spigot 1.8.8.

Licence : **[GNU Affero General Public License v3.0](LICENSE)** (AGPL-3.0). Toute modification déployée sur un serveur doit rester disponible sous la même licence.

## MongoDB

Nova embarque `mongo-java-driver` dans le module `core` : configs, scénarios et rôles sont sérialisés en documents BSON (`org.bson.Document`). Un déploiement Nova typique s’appuie donc sur **MongoDB** (et, selon la config, sur l’API HTTP définie dans `core/src/main/resources/config.yml` → `api.url` / `api.key`).

Sans MongoDB / API Nova, le plugin charge quand même, mais la sauvegarde de configs et certaines fonctions réseau ne seront pas disponibles. Le gameplay UHC (bordure, épisodes, meetup, rôles) reste géré par le core Nova — il ne faut pas le réimplémenter dans un module scénario.

## Compilation

Prérequis : **JDK 25+**. Nova `core` dépend de CloudNet `4.0.0-RC15`, dont les artefacts sont compilés pour la JVM 25. Avec un JDK 21, Gradle échoue dès `:core:compileJava` (`wrapper-jvm-impl` n’est pas compatible JVM 21).

Le wrapper n’est pas exécutable dans Git (`100644`) : utilisez `bash ./gradlew`.

```bash
bash ./gradlew build
```

Vérifié : `BUILD SUCCESSFUL` avec Temurin 25.0.4.1 (module `uhc3945` inclus). Sur JDK 21, le build s’arrête sur CloudNet — ce n’est pas un problème du module 39-45.

JARs produits :

| Module | Archive |
| --- | --- |
| `core` (+ `api`) | `core/build/libs/API.jar` (plugin **NovaUHC**) |
| `ultimate` | `ultimate/build/libs/Ultimate.jar` |
| `scenarioplus` | `scenarioplus/build/libs/ScenarioPlus.jar` |
| `uhc3945` | `uhc3945/build/libs/UHC3945.jar` |
| `lguhc` | `lguhc/build/libs/LGUHC.jar` (pont WereWolf officiel, **pas** un port de rôles) |

Copier ces JARs dans `plugins/` d’un serveur Spigot/Paper **1.8.8**, avec les dépendances de Nova (`packetevents`, éventuellement Apollo / Citizens).

## LG-UHC (Ph1Lou)

Loup-Garou UHC **n’est pas** réimplémenté dans Nova. On utilise le plugin officiel **WereWolfPlugin** (AGPL, Ph1Lou, multi-1.8.8–1.21+, Java 8). Nova et WereWolf sont **deux moteurs UHC** : ne lancez pas les deux parties à la fois. Détail, install Spigot **#73113**, compilation JDK 8+ : **[LGUHC.md](LGUHC.md)**.

## Scénario UHC 39-45

Module Gradle **`uhc3945`** : UHC à rôles cachés sur le thème de la Seconde Guerre mondiale, branché sur le framework camps/rôles de Nova (`ScenarioRole` / `ModeKit`). La boucle UHC (bordure, épisodes, meetup, PvP) est celle du core.

### Camps et victoire

| Camp | Connaissance | Victoire |
| --- | --- | --- |
| **Axe** | Le **noyau** (Commandant, Officier, Soldats) se connaît. L’**Infiltré** est isolé : pas de roster Axe. | Camp lorsque la Résistance est neutralisée. Les civils survivants ne bloquent pas (configurable). |
| **Résistance** | Cellules de **2–3** (+ isolé possible). Pas de dump du camp. Liaison via authentification. | Camp lorsque l’Axe est neutralisé. Les hooks d’objectifs (cellules, etc.) ne retardent pas la fin s’il n’y a plus d’opposition. |
| **Civils** | Aucun allié automatique. | Objectifs personnels. Un civil gagne s’il est encore en vie à la victoire militaire, ou s’il ne reste plus que des civils. |

La détection est branchée sur `ScenarioRole` / `VictoryManager` (`winCondition` + libellé de fin). Flags host (catégorie **victoire**) : civils bloquants, Commandant requis, objectifs Résistance, fin sans opposition, partage civil.

### Limites de groupe (pré-meetup)

Plafonds par camp dans un rayon de 35 blocs : **Axe 4**, **Résistance 3**, **Civils 3**. Tant que les rôles ne sont pas distribués : plafond global (3). Tolérance 25 s, puis avertissement → compte à rebours → malus (Weakness + Mining Fatigue, **pouvoirs coupés** via `AbilitySuppression`). Exceptions : combat, dispersion. Meetup : limites levées par défaut. Tout est réglable (catégorie **groupes**).

### Cellules et authentification

À la distribution des rôles, les Résistants (y compris Médecin / Saboteur) sont découpés en cellules (ex. 9 joueurs → 3+3+2+1). Intra-cellule : les membres se connaissent. Inter-cellules :

1. `/role code` — mot de reconnaissance (incomplet). Le **mot secret** n’est visible que du chef (ou d’un isolé / si le chef est hors-jeu).
2. `/role auth <mot>` — indice reconnu, **doute** (incomplet).
3. `/role auth <mot> <secret>` — en attente.
4. `/role confirmer <joueur>` — le chef (ou un membre si pas de chef vivant) valide. Les deux cellules apprennent alors les **pseudos** de l’autre réseau, pas un dump de tout le camp.

`/contacts` et `/camp` n’affichent que la connaissance officielle du joueur. `/role mates` reste le listing KPI Nova.

### Infiltration (Axe)

Rôle unique **Infiltré** : `/role intercepter` capte un **fragment** lors d’une auth, `/role usurper <mot>` le rejoue. Le réseau est alerté ; l’Infiltré **n’obtient pas** la liste des membres. L’**Ordre** du Commandant ne buffe pas l’Infiltré (le noyau ne le connaît pas).

### Rôles et pouvoirs

Les pouvoirs suivent les `Ability` Nova (`UseAbility` / `CommandAbility`), avec messages FR, cooldowns, utilisations limitées et **portes d’épisode**. Pas de Strength auto-win, pas de révélation d’un camp entier via un pouvoir.

**Axe**

| Rôle | Type | Pouvoir |
| --- | --- | --- |
| Commandant | unique, noyau | **Ordre** (ép. 2, item) : Speed I + Résistance I au noyau proche. Sa mort affaiblit les Soldats. |
| Officier | unique, noyau | **`/role rapport`** (ép. 2) : position approximative du Commandant. |
| Soldat | masse, noyau | **Charge** (ép. 2, item) : Speed II + Résistance I, courte durée. Plus faible sans Commandant. |
| Infiltré | unique, isolé | **`/role intercepter`** / **`/role usurper`** : fragment d’auth, jamais le roster. |

**Résistance**

| Rôle | Type | Pouvoir |
| --- | --- | --- |
| Chef de réseau | unique | **Signal** (ép. 3, item) : présences + direction, **sans noms**. Auth : `/role code`, `auth`, `confirmer`. |
| Résistant | masse | **Cachette** (ép. 2, item) : invisibilité brève. Auth : `/role code`, `auth`, `confirmer`. |
| Médecin | unique | **Soin** (ép. 2, item) : quelques cœurs, 3 utilisations. 1 pomme d’or au briefing. Auth cellule. |
| Saboteur | unique | **Sabotage** (ép. 4, item) : silence des pouvoirs + perte des buffs, 2 utilisations. Auth cellule. |

**Civils**

| Rôle | Type | Pouvoir |
| --- | --- | --- |
| Civil | filler | **Instinct** (ép. 3, 1×/épisode) ; **`/role allegeance rester\|soutenir`** (ép. 5, 1×, ne change pas le camp). Objectif : survivre. |
| Informateur | unique | **`/role analyser`** (ép. 2) : santé / distance / équipement, jamais le rôle. **`/role transmettre`** (ép. 3) accomplit l’objectif personnel. |
| Contrebandier | unique | **Colis** (ép. 3, item) : or + pomme d’or. Sneak + visée pour le donner. Objectif : survivre. |

### Activer et playtester

1. Démarrer le serveur avec **NovaUHC** + **UHC3945** (enregistrement ~1 s après l’enable).
2. Host : `/h config` → scénarios spéciaux → **UHC 39-45**.
3. Composer au minimum : 1 Commandant, quelques Soldats, 1 Infiltré, 1 Chef de réseau, plusieurs Résistants (idéalement 5+ pour voir cellules + isolé), Civils au besoin.
4. Lancer la partie. Les rôles tombent au timer PvP (ou timer rôles du scénario).
5. Côté Résistance : vérifier `/role`, `/contacts`, `/role code`, échanger un mot en vocal, `/role auth`, `/role confirmer`.
6. Côté Infiltré : `/role intercepter` pendant une auth, puis `/role usurper`.
7. Côté Axe noyau : `/contacts` liste le noyau, pas l’Infiltré. Le Commandant est informé qu’un Infiltré existe sans nom.
8. Logs serveur `[UHC 39-45]` : cellules, codes, auth, intercept, victoire.

Un seul scénario spécial à la fois : n’activez pas Legend / Nuzlocke en même temps que 39-45.
