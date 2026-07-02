# Moonlight Skylark (Full Keyboard Edition)

This is a modified fork of the original [Moonlight Android](https://github.com/moonlight-stream/moonlight-android) repository.

It is specifically patched to support the **full capability of external/bluetooth keyboards** (including system-level shortcuts such as `Alt+Tab`, `Windows/Command` key, `Ctrl+Shift+Esc`, `Alt+F4`, `Win+D`, etc.) without being intercepted or blocked by the Android operating system.

*Edited by AI Gemini 3.5 Flash / Antigravity.*

---

## What was modified from the original:
* **Guided Accessibility Service**: Integrated a new `KeyInterceptorService` (Accessibility Service) that grabs system-level hardware keyboard shortcuts and forwards them directly to the active Moonlight game stream before the Android system can intercept them.
* **Momentary Release & Rapid Spam Fix**: Fixed a focus-loss bug where spamming `Tab` (while holding `Alt`) would cause the switcher on the PC to close early. Ignored synthetic system-dispatched key releases and preserved modifier keys state during focus shifts.
* **Memory & Performance Optimizations**: Cached preference checks in memory to ensure key interception runs with sub-millisecond latency (preventing Android accessibility service timeouts).
* **Settings Toggle UI**: Added a toggle switch **"Intercept system key shortcuts"** in the Moonlight settings menu under **Input settings** that guides the user to the Accessibility settings (with helpful bypass instructions for Xiaomi/Poco/Redmi "Restricted settings" sideload policy).
* **Application Renaming**: Renamed the application name to **Moonlight Skylark**.

---

## How to Install / Build

### Method 1: Downloading from the Releases tab
* Go to the [Releases](https://github.com/KaleidSkylark/moonlight-android/releases) tab in this repository.
* Download the compiled `app-nonRoot-debug.apk` or any other packaged release.
* Install it on your Android device.
* *Note:* If you receive a *"Restricted setting: For your security, this setting is currently unavailable"* prompt when enabling the Accessibility Service:
  1. Go to your phone's *Settings -> Apps -> Manage apps -> Moonlight Skylark*.
  2. Tap the three dots (⋮) in the top-right corner.
  3. Select **Allow restricted settings**, then return to Accessibility settings to turn it on.

### Method 2: Sideloading via Forking & GitHub Actions (Android CLI Workflow)
* Fork this repository to your own GitHub account.
* The GitHub Action will compile and build the APK using the Android CLI workflow automatically when a push occurs, or you can trigger it manually, allowing you to download the artifact directly from your workflow runs.

---

## Authors

### Original Authors
* [Cameron Gutman](https://github.com/cgutman)  
* [Diego Waxemberg](https://github.com/dwaxemberg)  
* [Aaron Neyer](https://github.com/Aaronneyer)  
* [Andrew Hennessy](https://github.com/yetanothername)

### Modified Authors
* [KaleidSkylark](https://github.com/KaleidSkylark)
* Modified and patched by AI Gemini 3.5 Flash / Antigravity

---

Moonlight is the work of students at [Case Western](http://case.edu) and was started as a project at [MHacks](http://mhacks.org).
