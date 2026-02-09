# 🚀 QUICK START GUIDE - ScreenTracker

## ⚡ Fast Track (30 Minutes)

### Prerequisites Check ✓
- [ ] Android Studio installed
- [ ] Android phone with USB cable
- [ ] USB debugging enabled on phone

### Step-by-Step Setup

#### 1️⃣ Create Project (5 min)
```
Open Android Studio
→ New Project
→ Empty Activity (Compose)
→ Name: ScreenTracker
→ Package: com.screentracker.app
→ Minimum SDK: API 26
→ Language: Kotlin
→ Build language: Kotlin DSL
→ Finish
```

#### 2️⃣ Add Files (10 min)

**Navigate to:** `app/src/main/java/com/screentracker/app/`

**Create 3 Kotlin files:**

1. **MainActivity.kt**
   - Copy content from MainActivity.kt file
   - This contains the UI and main app logic

2. **MonitoringService.kt**
   - Copy content from MonitoringService.kt file
   - This is the background monitoring service

3. **Database.kt**
   - Copy content from Database.kt file
   - This handles data storage

**Update configuration files:**

4. **app/src/main/AndroidManifest.xml**
   - Replace entire content with AndroidManifest.xml

5. **app/build.gradle.kts**
   - Replace entire content with build.gradle.kts

6. **build.gradle.kts** (project level)
   - Replace with build.gradle.kts.root content

7. **settings.gradle.kts** (project level)
   - Replace with settings.gradle.kts content

8. **gradle.properties** (project level)
   - Replace with gradle.properties content

#### 3️⃣ Sync & Build (10 min)
```
Click: Sync Project with Gradle Files (🐘 icon)
→ Wait for dependencies to download
→ Fix any import errors (Alt+Enter)
→ Build → Build Bundle(s)/APK(s) → Build APK(s)
```

#### 4️⃣ Deploy (5 min)

**Method A - Direct Install:**
```
Connect phone via USB
→ Select device in dropdown
→ Click Run ▶️
→ App installs automatically
```

**Method B - Manual APK:**
```
After build completes
→ Click "locate" in notification
→ Find: app/build/outputs/apk/debug/app-debug.apk
→ Transfer to phone
→ Install APK
```

## 🎯 Usage

1. **Open app** → Tap "Start Monitoring"
2. **Grant permission** → Allow notifications
3. **Done!** → App now tracks screen & flashlight

Events appear in the list below with timestamps.

## 🔧 Common Issues & Fixes

### Build Issues
| Issue | Fix |
|-------|-----|
| Gradle sync fails | Check internet connection |
| SDK not found | Tools → SDK Manager → Install API 34 |
| Import errors | Alt+Enter on red lines |

### Runtime Issues
| Issue | Fix |
|-------|-----|
| App crashes | Check Logcat for errors |
| Service stops | Disable battery optimization |
| No flashlight events | Grant camera permission |
| Can't install | Enable "Install unknown apps" |

## 📁 Project Structure
```
ScreenTracker/
├── app/
│   ├── src/main/
│   │   ├── java/com/screentracker/app/
│   │   │   ├── MainActivity.kt          ← UI & app logic
│   │   │   ├── MonitoringService.kt     ← Background service
│   │   │   └── Database.kt              ← Data storage
│   │   └── AndroidManifest.xml          ← Permissions & config
│   └── build.gradle.kts                 ← App dependencies
├── build.gradle.kts                     ← Project config
├── settings.gradle.kts                  ← Module settings
└── gradle.properties                    ← Gradle settings
```

## ⚙️ Enable USB Debugging

**On your Android phone:**
```
Settings → About Phone → Tap "Build Number" 7 times
→ Back → System → Developer Options
→ Enable "USB Debugging"
→ Enable "Install via USB"
```

## 🎨 What You'll See

**Home Screen:**
- Pulsing status indicator (green when active)
- Start/Stop monitoring button
- Stats cards showing counts
- Event history list

**Features:**
- Purple cards = Screen events
- Yellow cards = Flashlight events
- Tabs to filter events
- Timestamps for each event

## 💡 Pro Tips

1. **Battery Optimization:** Disable for best results
   - Settings → Apps → ScreenTracker → Battery → Unrestricted

2. **Persistent Service:** Keep notification visible
   - Don't swipe away the notification

3. **Test It:** 
   - Lock/unlock phone → See screen events
   - Turn flashlight on/off → See flashlight events

## 📊 Performance
- Battery: ~1-2% per day
- RAM: 15-20 MB
- Storage: Grows with event logs

## 🆘 Need Help?

**Check Logcat for errors:**
```
Android Studio → Logcat (bottom panel)
→ Filter by "ScreenTracker"
→ Look for red error messages
```

**Verify permissions:**
```
Phone Settings → Apps → ScreenTracker → Permissions
→ Ensure all are granted
```

**Clean rebuild:**
```
Android Studio → Build → Clean Project
→ Build → Rebuild Project
```

---

## ✅ Checklist

- [ ] Android Studio installed
- [ ] Project created
- [ ] All 8 files copied
- [ ] Gradle synced successfully
- [ ] No red errors in code
- [ ] Phone connected
- [ ] USB debugging enabled
- [ ] App built successfully
- [ ] APK installed on phone
- [ ] App opened and permissions granted
- [ ] Monitoring started
- [ ] Test events appearing

**You're done! 🎉**

---

*For detailed documentation, see README.md*
