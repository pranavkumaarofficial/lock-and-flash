# lock-and-flash

[![Android](https://img.shields.io/badge/Platform-Android-green.svg?logo=android)](https://www.android.com/)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg)](https://android-arsenal.com/api?level=26)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue.svg?logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.02.00-4285F4.svg?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Material Design 3](https://img.shields.io/badge/Material%20Design-3-6750A4.svg?logo=materialdesign)](https://m3.material.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

An Android app that tracks screen unlocks and flashlight usage. Built with Material Design 3.

## What it does

The app runs a background service that logs:
- Screen on events
- Device unlocks
- Flashlight on/off

Events are stored locally and displayed in a timeline.

## Tech stack

- **Kotlin** 1.9.20
- **Jetpack Compose** for UI
- **Room** for database
- **Material Design 3** with dark theme
- **Coroutines** for async work

Min SDK: 26 (Android 8.0)
Target SDK: 34 (Android 14)

## Building

```bash
git clone https://github.com/pranavkumaarofficial/lock-and-flash.git
cd lock-and-flash
./gradlew build
```

Open in Android Studio, sync Gradle, and run on a physical device (emulators don't trigger all events reliably).

## Permissions

- `CAMERA` - for flashlight monitoring (doesn't access camera)
- `FOREGROUND_SERVICE` - keeps tracking service running
- `POST_NOTIFICATIONS` - shows service notification
- `WAKE_LOCK` - detects screen events

## UI design

Uses Material 3 Expressive with:
- 28dp corner radius on main cards
- Purple-forward color scheme
- Tonal surface elevation (no shadows)
- Dark theme optimized for OLED

Stats cards show total counts. Event list has filtering tabs.

## Known issues

- Samsung/Xiaomi/Oppo devices may need manual "autostart" permission
- Battery optimization can kill the service - disable it in app settings
- Small delay (100-500ms) between event and database write is normal

## Roadmap

- [ ] Data export (CSV/JSON)
- [ ] Usage charts
- [ ] Weekly stats
- [ ] Home screen widget
- [ ] Custom notification options

## License

MIT License - see [LICENSE](LICENSE)
