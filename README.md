# Xenon's Addon

A Meteor Client addon for Minecraft 1.21.5 that includes powerful PvP automation tools like **AutoConcrete**, **AntiConcrete**, **AntiConcreteDetection**, **AutoTNTplus**, **AutoMinePlus**, **AutoPearlStasis**, **AutoPearlThrow**, **AutoWebFeetPlace**, **AntiFeetPlace**, and **BetterScaffold**.

---

## Features

### AutoConcrete
Automatically builds a vertical obsidian pillar beside a nearby target and drops concrete powder on their head.

- Builds a **2–4 block obsidian pillar** on one side of a target's surround.
- Drops **up to 3 concrete powder blocks** at once using the `Concrete Count` slider.
- **Air Place** mode allows dropping without support blocks.
- Detects **End Crystals** by the target to adapt obsidian pillar for better placement.
- Reuses existing obsidian pillars if detected by target.
- Smart resets when the target moves.
- Can automatically **disable after one use**.
- Support for falling blocks:
    - Concrete Powder
    - Gravel
    - Sand
    - Red Sand
    - Suspicious Sand
    - Suspicious Gravel

**Settings:**
- `Range`: Target detection distance.
- `Concrete Count`: Number of concrete blocks to place (1–3).
- `Pillar Delay`: Delay between obsidian block placements.
- `Drop Delay`: Delay after dropping concrete.
- `Rotate`: Rotate player toward block placements.
- `Detect Crystals`: Raises pillar height if End Crystal detected.
- `Air Place`: Enable dropping concrete in air.
- `Place Support`: Toggle obsidian pillar placement when Air Place is disabled.
- `Disable On Use`: Turns off the module automatically after one concrete drop.
- `Only In Hole` :Ensures concrete only drops if the target is fully surrounded by blocks at their feet.

---

### AntiConcrete
Automatically places a button beneath your feet to prevent concrete displacement.

- Detects when an enemy is nearby or when concrete is falling above you.
- Places any valid **button type** under you instantly.
- Smart mode places only when concrete is detected above.
- **Silent Swap** system moves button from inventory to hotbar and returns it after use.
- Rotates toward placement or target blocks when enabled.

**Settings:**
- `Mode`: `Strict` (always place at feet when target is in range) or `Smart` (only if concrete above).
- `Smart Range`: Enemy proximity trigger.
- `Silent Inventory Swap`: Enable silent hotbar swapping.
- `Hotbar Slot`: Which hotbar slot to use for button swapping.
- `Return Delay`: How long to wait before returning the button to inventory from hotbar.
- `Rotate`: Rotate toward blocks when placing or breaking.

---

### AntiConcreteDetection
- Detects buttons or torches placed under nearby enemies' feet.
    - Breaks detected blocks with left-click, with toggle for `Tap to break` vs `Hold to break`.
    - Includes `Rotate` toggle for snapping to face target block.

**Settings:**
- `Break Mode`: `Tap to break` & `Hold to break` methods used when breaking targeted button/torch(s) under enemies' feet.
- `Rotate`: Rotate player toward block being targeted.

---

### AutoTNTplus
Automatically places and ignites TNT on top of a pillar beside a target.

- Builds a **2-block obsidian pillar** beside the target.
- Drops and ignites **TNT** using Flint & Steel or Fire Charges.
- Supports **Air Place** for direct placement without support blocks.
- Smart ignition system finds nearby TNT blocks and lights them.
- Resets if the target moves.

**Settings:**
- `Target Range`: Distance to find a player.
- `Pillar Delay`: Delay between placing pillar blocks.
- `TNT Delay`: Delay after placing TNT.
- `Ignition Delay`: Delay before lighting TNT.
- `Rotate`: Rotate toward TNT before lighting.
- `Use Flint & Steel`: Toggle for flint ignition.
- `Use Fire Charge`: Toggle for fire charge ignition.
- `Air Place`: Enables TNT placement in air.
- `Place Support`: Builds obsidian pillar when air place is off.

---

### AutoMinePlus
An enhanced automine module with bedrock clearing and improved targeting.

- Scans for the **nearest valid target** within range.
- Checks all **four horizontal blocks** around the target's feet for mineable blocks.
- Can be configured to **target Bedrock** blocks when enabled.
- Supports placing a **support block (Obsidian)** underneath the break target if needed.
- Simulates **natural mining** using vanilla mining logic (no packet mining).
- Optional **rotation** to face the target block during mining.
- Sends **debug messages** in chat about target acquisition and mining status.
- **New:** Pause while eating toggle.

**Settings:**
- `Target Range`: Distance to search for players to target.
- `Break Range`: Maximum distance to mine blocks.
- `Mine Bedrock`: Allows bedrock blocks to be mined.
- `Prioritize Target Standing Bedrock`: Prefer mining Bedrock under the target's feet.
- `Clear Upper Bedrock`: Automatically break Bedrock in your own upper hitbox.
- `Ignore Friends`: Skip friends from targeting.
- `Ignore Naked`: Skip players without armor.
- `Support`: Place support block beneath the mining target if needed.
- `Place Range`: Distance limit for placing support blocks.
- `Rotate`: Rotate to face the mining block.
- `Chat Info`: Send status messages in chat (e.g. target found).
- `Chat Delay`: How often to send chat messages.
- `Swing Hand`: Toggle whether to swing your hand while mining.
- `Render Block`: Toggle rendering the mining target.
- `Shape Mode`: Choose between side, line, or both rendering shapes.
- `Side Color`: Color for box sides.
- `Line Color`: Color for box lines.
- `Pause While Eating`: Pauses mining when eating.

---

### 🌀 AutoPearlStasis
Merged + upgraded pearl stasis system with **MAIN** (sender) and **ALT** (receiver) modes, dual-alt support, proximity triggers, and a full **post-teleport re-arm assist** (walk to edge → look down → throw → step back). Also supports Baritone pathing if present.

**Modes**
- `mode`: `MAIN` (your main account—sends /msg and re-arms stasis) or `ALT` (your alt—flips trapdoor on command and reopens it).
- **Dual Alt** on MAIN: independently configure **Alt #1** and **Alt #2** (name, totem threshold, force toggle, hotkey).

**How MAIN works**
- Tracks your **totem count** and sends `/msg <alt> "<count> totem remaining"` when ≤ per-alt threshold.
- **Force Teleport** per-alt: toggle or **hotkey** (e.g., `G`, `F4`, `SPACE`); fires immediately.
- **Proximity Trigger** (optional): if a watched player enters radius, auto-/msg an alt and start teleport flow.
- Detects teleport by **position jump** or by receiving **`tp-ok`** from ALT to start the re-arm assist.

**How ALT works**
- Listens for `/msg` from your **Main Name** containing "`totem remaining`".
- If the number ≤ `totem-threshold`, it finds the **nearest armed stasis** (water + trapdoor + pearl), flips the **trapdoor**, and (optionally) sends **`tp-ok`** back to MAIN.
- Reopens the trapdoor after a delay to keep the chamber armed.

**Re-Arm Assist (MAIN, after teleport)**
- Searches for the closest valid stasis (water under trapdoor) within a **search radius**.
- Walks to the **edge** facing the water, looks **down**, ensures pearl ready (offhand/hotbar/silent-swap/inventory pull), **throws**, and **steps back** to a safe block; verifies pearl landed.
- Includes robust **water-exit** logic and optional **Baritone** pathing.
- Retries throw on miss (configurable count).

---

#### Settings

**General**
- `mode`: `MAIN` | `ALT`.

**MAIN — Alt #1**
- `alt1-name`: Username to `/msg`.
- `alt1-totem-threshold`: Auto-send when your totems drop to this count (or below).
- `alt1-force-teleport`: Force a `/msg` this tick.
- `alt1-force-key`: Optional hotkey (e.g., `G`, `F4`, `SPACE`). Blank = disabled.

**MAIN — Alt #2**
- `alt2-name`
- `alt2-totem-threshold`
- `alt2-force-teleport`
- `alt2-force-key`

**Proximity Trigger (MAIN)**
- `enable`: Turn the system on/off.
- `radius`: Scan radius (blocks).
- `names-list`: Add names individually (normalized, case-insensitive).
- `names-comma`: Comma-separated names (alternative input).
- `match-anyone-when-empty`: If no names configured, match anyone in radius.
- `prox-warmup-ticks`: Disarm for N ticks after joining.
- `send-to`: `ALT1` | `ALT2` | `BOTH` (which alt(s) to ping).
- `cooldown-ticks`: Min ticks between proximity fires.
- `chat-feedback`: Echo detections and actions to chat.
- `require-motion`: Only trigger if you’ve been moving recently.

**ALT (Receiver)**
- `main-account-name`: Your main’s username (required).
- `totem-threshold`: Flip trapdoor if received count ≤ this.
- `send-tp-confirm`: Whisper `tp-ok` back to main after flipping.
- `reopen-delay-ticks`: After using, wait N ticks then **ensure trapdoor is open** again.

**Stasis Assist (MAIN)**
- `auto-approach-stasis`: Walk to the stand block beside the water.
- `auto-rethrow`: Automatically aim/throw into chamber.
- `auto-start-when-near`: If a valid stasis is already nearby, start assist automatically.
- `throw-mode`: `SIMPLE_DOWN` (default) | `PRECISE_AIM`.
- `down-pitch-deg`: Pitch used when aiming down (82–90).
- `pitch-hold-ticks`: Hard-hold pitch so you see it before throw.
- `throw-window-ticks`: Time window to attempt throwing.
- `retry-gap-ticks`: Gap before a second throw attempt if needed.
- `step-back-after-throw`: Step back from edge after throwing.
- `step-back-ticks`: Base retreat ticks (auto-scales with distance).
- `step-back-distance`: Preferred retreat distance (blocks).
- `sneak-while-aiming`: Hold sneak while aiming/throwing.
- `sneak-aiming-ticks`: How long to hold sneak.
- `search-radius`: Scan radius (blocks) for water+trapdoor stasis.
- `approach-timeout-ticks`: Give up moving after N ticks.
- `edge-distance`: How close to stand before throwing.
- `use-chat-confirm`: Start assist after `tp-ok` (fallback is teleport distance detection).
- `post-teleport-delay-ticks`: Wait after `tp-ok` / teleport before starting.
- `pearl-verify-ticks`: Wait then verify pearl landed in water.
- `retry-on-miss`: Retry if no pearl detected.
- `max-retries`: Max retries on miss.
- `use-baritone-if-present`: Use Baritone for pathing when available.
- `surface-head-clearance`: Extra head height needed to consider “above water”.
- `advanced-water-escape`: Enhanced water exit mechanics.
- `debug-chat`: Verbose state logs.

**Water Exit Tuning**
- `exit-pitch-deg`: Pitch during exit pulses.
- `forward-hold-ticks`: Hold **W** this long per pulse.
- `sprint-hold-ticks`: Hold **Sprint** this long per pulse.
- `jump-pulse-ticks`: Tap **Jump** this many ticks per pulse.

**Inventory**
- `prefer-offhand`: Use offhand pearls first.
- `silent-swap`: Temporarily swap to pearls, then swap back.
- `swap-back-stricter-servers`: If `silent-swap` is **off**, restore pearls to original slot (for stricter servers).
- `pull-from-inventory`: If no pearls in hotbar, pull from main inventory.
- `prefer-empty-hotbar`: Prefer an empty hotbar slot when pulling.
- `temp-hotbar-slot`: Fallback hotbar slot [0–8] if no empty slot.

---

#### Notes
- MAIN sends messages like: `"/msg <alt> <N> totem remaining"`. ALT parses the **number** and flips the nearest armed stasis if `N ≤ totem-threshold`.
- ALT verifies **water + trapdoor + a pearl in the water** before flipping; then **reopens** the trapdoor after `reopen-delay-ticks`.
- Assist uses rotation packets (`LookAndOnGround`) and checks **pearl cooldown** to infer throw success; also explicitly counts pearls inside the stasis water.

---

## AutoPearlThrow
Automatically throws an Ender Pearl away from danger when your totem pops, with advanced aiming, obstacle avoidance, totem reserve logic, and inventory handling. Designed for high risk PvP situations to quickly escape.

### **Features**
- **Automatic Trigger on Totem Pop:** – Instantly schedules a pearl throw when your totem breaks.
- **360° Auto-Aim:** – Searches for the best throw direction in all directions, optionally avoiding throwing toward enemies.
- **Smart Pitch Selection:** – Automatically selects the lowest possible pitch for max range without hitting obstacles.
- **Wall & Obstacle Avoidance:** – Skips throws into nearby walls, raises pitch if blocked, and can fallback to throwing straight up if trapped.
- **Jump Before Throw:** – Optional jump to see over 2-block walls before throwing.
- **Reserve Totem Protection:** – Skips pearl throws when your total totems (in hotbar, inventory, offhand, and cursor) are at or below a set threshold.
- **Silent Swap:** – Temporarily swaps to pearls, throws, then swaps back without disrupting your hotbar setup.
- **Inventory Pulling:** – Can move pearls from your inventory into a hotbar slot automatically if none are available.
- **Manual Test Key:** – Option to bind a key to manually test throws without needing a totem pop.
- **Cooldowns & Delays:** – Configurable cooldown between throws and delay after totem pop.
- **Detailed Debug Logging:** – Optional debug mode for step-by-step throw decisions.

---

### **Settings**
#### **General**
- **`Rotate:`** – Rotates your view toward the aim direction before throwing.
- **`Pitch Up (deg):`** – Default upward aim when auto-pitch is disabled.
- **`Throw Delay (ms):`** – Delay after totem pop before throwing.
- **`Cooldown (ms):`** – Minimum time between throws.
- **`Silent Swap:`** – Enables silent hotbar swapping to pearls.
- **`Prefer Offhand:`** – Uses offhand pearls first if available.
- **`Jump on Throw:`** – Jumps before throwing to clear 2-block walls.
- **`Jump Wait (ms):`** – Delay after jumping before aiming/throwing.
- **`Reserve Totems:`** – Enable/disable skipping throws when low on totems.
- **`Reserve Count:`** – Minimum totem count before skipping throws.

#### **Aiming**
- **`Auto Pitch:`** – Automatically picks the flattest possible pitch for the chosen distance range.
- **`Allow 360 Aim:`** – Search all directions for the best yaw instead of only behind you.
- **`Avoid Enemy Cone:`** – Avoid throwing toward the nearest enemy within a cone.
- **`Enemy Cone Degrees:`** – Width of the avoidance cone in degrees.
- **`Min Escape Distance:`** – Minimum distance the pearl should land.
- **`Max Escape Distance:`** – Maximum allowed landing distance.

#### **Safety**
- **`Clear Check Distance:`** – Distance to check along aim direction for blocking walls.
- **`Try Side Offsets:`** – When blocked, try yaw offsets (±20°).
- **`Escalate Pitch:`** – If blocked, increase pitch to clear obstacles.
- **`Fallback Straight Up:`** – If all else fails, throw straight up.
- **`Near Path Check:`** – Checks first N blocks of pearl’s path for collisions.
- **`Initial Clearance:`** – Minimum clearance distance from eyes to avoid immediate wall collisions.

#### **Inventory**
- **`Pull from Inventory:`** – Pull pearls from main inventory if none in hotbar.
- **`Prefer Empty Hotbar:`** – Use empty hotbar slots when pulling from inventory.
- **`Temp Hotbar Slot:`** – Default hotbar slot for pulled pearls.

#### **Debug**
- **`Debug Logging:`** – Logs throw decisions and debug info.
- **`Manual Test Key:`** – Binds a key to manually trigger a throw.

---

### AutoWebFeetPlace
Automatically places a cobweb, ladder, or button in a broken surround slot under or beside the target.

- Detects when a target is already in a surround.
- Monitors surround blocks for **break events** and places instantly when a hole appears.
- Works with **cobwebs, ladders, or any button type**.
- Adjustable spam rate for repeated placement attempts.
- Includes **Silent Swap** and **Inventory Pull**.
- Can prioritize nearest hole or first available.
- Optional rotation to face placement.
- **New:** Pause while eating toggle for safety.

**Settings:**
- `Place Item`: Cobweb, Ladder, or Button.
- `Range`: Target detection distance.
- `Ignore Friends`: Skip friends.
- `Ignore Naked`: Skip players without armor.
- `Attempts Per Second`: Placement spam rate.
- `Rotate`: Rotate player toward placement.
- `Silent Swap`: Place from hotbar silently.
- `Only When Air`: Only place if hole is air/replaceable.
- `Prefer Closest Hole`: Prioritize nearest broken surround hole.
- `Grab From Inventory`: Pull from main inventory if missing in hotbar.
- `Hotbar Slot`: Slot for grabbed items.
- `Pause While Eating`: Pause placement when eating.

---

### AntiFeetPlace
Disrupts enemy **feetplace/surround** by mining the block **below** a surround face and placing an **Ender Chest** there, then (optionally) placing **obsidian** beside it to prep a crystal base. Includes bedrock handling, rate-limit safety, and a wait/retarget loop to keep the hole open.
- **Targeting:** Picks the **nearest enemy** within `range`, preferring the surround **facing you** (for ideal chest→obsidian→you alignment).
- **Below logic:**
    - If **ender chest already below**: enter WAIT mode (or yield/hold per settings).
    - If **bedrock below** and allowed: **hold-mine** it up to `bedrock-hold-max-ticks` (skips world bottom layer if enabled).
    - If **other block** below: **tap-mine** it for `tap-ticks-below` ticks.
    - If **air** below: tap the **surround** for `tap-ticks-surround` ticks so rebreak targets it, then **place chest**.
- **Chest placement:** Always places **directly under the chosen surround**; rotates first if enabled; renders intended placement if enabled.
- **Bridge for crystal (optional):** Tries up to four sides (biased toward you) to place **obsidian** beside the chest, with optional **clearance mining** and verifying **two air blocks above** for crystal placement.
- **Hold / Wait loop:** After placing, either **disable immediately**, **hold** for `post-place-pause` ticks, or **wait while chest present**, periodically retargeting the surround so your autominers keep that hole open.

**Safety & QoL**
- **Rate limiting:** Enforces `mine-rate-limit-ticks` between tap/mine packets to avoid speedmine resets/kicks.
- **Filters:** Ignore friends, armourless players (“nakeds”), unbreakables below (except allowed bedrock), and extra “hard” blocks you specify.
- **Inventory:** Finds **ender chest** / **obsidian** in **hotbar**, then falls back to **main inventory**.

**Settings (General)**
- `range` — Max target distance.
- `rotate` — Rotate during taps/placement/bedrock hold.
- `tap-ticks-below` — Ticks to tap the **below** block before moving on.
- `tap-ticks-surround` — Ticks to tap the **surround** before chest placement.
- `render-placement` — Let BlockUtils render intended placement.
- `disable-after-place` — Toggle off immediately after placing chest.
- `post-place-pause` — If not disabling, **hold** this many ticks so autominers can work.
- `wait-while-chest-present` — While a chest sits below, don’t re-mine the below block.
- `max-wait-chest-ticks` — 0 = wait indefinitely; otherwise leave WAIT after N ticks.
- `retarget-while-chest-present` — Keep tapping the surround to keep the hole open.
- `wait-retarget-interval` — Tick interval between those taps.

**Bridge / Crystal Prep**
- `place-obsidian-bridge` — Place **obsidian** beside the chest (same Y) on any open side.
- `bridge-mine-if-blocking` — Briefly mine to clear the bridge spot / clearance.
- `bridge-prep-max-ticks` — Max ticks to try clearing.
- `ensure-crystal-clearance` — Ensure **two air blocks above** the bridge for crystal placement.

**Bedrock Behavior**
- `allow-bedrock-break` — Hold-mine bedrock below surround (server permitting).
- `skip-bottom-bedrock-layer` — Skip world bottommost Y.
- `bedrock-hold-max-ticks` — Abort hold-mining if bedrock didn’t break in time.

**Packet / Kick Safety**
- `mine-rate-limit-ticks` — Minimum ticks between any tap/mine packets.

**Filters**
- `ignore-friends` — Skip friends.
- `ignore-nakeds` — Skip unarmoured players.
- `skip-unbreakable-below` — Skip below blocks with hardness < 0 (except allowed bedrock).
- `skip-hard-below` — Skip if below block is in the “hard” list.
- `extra-hard-list` — Defaults: Obsidian, Ender Chest, Ancient Debris, Respawn Anchor.

---

### BetterScaffold
Automatically places blocks under (or above) you for safe bridging/towering. Hotbar-aware with autoswitch, yaw/pitch rotation, optional air-place, level lock, and fading box renders. *(Credits: TrouserStreak addon)*

- **Placement modes:** `BelowFeet` (standard scaffold) or `AboveHead` (places a ceiling at a configurable offset).
- **Air Place:** When enabled, places at your projected position (player pos + velocity − 0.5Y).  
  When disabled, scans within a radius to find the nearest **non-air** block and places adjacent to it (respects `closest-block-range`).
- **Keep Y:** Locks placement to the Y level from when you enabled the module (with a reach limit).
- **Horizontal/Vertical Fill:** Expands placement in a configurable cross/square pattern around/above/below you.
- **Fast Tower:** Adds jump velocity while holding jump (and slight downward nudge post-place) for rapid upward stacking.
- **Block Filter:** Only places **full-cube** blocks from your hotbar, with **Whitelist/Blacklist** control. Prevents placing falling blocks over air.
- **Selection & Switch:** Searches **hotbar** for valid blocks; will **Auto Switch** if allowed.
- **Rotation & Swing:** Optionally rotates toward the place pos; optional **client-side swing** render.
- **Visuals:** Fading box render on each placed block with configurable line/side colors and shape mode.

**Settings**
- `blocks` — List of allowed/blocked blocks.
- `blocks-filter` — `Whitelist` | `Blacklist` (how to apply `blocks` list).
- `Placement Mode` — `BelowFeet` | `AboveHead` (where blocks go).
- `distance-above-head` — Offset above head when using `AboveHead`.
- `Keep Y` — Lock placement to the enable-time Y level.
- `Keep Y Reach` — Max distance from player to still place while `Keep Y` is on.
- `fast-tower` — Faster upward tower placing while holding jump.
- `swing` — Render client-side swing animation on place.
- `auto-switch` — Auto swap to a valid block before placing.
- `rotate` — Rotate toward the block being placed.
- `air-place` — Allow placing without a neighbor (uses projected position).
- `closest-block-range` — Max search distance for a solid neighbor (only visible when `air-place` is **off**).
- `on-surface` — Also place horizontal/vertical pads when you’re already standing on a block.
- `horizontal-radius` — Horizontal expansion radius (1–8).
- `vertical-radius` — Vertical expansion radius (1–8).

**Settings (Render)**
- `shape-mode` — `Lines` | `Sides` | `Both`.
- `side-color` — RGBA for box sides.
- `line-color` — RGBA for box lines.

---

## License
This project is licensed under the CC0-1.0 license.
