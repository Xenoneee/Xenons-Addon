# Changelog

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

