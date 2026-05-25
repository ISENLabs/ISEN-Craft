# ISEN-Craft

Plugin Minecraft multi-serveur (hub + proxy) pour réseau Paper/Waterfall.

## Architecture

```text
┌──────────────────────────────────────────────────────────────────┐
│                    Plugin Layer (Deployable JARs)                │
├────────────────────────────┬─────────────────────────────────────┤
│   hub (Paper plugin)       │   proxy (Waterfall/BungeeCord)      │
│   `hub/src/main/java/`     │   `proxy/src/main/java/`            │
│   HubPlugin, Managers,     │   ProxyPlugin, HubManager,          │
│   Listeners, Commands      │   Commands                          │
└────────────┬───────────────┴──────────────┬────────────────────-─┘
             │                              │
             ▼                              ▼
┌────────────────────────────┬─────────────────────────────────────┐
│   api-paper                │   api-bungee                        │
│   `api-paper/src/main/`    │   `api-bungee/src/main/`            │
│   PaperCommandBridge,      │   BungeeCommandBridge,              │
│   PaperSender, PaperLogger │   BungeeSender, BungeeLogger        │
│   ItemBuilder, MessageUtils│   MessageUtils                      │
└────────────┬───────────────┴──────────────┬────────────────────-─┘
             │                              │
             └──────────────┬───────────────┘
                            ▼
             ┌──────────────────────────────┐
             │         api-common           │
             │  `api-common/src/main/java/` │
             │  IsenCommand, IsenSender,    │
             │  IsenCommandArgument,        │
             │  IsenLogger                  │
             └──────────────────────────────┘
```

| Module | Responsabilité | Fichier clé |
|--------|----------------|-------------|
| `api-common` | Framework de commandes et abstractions de logs sans dépendance plateforme | `api-common/src/main/java/fr/isen/common/` |
| `api-paper` | Adaptateur Paper — relie l'API Bukkit aux abstractions Isen, fournit les utilitaires | `api-paper/src/main/java/fr/isen/paper/` |
| `api-bungee` | Adaptateur BungeeCord — relie l'API Waterfall aux abstractions Isen | `api-bungee/src/main/java/fr/isen/bungee/` |
| `hub` | Plugin Paper : gameplay du hub (spawn, navigation, protections) | `hub/src/main/java/fr/isen/hub/HubPlugin.java` |
| `proxy` | Plugin BungeeCord : routage proxy (commande hub, redirection lobby) | `proxy/src/main/java/fr/isen/proxy/ProxyPlugin.java` |

## Setup

**Prérequis :**
- Java 21 JDK
- Serveur Paper 1.21
- Proxy Waterfall 1.21

**Build :**

```bash
./gradlew build             # tous les modules
./gradlew :hub:build        # hub uniquement
./gradlew :proxy:build      # proxy uniquement
```

Les JARs produits se trouvent dans `hub/build/libs/` et `proxy/build/libs/`. Copier dans le dossier `plugins/` du serveur correspondant.

## Ajouter une commande

Toute nouvelle commande étend `IsenCommand<PaperSender>` (ou `BungeeSender` pour le proxy), déclare ses sous-commandes via `IsenCommandArgument`, puis s'enregistre dans le Manager via `plugin.registerCommand("nom", new MaCommande(...))`.
