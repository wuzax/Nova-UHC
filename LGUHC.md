# LG-UHC (module Nova `lguhc`)

Mode spécial **LG-UHC** : UHC à rôles cachés **inspiré** de [WereWolfPlugin / WereWolfAPI](https://github.com/Ph1Lou/WereWolfPlugin) (Ph1Lou, **AGPL-3.0**).
Réimplémenté sur **Nova 1.8.8** (`ScenarioRole` / `ModeKit`), comme `uhc3945`.

**Ce n’est pas le plugin Ph1Lou.** Le JAR actuel (`spigot 1.21.4`, `api-version: 1.13`) **ne charge pas** sur le serveur Nova 1.8.8. Ne le mettez pas dans `plugins/`. Détail et matrice A/B/C : [`lguhc/DESIGN.md`](lguhc/DESIGN.md).

Gameplay UHC (host, scatter, bordure, PvP, meetup) = **Nova**. Rôles / camps / chat loups / couple / victoires LG = **ce module**.

## Activer

1. JARs : `API.jar` (NovaUHC) + `LGUHC.jar`. `plugin.yml` : `depend: [NovaUHC]`.
2. Enregistrement ~1 s après l’enable (même délai que 39-45).
3. Host : `/h config` → modes / scénarios **spéciaux** → **LG-UHC**.
4. Un seul spécial à la fois : activer LG-UHC désactive 39-45 / Ultimate spéciaux déjà cochés. `team_size` passe à 1.
5. Composition (UI Nova) : défauts **2 Loups-Garous**, 1 Voyante, 1 Sorcière, 1 Chasseur, 1 Petite Fille, 1 Cupidon, 1 Ancien ; le reste en **Villageois** (filler). Ajustable par camp.
6. Start Nova habituel. Les rôles tombent au timer PvP (ou timer rôles du scénario).

Ne pas casser `uhc3945` : n’activez pas les deux.

## Camps et victoire

| Camp | Connaissance | Victoire |
| --- | --- | --- |
| **Village** | Pas de roster village | Plus aucun loup vivant |
| **Loups** | Se connaissent ; chat `lg <msg>` | Plus aucun villageois vivant |
| **Couple** (Cupidon) | Annonce + chat de lien | Les amoureux (et seulement eux) sont les derniers vivants |
| **Solitaires** | Camp prévu | S’il ne reste que ce camp (aucun rôle solitaire dans le MVP) |

## Rôles MVP

| Rôle | Commande / déclencheur |
| --- | --- |
| Villageois | — |
| Loup-Garou | `lg <message>` ; force + night vision |
| Voyante | `/role voir <joueur>` (1×) |
| Sorcière | `/role soigner <joueur>` (1×, soi autorisé) ; `/role tuer <joueur>` (1×) |
| Chasseur | Tue l’assassin à la mort |
| Petite Fille | Entend `lg` ; night vision |
| Cupidon | `/role couple <j1> <j2>` (1×) |
| Ancien | Survive une première mort |

**Renard** : non porté.

## Écarts vs Ph1Lou (volontaires)

- Pas de vote villageois, pas de cycle jour/nuit LG, pas d’infection, pas des ~99 rôles.
- Force loup **permanente** (UHC), pas limitée à la nuit WereWolf.
- Matériaux 1.8.8 uniquement (pas de `SHIELD` / `ENDER_EYE` 1.13+).
- WereWolfAPI n’est **pas** une dépendance Gradle.

## Licence

| Projet | Licence |
| --- | --- |
| Nova-UHC | AGPL-3.0 |
| WereWolfPlugin / WereWolfAPI | AGPL-3.0 (Ph1Lou) |

Ce module reste AGPL. Créditer **Ph1Lou** pour le design LG UHC. Auteurs du plugin : `wuzax`, `Ph1Lou` (`lguhc/src/main/resources/plugin.yml`).
