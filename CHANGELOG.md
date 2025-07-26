# Changelog

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
