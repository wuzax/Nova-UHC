# Playtest UHC 39-45 (Spigot 1.8.8)

Jars de la release `v0.1.0-3945-playtest`. Compilation : **JDK 25** (`bash ./gradlew build`). Les jars sont du **bytecode Java 25** (CloudNet RC15 impose ce JDK à la compile) : lancer Spigot 1.8.8 avec **Java 25** (`UnsupportedClassVersionError` sur 8/17/21).

## 1. Jars à déposer dans `plugins/`

**Minimum (scénario UHC 39-45) :**

| Fichier | Plugin Bukkit | Rôle |
| --- | --- | --- |
| `API.jar` | **NovaUHC** | Core + module `api` (déjà fusionné dans ce jar) |
| `UHC3945.jar` | **UHC3945** | Scénario 39-45 |
| `packetevents-spigot-2.13.0.jar` | **packetevents** | Dépendance dure de NovaUHC |

Ne **pas** ajouter `api/build/libs/api.jar` : les classes API sont déjà dans `API.jar`.

**Optionnel (autres modes, pas requis pour 39-45) :** `ScenarioPlus.jar`, `Ultimate.jar`. Un seul scénario spécial à la fois.

Chemins après `./gradlew build` :

- `core/build/libs/API.jar`
- `uhc3945/build/libs/UHC3945.jar`
- `scenarioplus/build/libs/ScenarioPlus.jar` (optionnel)
- `ultimate/build/libs/Ultimate.jar` (optionnel)

## 2. CloudNet

**Pas obligatoire.** Sans `CloudNet-Bridge`, Nova charge en standalone : l’intégration n’est initialisée que si ce plugin est présent (les APIs CloudNet ne sont pas ombrées dans `API.jar`).

Ne mettez pas CloudNet sur un Spigot 1.8.8 de playtest.

## 3. MongoDB / API

Nova **n’a pas** de clé `mongodb://` dans `config.yml`. Le driver Mongo est ombré dans `API.jar` uniquement pour sérialiser des `org.bson.Document` (configs / scénarios).

La persistance réseau passe par l’API HTTP :

```yaml
# plugins/NovaUHC/config.yml
api:
  url: "https://api.nova-code.fr"   # obligatoire (sinon shutdown)
  key: "playtest"                   # obligatoire (sinon shutdown)
  server-name: "playtest-3945"
```

Sans clé API réelle, le login échoue dans les logs (`API authentication failed`) mais le plugin **reste chargé**. Gameplay, host et 39-45 fonctionnent.

Mongo local si vous branchez plus tard un backend :

```bash
docker run -d --name nova-mongo -p 27017:27017 mongo:7
```

URI typique côté **votre** API, pas Nova : `mongodb://127.0.0.1:27017`.

Playtest carte existante (pas de prégén) :

```yaml
dev: true
language: fr_FR
```

## 4. Activer UHC 39-45

1. Démarrer Spigot 1.8.8, vérifier `[UHC 39-45] scénario enregistré` (~1 s après l’enable).
2. Le **premier joueur** devient host (`/h` OK). Ou UUID dans `staff:` de `config.yml`.
3. `/h config` → **Mode de Jeu** (tête rouge) → **UHC 39-45**.
4. Composer (clic sur le mode) : 1 Commandant, Soldats, 1 Infiltré, 1 Chef de réseau, Résistants, Civils.
5. Lancer la partie (bouton vert du menu host). Les rôles tombent au timer PvP.

Ne pas activer Legend / Nuzlocke / un autre spécial en même temps.
