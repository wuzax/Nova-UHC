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

Copier ces JARs dans `plugins/` d’un serveur Spigot/Paper **1.8.8**, avec les dépendances de Nova (`packetevents`, éventuellement Apollo / Citizens).

## Scénario UHC 39-45

Module Gradle **`uhc3945`** : UHC à rôles cachés sur le thème de la Seconde Guerre mondiale, branché sur le framework camps/rôles de Nova (`ScenarioRole` / `ModeKit`). La boucle UHC (bordure, épisodes, meetup, PvP) est celle du core.

### Camps

| Camp | Comportement |
| --- | --- |
| **Axe** | Camp organisé : victoire groupée, les membres se connaissent. |
| **Résistance** | Camp fragmenté : victoire de camp ; cellules / authentification **à venir**. |
| **Civils** | Indépendants : victoire solo. |

### Rôles et pouvoirs

Les pouvoirs suivent les `Ability` Nova (`UseAbility` / `CommandAbility`), avec messages FR, cooldowns, utilisations limitées et **portes d’épisode**. Pas de Strength auto-win, pas de révélation d’un camp entier via un pouvoir.

**Axe**

| Rôle | Type | Pouvoir |
| --- | --- | --- |
| Commandant | unique | **Ordre** (ép. 2, item) : Speed I + Résistance I aux Axe proches. Sa mort affaiblit les Soldats. |
| Officier | unique | **`/role rapport`** (ép. 2) : position approximative du Commandant. |
| Soldat | masse | **Charge** (ép. 2, item) : Speed II + Résistance I, courte durée. Plus faible sans Commandant. |

**Résistance**

| Rôle | Type | Pouvoir |
| --- | --- | --- |
| Chef de réseau | unique | **Signal** (ép. 3, item) : nombre de présences + direction, **sans noms**. |
| Résistant | masse | **Cachette** (ép. 2, item) : invisibilité brève. |
| Médecin | unique | **Soin** (ép. 2, item) : quelques cœurs, 3 utilisations. 1 pomme d’or au briefing. |
| Saboteur | unique | **Sabotage** (ép. 4, item) : silence des pouvoirs + perte des buffs, 2 utilisations. |

**Civils**

| Rôle | Type | Pouvoir |
| --- | --- | --- |
| Civil | filler | **Instinct** (ép. 3, 1×/épisode) ; **`/role allegeance rester\|soutenir`** (ép. 5, 1×, ne change pas le camp). |
| Informateur | unique | **`/role analyser`** (ép. 2) : santé / distance / équipement, jamais le rôle. **`/role transmettre`** (ép. 3). |
| Contrebandier | unique | **Colis** (ép. 3, item) : or + pomme d’or. Sneak + visée pour le donner. |

Hors périmètre (autres agents) : cellules / authentification, knowledge service, limites de groupes.

### Activer le scénario

1. Démarrer le serveur avec **NovaUHC** + **UHC3945** (le module s’enregistre ~1 s après le enable, comme Ultimate / ScenarioPlus).
2. En host : `/h config` → menu des **scénarios spéciaux** → activer **UHC 39-45**.
3. Ouvrir la config du scénario pour composer les rôles (incrémenter Commandant, Soldats, etc.).
4. Lancer la partie : les rôles sont distribués au timer PvP (ou au timer rôles du scénario), comme les autres modes `ScenarioRole`.

Un seul scénario spécial à la fois : n’activez pas Legend / Nuzlocke en même temps que 39-45.
