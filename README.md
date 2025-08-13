# Xenon's Addon

A Meteor Client addon for Minecraft 1.21.5 that includes powerful PvP automation tools like **AutoConcrete**, **AntiConcrete**, **AntiConcreteDetection**, **AutoTNTplus**, **AutoMinePlus**, **AutoPearlTeleport**, **AutoPearlThrow**, and AutoWebFeetPlace.

---

##  Features
###  AutoConcrete
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

###  AntiConcrete
Automatically places a button beneath your feet to prevent concrete displacement.

- Detects when an enemy is nearby or when concrete is falling above you.
- Places any valid **button type** under you instantly.
- Smart mode places only when concrete is detected above.
- **Silent Swap** system moves button from inventory to hotbar and returns it after use.
- Rotates toward placement or target blocks when enabled.
- 
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
- `Break Mode` : `Tap to break` & `Hold to break` Methods used when breaking targeted button/torch(s) under enemies' feet
- `Rotate` : Rotate player toward block being targeted.

---

###  AutoTNTplus
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

---

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

##  License
This project is licensed under the CC0-1.0 license.
