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

Module Gradle **`uhc3945`** : UHC à rôles cachés, branché sur `ScenarioRole` / `ModeKit`. La boucle UHC (bordure, épisodes, meetup, PvP) est celle du core.

### Camps

| Camp | Connaissance | Victoire (inchangée) |
| --- | --- | --- |
| **Axe** | Le **noyau** (Commandant + Soldats) se connaît. L’**Infiltré** est isolé : pas de roster Axe. | Victoire de camp |
| **Résistance** | Cellules de **2–3** (+ isolé possible). Pas de dump du camp. Liaison via authentification. | Victoire de camp |
| **Civils** | Aucun allié automatique. | Victoire solo |

### Cellules et authentification

À la distribution des rôles, les Résistants sont découpés en cellules (ex. 9 joueurs → 3+3+2+1). Intra-cellule : les membres se connaissent. Inter-cellules :

1. `/role code` — mot de reconnaissance (incomplet). Le **mot secret** n’est visible que du chef (ou d’un isolé / si le chef est hors-jeu).
2. `/role auth <mot>` — indice reconnu, **doute** (incomplet).
3. `/role auth <mot> <secret>` — en attente.
4. `/role confirmer <joueur>` — le chef (ou un membre si pas de chef vivant) valide. Les deux cellules apprennent alors les **pseudos** de l’autre réseau, pas un dump de tout le camp.

`/contacts` et `/camp` n’affichent que la connaissance officielle du joueur. `/role mates` reste le listing KPI Nova.

### Infiltration (Axe)

Rôle unique **Infiltré** : `/role intercepter` capte un **fragment** lors d’une auth, `/role usurper <mot>` le rejoue. Le réseau est alerté ; l’Infiltré **n’obtient pas** la liste des membres.

### Rôles

- **Axe** : Commandant (unique), Soldat (masse), Infiltré (unique, isolé)
- **Résistance** : Chef de réseau (unique), Résistant (masse)
- **Civils** : Civil (filler), Informateur (unique)

Hors périmètre ici : pouvoirs de combat, limites de groupes pre-meetup, polish des wincons.

### Activer et playtester

1. Démarrer le serveur avec **NovaUHC** + **UHC3945** (enregistrement ~1 s après l’enable).
2. Host : `/h config` → scénarios spéciaux → **UHC 39-45**.
3. Composer au minimum : 1 Commandant, quelques Soldats, 1 Infiltré, 1 Chef de réseau, plusieurs Résistants (idéalement 5+ pour voir cellules + isolé), Civils au besoin.
4. Lancer la partie. Les rôles tombent au timer PvP (ou timer rôles du scénario).
5. Côté Résistance : vérifier `/role`, `/contacts`, `/role code`, échanger un mot en vocal, `/role auth`, `/role confirmer`.
6. Côté Infiltré : `/role intercepter` pendant une auth, puis `/role usurper`.
7. Côté Axe noyau : `/contacts` liste le noyau, pas l’Infiltré. Le Commandant est informé qu’un Infiltré existe sans nom.
8. Logs serveur `[UHC 39-45]` : cellules, codes, auth, intercept.

Un seul scénario spécial à la fois : n’activez pas Legend / Nuzlocke en même temps que 39-45.
