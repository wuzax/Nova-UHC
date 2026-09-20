# LG-UHC — plugin officiel Ph1Lou, pas un port Nova

Ce fork **n’intègre pas** Loup-Garou UHC en réécrivant les rôles dans `ScenarioRole` / `ModeKit`.
Les ~99 rôles, le cycle jour/nuit LG, le vote, les amoureux, l’infection et la victoire restent
ceux de **[WereWolfPlugin](https://github.com/Ph1Lou/WereWolfPlugin)** (API
**[WereWolfAPI](https://github.com/Ph1Lou/WereWolfAPI)**).

WereWolfPlugin est un **moteur UHC complet**, multi-versions **1.8.8 → 1.21+**, bytecode **Java 8**,
avec `VersionUtils` et BiomeMapping. Nova-UHC est **un autre** moteur UHC complet, ciblé
**Spigot/Paper 1.8.8**.

Les deux sont sous **GNU Affero GPL v3**. Le gameplay LG et le nom « WereWolf / LG UHC »
appartiennent à **Ph1Lou**. Toute redistribution d’une version modifiée doit rester AGPL et
**créditer Ph1Lou**.

## Qui fait quoi

| Intention | Moteur | Plugins |
| --- | --- | --- |
| UHC classique, TaupeGun, Ultimate, **UHC 39-45** | **Nova** | `API.jar` (NovaUHC) + `Ultimate.jar` / `UHC3945.jar` / `ScenarioPlus.jar` |
| **LG-UHC** (Loup-Garou) | **WereWolf** | `WereWolfPlugin.jar` officiel |

**Ne lancez jamais une partie Nova et une partie WereWolf en même temps.**

- Nova : `/h` (host), config, scatter, bordure, puis start Nova.
- WereWolf : `/a` (admin), composition des rôles, `/a start` (commande interne `werewolf.commands.admin.start`).
- Joueurs LG : `/ww`.

Le module optionnel **`lguhc`** (`LGUHC.jar`) n’est **pas** un moteur LG. C’est un pont mince :
entrée « **LG-UHC (Ph1Lou)** » dans les modes spéciaux Nova, détection du service Bukkit
`GetWereWolfAPI`, et **blocage du start Nova** tant que cette entrée est active — pour éviter
les deux scatter / deux bordures / deux scoreboards.

## Installation (serveur 1.8.8)

1. JAR Nova habituels dans `plugins/` (`API.jar`, éventuellement Ultimate / 39-45 / ScenarioPlus).
2. **WereWolfPlugin.jar** dans `plugins/`. L’API est **shade** dans ce JAR : pas besoin d’un
   `WereWolfAPI.jar` séparé sur le serveur (l’API sert aux addons à la compilation).
3. Optionnel : `LGUHC.jar` (ce dépôt, module Gradle `lguhc`) pour l’entrée host + garde-fou start.
4. Redémarrer. Dans Nova : `/h config` → modes spéciaux → **LG-UHC (Ph1Lou)** (pense-bête).
5. Composer et lancer le LG **dans WereWolf** (`/a`), pas avec le bouton start Nova.

### Télécharger le JAR officiel

- SpigotMC : [Loup Garou UHC / WereWolf UHC, resource **73113**](https://www.spigotmc.org/resources/loup-garou-uhc-werewolf-uhc.73113/)
- Sources : https://github.com/Ph1Lou/WereWolfPlugin — licence AGPL-3.0

Ce dépôt **ne redistribue pas** le JAR Ph1Lou. Utilisez le téléchargement Spigot ou compilez
depuis les sources.

### Compiler WereWolf (JDK 8+)

WereWolfAPI et WereWolfPlugin ciblent **Java 8**. Maven, pas Gradle.

```bash
git clone https://github.com/Ph1Lou/WereWolfAPI.git
cd WereWolfAPI && mvn install -DskipTests

git clone https://github.com/Ph1Lou/WereWolfPlugin.git
cd WereWolfPlugin && mvn package -DskipTests
```

Le plugin attend `fr.ph1lou:werewolfapi:1.12` : `mvn install` de l’API d’abord.
Artefact typique : `WereWolfPlugin/target/werewolfplugin-*.jar` (shade).

Nova se compile à part (`bash ./gradlew build`, **JDK 25+** à cause de CloudNet). Ne mélangez
pas les toolchains : JDK 8 pour Ph1Lou, JDK 25 pour Nova.

## Pourquoi pas un port ModeKit

`uhc3945` et Ultimate (Legend, Nuzlocke) enregistrent des **rôles Nova** via `ModeKit` /
`ScenarioRole`. Reprendre LG de cette façon impliquerait de réécrire ~99 rôles, le pipeline
de mort/résurrection, le chat des loups, les lovers, le vote, etc. C’est volontairement
**hors scope**.

WereWolfAPI n’est pas une couche de rôles autonome : c’est le contrat du **plugin**
(`GetWereWolfAPI` publié dans `ServicesManager`). Les classes annotées `@Role` vivent dans
WereWolfPlugin.

## Limites du pont (honnête)

WereWolf **démarre son moteur dès `onEnable`** : lobby, `werewolf_map`, FastBoard, prise en
charge des joins, plateforme de spawn si `default_lobby: true`. Le stub `lguhc` **ne met pas
WereWolf en veille**. Conséquences si les deux JAR sont dans le même `plugins/` :

- conflits possibles lobby / scoreboard / monde / messages de join ;
- la partie LG se lance quand même via `/a start`, pas via Nova ;
- pour une UHC Nova (39-45, Taupe…), le plus sûr est de **retirer WereWolfPlugin.jar**
  (ou d’avoir deux dossiers plugins / deux instances).

Le pont empêche seulement de **démarrer une game Nova** tant que le mode spécial
« LG-UHC (Ph1Lou) » est coché.

## Licence

| Projet | Licence |
| --- | --- |
| Nova-UHC (ce fork) | AGPL-3.0 (`LICENSE`) |
| WereWolfPlugin / WereWolfAPI | AGPL-3.0 (Ph1Lou) |

Combiner les deux sur un serveur réseau impose déjà l’AGPL (offre de sources). Ne pas
décréditer Ph1Lou. Ne pas revendiquer LG-UHC comme un mode « fait maison » Nova.
