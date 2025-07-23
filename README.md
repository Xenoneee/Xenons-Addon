# Xenon's Addon

A Meteor Client addon for Minecraft 1.21.1 that includes powerful PvP automation tools like **AutoConcrete**, **AntiConcrete**, and **AutoTNTplus** 

---

##  Features

###  AutoConcrete
Automatically builds a vertical obsidian pillar beside a nearby target and drops concrete powder on their head.

- Builds a **2–3 block obsidian pillar** on one side of a target's surround.
- Drops **concrete powder** or concrete blocks.
- **Air Place** mode allows dropping without support blocks.
- Detects **End Crystals** above the target to adapt obsidian pillar for better placement.
- Reuses existing obsidian pillars if detected by target.
- Smart resets when the target moves.

**Settings:**
- `Range`: Target detection distance.
- `Pillar Delay`: Delay between obsidian block placements.
- `Concrete Delay`: Delay after dropping concrete.
- `Rotate`: Rotate player toward block placements.
- `Detect Crystals`: Raises pillar height if End Crystal detected.
- `Air Place`: Enable dropping concrete in air.
- `Place Support`: Toggle obsidian pillar placement when Air Place is disabled.

---

###  AntiConcrete
Automatically places a button beneath your feet to prevent concrete displacement.

- Detects when an enemy is nearby or when concrete is falling above you.
- Places any valid **button type** under you instantly.
- Smart mode places only when concrete is detected above.
- **Silent Swap** system moves button from inventory to hotbar and returns it after use.

**Settings:**
- `Mode`: `Strict` (always place at feet when target is in range) or `Smart` (only if concrete above).
- `Smart Range`: Enemy proximity trigger.
- `Silent Inventory Swap`: Enable silent hotbar swapping.
- `Hotbar Slot`: Which hotbar slot to use for button swapping.
- `Return Delay`: How long to wait before returning the button to inventory from hotbar.

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

##  License
This project is licensed under the CC0-1.0 license.