# Changelog

---

---

## v0.2.0 - August 13th 2025

### Updated

### AutominePlus
- **Renamed & upgraded AutoCityPlus** with new bedrock-clearing features.
- Automatically mines blocks adjacent to a nearby player’s surround, with:
    - Optional **Bedrock mining** toggle.
    - **Clear Upper Bedrock** option: Automatically break bedrock in **your own upper hitbox** when phased into it to free AutoMine/AutoCrystal.
    - Prioritize **bedrock under target's feet** if enabled.
- Supports **support block placement** under break target if missing.
- Fully configurable render, rotation, and chat feedback.
- Safety checks to avoid scuffed rotations unless clearing upper bedrock.
- All original AutoCityPlus settings included with added options:
    - `Prioritize Target Standing Bedrock`
    - `Clear Upper Bedrock`

### Added

### AutoWebFeetPlace
Automatically places a cobweb, ladder, or button in a broken surround slot under or beside the target to prevent them from replacing their surround.

- Detects when a target is already in a surround.
- Monitors surround blocks for **break events** and places the chosen item immediately when a hole appears.
- Works with **cobwebs, ladders, or any button type**.
- Adjustable spam rate for repeated placement attempts.
- Includes **Silent Swap** and **Inventory Pull** so items can be used from inventory without hotbar disruption.
- Can prioritize the hole closest to you or pick the first available.
- Optional rotation to face the placement block.

**Settings:**
- `Place Item`: Choose between `Cobweb`, `Ladder`, or `Button` (all types supported).
- `Range`: Target detection distance.
- `Ignore Friends`: Skip players on your friends list.
- `Ignore Naked`: Skip players without armor.
- `Attempts Per Second`: Placement spam rate.
- `Rotate`: Rotate player toward placement.
- `Silent Swap`: Place from hotbar silently without switching slots.
- `Only When Air`: Only attempt placement if the hole is air/replaceable.
- `Prefer Closest Hole`: Prioritize nearest broken surround hole.
- `Grab From Inventory`: Move items from main inventory to a hotbar slot when not found in hotbar.
- `Hotbar Slot`: Slot number for grabbed items.

---

## v0.1.9 - August 10th 2025

### Added

### AutoPearlThrow
- Added automatic Ender Pearl throw on totem pop with wall avoidance, jump assist, and smart pitch selection.
- New 360° auto-aim with option to avoid throwing toward enemies.
- Reserve totem protection – skips throws when at or below a set totem count.
- Silent swap support with inventory pulling if no pearls in hotbar.
- Fallback straight-up throw when fully trapped.
- Customizable cooldown, throw delay, and pitch settings.
- Manual test keybind for instant pearl throws without totem pop.
- Debug mode for logging throw decisions.

### Fixed

### AutoPearlTeleport (Input)
- **Fixed:** `Force Teleport` toggle now sends the **threshold value** from the slider instead of the **current totem count**, ensuring the **Output** module always triggers immediately.
- **Improved:** Added a safety check to avoid sending messages if the Alt Account field is blank.

---
## v0.1.8 - August 6th 2025

### Added

### 🌀 AutoPearlTeleport (Input)
Teleports you to your enderpearl when your totem threshold is reached by silently messaging your alt to activate your enderpearl stasis chamber.

- Sends a `/msg` to your alt when your **totem count reaches a threshold**.
- Tracks totems in **inventory, hotbar, and offhand**.
- Optional **force teleport toggle** to trigger teleport instantly.
- Includes automatic cooldowns and resets after join/reconnection to a server.

**Settings:**
- `Alt Name`: Your alt account's username to send msg to (blank by default).
- `Totem Threshold`: Number of totems to trigger escape (sync with Output module).
- `Force Teleport`: Send a manual `/msg` to force teleport.

---

### 🌀 AutoPearlTeleport (Output)
Receives a message from your main account and right-clicks the nearest trapdoor to teleport when the totem threshold is reached.

- Listens for `/msg` from the main account to trigger teleportation.
- Parses totem count and activates if at or below threshold.
- Finds and interacts with the **closest trapdoor** automatically to activate enderpearl stasis chamber.
- Supports **all trapdoor types**.
- Ignores spoofed messages not sent by your real account.

**Settings:**
- `Main Name`: Your main account's username to receive msg from(blank by default).
- `Totem Threshold`: Match this with the Input module for accurate syncing.


---

## v0.1.7 - August 4th 2025

### Changed
- Updated addon base from **Minecraft 1.21.1** to **1.21.5** for compatibility with latest MioClient version.

### Added

### AutoCityPlus
- Automatically mines blocks adjacent to a nearby player's surround.
    - Detects and targets the nearest valid player within range.
    - Scans all four sides around the player’s feet to find a breakable block.
    - Can optionally target **bedrock** blocks using the `Mine Bedrock` toggle.
    - Places **support blocks** (e.g., obsidian) underneath if there’s no block below.
    - Uses **natural vanilla mining** (no packet mining).
    - Includes optional `Rotate` toggle to face the break target.
    - Renders a box on the block being mined using color and shape settings.

---

## v0.1.6 - July 26th 2025
### Fixed
- Reduced delay for chat feedback in `AntiConcrete`.
- Fixed chat delay when activating or running `AutoTNTplus`.

---

## v0.1.5 - July 25th 2025
### Added

### AntiConcreteDetection
- Detects buttons or torches placed under nearby enemies' feet.
  - Breaks detected blocks with left-click, with toggle for `Tap to break` vs `Hold to break`.
  - Includes `Rotate` toggle for snapping to face target block.
  - `Hold to break` now only targets the block at the enemy's feet (inside lower hitbox), preventing accidental block breaking based on camera direction.

### AutoConcrete
- `Only In Hole` setting for `AutoConcrete`:
    - Ensures concrete only drops if the target is fully surrounded by blocks at their feet.
- Added more support for falling blocks:
    - Concrete Powder
    - Gravel
    - Sand
    - Red Sand
    - Suspicious Sand
    - Suspicious Gravel

### Fixed
- Crash caused by null concrete positions in `AutoConcrete`.
- Crash when using `AntiConcreteDetection` and `AutoConcrete` simultaneously due to missing null checks and out-of-bounds access.
- `Hold to break` logic no longer breaks random blocks based on view direction—targeting is now precise to block under enemy.

### Changed
- Separated `Anti-AntiConcrete` functionality from `AntiConcrete` into its own module (`AntiConcreteDetection`).
- Improved obsidian pillar placement logic and support check before dropping blocks.
- Added safe loop over concrete position array with null checks.
- Renamed `AntiAntiConcrete` to `AntiConcreteDetection` for clarity and PVP-themed naming.

---

## v0.1.4 - July 24th 2025

### AutoConcrete
- Added support for placing **up to 3 concrete blocks** at once via a new `concrete-count` slider.
- Automatically adjusts **obsidian pillar height** to match concrete count:
    - 2 blocks tall for 1 concrete
    - 3 blocks tall for 2 concrete
    - 4 blocks tall for 3 concrete
- Added `disable-on-use` toggle to auto-disable after one use.

### AntiConcrete
- Added `anti-anticoncrete` toggle to break **enemy buttons or torches** placed under them.
- Added new `break-mode` setting with two options:
    - `Tap`: Single hit to break target block
    - `Hold`: Continuously holds left click to break
- Added `rotate` toggle to rotate toward enemy blocks when breaking or placing.
- Enhanced **silent inventory swap** logic to return items after delay automatically.
- Detection logic for button/torch identification.

---

## v0.1.3 - July 23rd 2025

-  Fixed crash on init due to `Items` not loading
-  Updated `fabric.mod.json` and `gradle.properties`
-  Version number now displays correctly
-  Compatible with **Minecraft 1.21.1** & **Meteor Client 0.5.8**

> This version silently swaps and restores buttons from inventory (AntiConcrete module) and supports airplace toggling (AutoConcrete module).
