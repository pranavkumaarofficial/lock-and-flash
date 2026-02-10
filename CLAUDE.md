# ScreenTracker - Android Phone Activity Monitor

## Project Overview
A lightweight Android app that monitors and logs:
1. Every time the screen turns on (lock screen appears)
2. Every time the device is unlocked
3. Every time the flashlight turns on/off

Built with Kotlin, Jetpack Compose, Room Database, Material Design 3.

## Tech Stack
- **Language**: Kotlin 1.9.20
- **UI Framework**: Jetpack Compose with Material Design 3
- **Database**: Room 2.6.1 (SQLite wrapper)
- **Architecture**: MVVM pattern
- **Background Processing**: Foreground Service with BroadcastReceivers
- **Min SDK**: 26 (Android 8.0 Oreo)
- **Target SDK**: 34 (Android 14)

## Project Structure
```
app/src/main/
├── java/com/screentracker/app/
│   ├── MainActivity.kt          # Main UI + Compose screens
│   ├── MonitoringService.kt     # Background service (event detection)
│   └── Database.kt              # Room database (entities, DAOs)
├── AndroidManifest.xml          # Permissions & component declarations
└── res/                         # Resources (auto-generated)

app/
└── build.gradle.kts             # App-level dependencies

Root/
├── build.gradle.kts             # Project-level config
├── settings.gradle.kts          # Module settings
└── gradle.properties            # Gradle properties
```

## Core Components

### 1. MainActivity.kt
- **Purpose**: User interface, start/stop monitoring service
- **Key Elements**:
  - Compose UI with Material 3 theme
  - Service control buttons
  - Real-time event list display
  - Statistics cards (screen unlocks, flashlight uses)
  - Permission handling (notifications, camera)

### 2. MonitoringService.kt
- **Purpose**: Background service that detects events
- **Detection Methods**:
  - Screen events: `BroadcastReceiver` listening for `ACTION_SCREEN_ON`, `ACTION_USER_PRESENT`
  - Flashlight: `CameraManager.TorchCallback` for torch state changes
- **Lifecycle**: Runs as foreground service with persistent notification
- **Data Flow**: Detected events → Insert into Room database

### 3. Database.kt
- **Components**:
  - `Event` entity (id, type, timestamp)
  - `EventType` enum (SCREEN_ON, USER_PRESENT, FLASHLIGHT_ON, FLASHLIGHT_OFF)
  - `EventDao` interface (insert, query, delete operations)
  - `EventDatabase` singleton

## Required Permissions (AndroidManifest.xml)
```xml




```

## Critical Android Development Rules

### Must Follow:
1. **ALWAYS sync Gradle** after modifying build.gradle.kts files
2. **Never skip permission requests** - Android 13+ requires runtime permissions
3. **Use foreground service** for long-running background work
4. **Test on physical device** - Emulators don't reliably trigger all events
5. **Check Logcat** for errors during debugging
6. **Handle service lifecycle** properly (onCreate, onStartCommand, onDestroy)
7. **Respect battery optimization** - Use efficient event listeners, not polling

### Never Do:
1. Don't modify package structure without updating all imports
2. Don't ignore deprecation warnings
3. Don't block the main thread with database operations (use coroutines)
4. Don't forget to unregister receivers in onDestroy
5. Don't hardcode strings in UI (use string resources for production)

## Development Workflow
```
1. Make code changes
2. Sync Gradle (if dependencies changed)
3. Build → Make Project
4. Run → Select Device → Run
5. Monitor Logcat for errors
6. Test functionality on device
7. Verify no memory leaks
```

## Testing Checklist
- [ ] Service starts when "Start Monitoring" clicked
- [ ] Notification appears when service is running
- [ ] Screen lock/unlock triggers events in list
- [ ] Flashlight on/off triggers events in list
- [ ] Events persist after app restart
- [ ] Service survives app kill (via recent apps)
- [ ] No crashes in Logcat
- [ ] Battery usage stays under 2% per day
- [ ] UI updates in real-time when events occur

## Known Issues & Gotchas
- **Camera permission**: Required for flashlight monitoring but app never accesses camera
- **Service killed on some devices**: Xiaomi/Huawei/Oppo require manual autostart permission
- **Android 12+**: Background restrictions may kill service - requires battery optimization exemption
- **Event timing**: Small delay (100-500ms) between actual event and database write is normal

## Build Variants
- **Debug**: Development build with verbose logging
- **Release**: Optimized build (requires signing key for distribution)

## Performance Targets
- App size: <8 MB (debug), <5 MB (release)
- RAM usage: <25 MB while running
- Battery drain: <2% per 24 hours
- Database size: ~1 KB per 100 events
- UI responsiveness: 60 FPS, no jank

## Error Handling
- Service crash → Auto-restart via START_STICKY
- Database error → Log and continue (don't crash app)
- Permission denied → Show user-friendly message
- Out of memory → Implement event auto-deletion (keep last 10k events)

## Code Style
- Follow Kotlin coding conventions
- Use meaningful variable names
- Add comments for complex logic only
- Keep functions under 50 lines when possible
- Use Compose best practices (remember, derivedStateOf, etc.)

## Current Status
✅ Basic functionality implemented
✅ UI displays events in real-time
✅ Background service working
✅ Database persistence working
⏸️ Ready for enhancements

## Next Steps (Future, Not Now)
- Data export functionality
- Charts/visualizations
- Settings screen
- Custom notification options
- Widget support




