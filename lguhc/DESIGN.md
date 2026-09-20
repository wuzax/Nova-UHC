# LG-UHC sur Nova 1.8.8 — conception

Ce document fige le **constat versions**, la **matrice de décision**, et le **mapping ModeKit**.
Il n’existe **pas** d’intégration « drop-in » du JAR Ph1Lou actuel sur Nova.

## 1. Constat versions (vérifié)

| Projet | Cible réelle |
| --- | --- |
| **Nova-UHC** (`core/build.gradle`) | `com.github.azbh111:spigot-1.8.8:R`. `plugin.yml` **sans** `api-version`. Clients UHC classiques **1.8.9**. |
| **WereWolfPlugin HEAD** (Ph1Lou, `1.13.1`) | `org.spigotmc:spigot:1.21.4-R0.1-SNAPSHOT`, Java 8 **source/target**, `plugin.yml` `api-version: "1.13"`, `load: STARTUP`. |
| **WereWolfAPI HEAD** (`1.12`) | même `spigot:1.21.4-R0.1-SNAPSHOT`. Contrats seulement. |

Conséquence : le JAR WereWolf **actuel** ne peut pas charger dans le process Spigot **1.8.8** de Nova. `api-version: 1.13` est déjà hors 1.8 ; le code HEAD utilise des API 1.9+ (ex. `Particle.FLAME` dans `Pyromane`). **Ne pas** copier `WereWolfPlugin.jar` dans `plugins/` de Nova. **Ne pas** shade / dependre de cet artefact.

WereWolf et Nova sont **deux moteurs UHC complets** (lobby, scatter, bordure, scoreboard, start). Même si les versions matchaient, les lancer ensemble dans un seul process serait un conflit de chassis.

## 2. Historique Ph1Lou : y a-t-il un drop-in 1.8.x ?

Audit GitHub `Ph1Lou/WereWolfPlugin` (pas de tags publiés ; branches distantes : `master` + un Dependabot).

| Époque | Preuve | 1.8.8 Nova ? |
| --- | --- | --- |
| Commit initial `a75bf998` (2020) | `plugin.yml` : `name: pluginLG`, `api-version: 1.8`, main `io.github.ph1lou.pluginlg.MainLG` | Archéologie seulement. Pas un artefact maintenu. |
| `5cd48687` « add multi version support 1.8-1.16 » | Compile **`spigot 1.16.1`**, `api-version: "1.16"` | VersionUtils 1.8 existe encore dans l’API, mais ce n’est **pas** un JAR 1.8.8 testé/maintenu pour Nova. |
| `2520456e` « update 1.17.1 » | `spigot 1.17.1`, `api-version: "1.13"` | Non. |
| HEAD `1.13.1` | `spigot 1.21.4`, `api-version: "1.13"` | **Non.** |

Aucune branche `1.8` / `legacy` n’est publiée aujourd’hui. WereWolfAPI garde `VersionUtils_1_8` (couche NMS), mais le **plugin** HEAD n’est plus un compagnon 1.8.

**Décision :** il n’existe pas de JAR Ph1Lou actuel (ni tag) que l’on puisse poser à côté de Nova 1.8.8.

## 3. Matrice de décision

| Voie | Quoi | Garde Nova 1.8 + clients 1.8.9 | Garde le plugin Ph1Lou stock | Effort / risque |
| --- | --- | --- | --- | --- |
| **A** | Serveur **Paper 1.21** séparé, **WereWolfPlugin officiel** seul. Pas de chassis Nova. | Non (autre binaire, autre entrée). | Oui | Faible côté Nova. Deux stacks à opérer. |
| **B** | **Port** des rôles / wincons LG sur `ScenarioRole` / `ModeKit`, module Gradle `lguhc`, compile contre **spigot-1.8.8**. WereWolfAPI = **référence de design** uniquement. | **Oui** | Non (réimplémentation AGPL + attribution) | Gros portage si on vise ~99 rôles. MVP volontairement petit. |
| **C** | Monter tout Nova (core, Ultimate, 39-45, clients) vers un MC moderne pour **héberger** WereWolf. | Non, sauf ViaVersion / protocole — casse le UHC 1.8.9 « vanilla client ». | Possible ensuite | Chantier chassis : NMS, matériaux, packetevents, mondes, tous les modules. |

- « LG **dans** Nova 1.8 » ⇒ **B seulement**.
- « LG Ph1Lou **complet** et à jour » ⇒ **A**.
- « Un seul process moderne qui fait UHC Nova + LG officiel » ⇒ **C** (hors scope, massif).

Licence : Nova et WereWolf sont **AGPL-3.0**. Fork / adaptation **OK** avec conservation AGPL et **crédit Ph1Lou**.

## 4. Mapping Ph1Lou → Nova (voie B)

Nova fournit déjà le chassis UHC (host `/h`, scatter, bordure, épisodes, PvP, meetup, victoire). `uhc3945` est le modèle : plugin `depend: NovaUHC`, enregistrement `ScenarioManager.addScenario` à J+20 ticks, `ModeKit` pour camps / composition / win.

| Concept Ph1Lou | Équivalent Nova | Notes MVP |
| --- | --- | --- |
| Composition rôles (`/a`) | `ScenarioRoleDispatcherUi` / `ScenarioCampUi` (Mode de jeu host) | Défauts : 2 Loups, 1 de chaque unique, Villageois filler. |
| Camps Village / LG / Neutres | `CampsLguhc` VILLAGE / LOUPS / SOLITAIRE | Camp solitaire présent ; **aucun rôle solitaire** dans le MVP. |
| `@Role` + IRole | `RoleLguhc` extends `Role` | Icônes **1.8.8** (`EYE_OF_ENDER`, `GOLD_CHESTPLATE`, `RED_ROSE`…). |
| Chat loups | `ChatManager.createCampChannel(..., prefix "lg")` | Préfixe `lg `. Petite Fille = `addIndirectReader` (écoute, pas d’écriture). |
| Liste des loups | `ModeKit.revealWithin(LOUPS)` + `KPIBuilder.ofCamp` | Les loups se connaissent. |
| Lovers | `Bonds.link().dieTogether().winTogether().announce().chat()` | `/role couple <j1> <j2>` (Cupidon, 1×). |
| Vote villageois / cycle jour-nuit LG | — | **Non porté.** La nuit UHC (bordure / timer Nova) n’est pas le cycle LG. Force loup = `setStrength(0.20)` **permanent** (UHC), pas seulement la nuit Ph1Lou. |
| Infection, LG blanc, etc. | — | Hors MVP. |
| Win Village / LG / couple / solitaire | `WinLguhc` + `ModeKit.winCondition` | Couple si le lien couvre **tous** les vivants. |
| Voyante / Sorcière / Chasseur / Ancien / … | `CommandAbility` / `PassiveAbility` / `Role.onDeath` + `PendingDeathManager` | Voir tableau rôles. |
| ~99 rôles, addons WereWolfAPI | Nouveaux `RoleLguhc` | Ajouter une classe + `ModeKit.uniqueRoles` / `.role()`. Ne **jamais** importer `fr.ph1lou.*`. |

### Rôles MVP

| Rôle | Camp | Pouvoir Nova |
| --- | --- | --- |
| Villageois | Village | Filler. Aucun pouvoir. |
| Loup-Garou | Loups | Vision nocturne, force 20 %, roster loups, chat `lg`. |
| Voyante | Village | `/role voir <joueur>` 1×. |
| Sorcière | Village | `/role soigner` et `/role tuer` 1× chacun (soin autorisé sur soi). |
| Chasseur | Village | Tue l’assassin à la mort (`Player.setHealth(0)` — `tryUse` refuse un joueur mort). |
| Petite Fille | Village | Écoute le chat loups + vision nocturne. |
| Cupidon | Village | `/role couple` → Bonds. |
| Ancien | Village | 1ère mort : `PendingDeathManager` résurrection. |
| Renard | — | **Non porté** (trop spécifique Ph1Lou / flair). |

## 5. Ce que ce module n’est pas

- Pas un softdepend WereWolf.
- Pas un shade du JAR 1.21.
- Pas une émulation du moteur WereWolf (lobby `werewolf_map`, `/a start`, vote).
- Pas une coexistence « deux UHC dans le même `plugins/` ».

Pour le LG **officiel** complet : voie **A**. Pour étendre le LG **dans** Nova : ajouter des rôles ici (voie **B**).
