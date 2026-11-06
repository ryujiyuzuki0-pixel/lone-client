# Lone Client (formerly Pulse Client)

An original Fabric PvP HUD client for **Minecraft 1.21.11** (Java 21). Press **Right Shift** in game.

## Build
```
./gradlew build
```
The jar is `build/libs/pulse-client-1.0.0.jar` (ignore the `-sources` jar).
No local setup? Push this folder to GitHub: the included workflow builds the jar and attaches it as an artifact.

## Install
Put the jar in `.minecraft/mods/` with Fabric Loader 0.19.2+ and Fabric API 0.141.3+1.21.11.

## What's new: Lone Client UI

* **Title screen**: snowy Minecraft backdrop (slow pan + falling snow), the Lone Client logo, and buttons
  SinglePlayer, Multiplayer, Cosmetic, Mods, Settings and Lone Mods. Your own skin stands on the right with a
  Wardrobe button (capes). **Mods** lists the mods installed in your game (search, details). **Lone Mods** opens the
  client's built-in modules. Up/Down/Enter work too. Turn it off in Lone Mods > Settings > Title screen.
* **Lone Mods menu** (title screen "Lone Mods", or Right Shift in game): sidebar with Favorites / All / HUD / Visual /
  Gameplay / Utility, Settings, Friends, Cosmetics, HUD Editor and profiles. Search with Ctrl+F.
  Click a card for its settings, the switch to toggle it, the star to favorite it.
* **Every module has its own settings**: sliders (mouse wheel works, right click resets), choices,
  full color pickers (hue / saturation / brightness / opacity, presets, follow-theme and rainbow modes),
  keybinds, and per-module "Reset". Every HUD element also gets scale, background, corner radius,
  accent line, text / label colors, text shadow and exact X / Y position.
* **Profiles**: save separate setups (sidebar bottom-left). Friends: simple local list.

## Modules (25)
HUD: FPS, Ping, Coordinates, Clock, Keystrokes, Armor, Potion Effects, Totem Count, Item Counters,
Cooldowns, Inventory, Watermark. Gameplay: CPS, Combo, Reach, Toggle Sprint.
Visual: Custom Crosshair, Low Health Vignette, Client Theme, Cape. Utility: Zoom, Waypoints,
FPS Boost, Now Playing (Spotify).

The in-game cape hook is wired through a mixin; textures are in `assets/pulse/textures/cape/`.
"Edit HUD" lets you drag elements. Settings save to `config/pulse.json` (profiles: `config/pulse-profile-NAME.json`).

## Spotify
1. Create an app at https://developer.spotify.com/dashboard
2. Add the redirect URI `http://127.0.0.1:43019/callback`
3. In game: `/pulse spotify id <client id>` then `/pulse spotify login`
Play/pause/next need Spotify Premium. The refresh token is stored in `config/pulse-spotify.json`.

## Multiple Minecraft versions

One source tree (`src/`) builds every version listed in `settings.gradle`, using
[Stonecutter](https://stonecutter.kikugie.dev/). Per-version values live in
`versions/<mc>/gradle.properties`.

Build all versions: `./gradlew build` (jars land in `versions/<mc>/build/libs`).

Currently builds: 1.21.9, 1.21.10, 1.21.11. Versions before 1.21.9 need extra branches
(the `KeyEvent`/`MouseButtonEvent` input API doesn't exist there).

To add a version:
1. Create `versions/<mc>/gradle.properties` with `minecraft_version`, `minecraft_dep`,
   `fabric_version` and `java_version` (copy `versions/1.21.11` and edit).
2. Add the version to `versions '...'` in `settings.gradle`.
3. Where the game API differs, wrap the code in Stonecutter conditionals, e.g.
   `//? if >=1.21.9 { ... //?} else { ... }`.

## Extra modules

Inventory HUD, Cooldowns (pearls/gapples/chorus/wind charges), Item Counters, Waypoints and
Watermark. All are off by default; enable them in the menu (Right Shift).
Waypoints: `/pulse waypoint add <name>`, `remove <name>`, `list`, `clear`.

## FPS Boost

Performance tab -> **FPS Boost** (on by default). It lowers the vanilla video settings that cost the most frames and
puts yours back when you turn it off or close the game. Pick a preset (Balanced, Max FPS, Potato) or tune each row:
uncapped framerate, VSync, particles, clouds, entity shadows, entity distance, biome blend and a render distance cap.

- It only lowers values. A setting you already run lower is never raised.
- If you change a setting yourself in the vanilla menu while it is on, that setting is left alone when you turn it off.
- Your originals are kept in `config/pulse-fpsboost.json` while it is active, so a crash can't leave you on boosted values.
- It only changes video settings (no mixins). For faster chunk and entity rendering as well, run it next to Sodium and Lithium.

## Capes

Eight bundled capes (Pulse, Japan, Heartbeat, Nightfall, Breaker, Aurora, Ember, Frost). Pick one in the
Cosmetics tab; it shows on your own player in game (other players don't see it). A mixin
(`AbstractClientPlayerMixin`) swaps the cape into your skin. Textures are 64x32 PNGs in
`assets/pulse/textures/cape/`; add your own by dropping a PNG there and adding its name to `CapeArt`.

## Lone logo on other Lone Client users

Players running Lone Client get the logo in front of their name in the tab list and on their nametag
(yours always shows). To find each other they use a small presence server:

1. Host `presence-server/server.js` anywhere (`node server.js`).
2. Put its address in `SERVER_URL` in `presence/Presence.java` and build. No commands needed.

Settings > Lone users lets you hide the logos or stop announcing that you use Lone Client.
The server does not verify identities, so someone could fake the logo; fine for friends, not for a big public network.

## Texture Packs (PacksMC)

Title screen > **Texture Packs**.

* **My packs**: turn installed packs on or off, change their order (Higher / Lower), open the folder.
  Changes apply when you press Done.
* **PacksMC**: search and sort the packsmc.com catalog (name, author, versions, downloads, license).
  It uses the official PacksMC API, which needs a free key: sign in on packsmc.com, open API Access, create a key,
  and paste it into the screen. It is stored in `config/lone-packsmc.txt` and only sent to packsmc.com.
* **Get on PacksMC** opens the pack's page; PacksMC only allows downloads through the site (that is how creator
  credit, link lockers and Packs+ work), so the API never gives out file links and this client does not fetch pack files.
  After downloading, press **Import downloads**: it copies pack zips from your Downloads folder (changed in the last
  2 hours, with a pack.mcmeta) into `resourcepacks/`.
* For packs that appear in game without a browser step, PacksMC's own mod (packsmc.com/mods) syncs through a linked account.

## License

All rights reserved. See the LICENSE file.
