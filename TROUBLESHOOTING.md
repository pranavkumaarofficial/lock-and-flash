# 🔧 TROUBLESHOOTING GUIDE - ScreenTracker

## 🏗️ Build & Setup Issues

### Issue: Gradle Sync Failed

**Symptoms:**
- Red error messages in "Build" panel
- "Sync failed" notification
- Dependencies not downloading

**Solutions:**

1. **Check Internet Connection**
   ```
   Gradle needs internet to download dependencies
   → Verify you're connected
   → Try disabling VPN if using one
   ```

2. **Clear Gradle Cache**
   ```
   File → Invalidate Caches → Invalidate and Restart
   → Wait for restart
   → Sync again
   ```

3. **Check Gradle Files**
   ```
   Ensure build.gradle.kts files match exactly
   → No extra spaces or characters
   → Proper formatting
   ```

4. **Update Gradle**
   ```
   Help → Check for Updates
   → Update Android Gradle Plugin if available
   ```

---

### Issue: "SDK not found" or "SDK location not found"

**Symptoms:**
- Error: "SDK location not found"
- Can't compile project

**Solutions:**

1. **Install Required SDK**
   ```
   Tools → SDK Manager
   → SDK Platforms tab
   → Check "Android 14.0 (API 34)"
   → Apply
   ```

2. **Set SDK Location**
   ```
   File → Project Structure → SDK Location
   → Android SDK location: [Browse to SDK]
   → Typically: C:\Users\[YourName]\AppData\Local\Android\Sdk
   ```

3. **Accept Licenses**
   ```
   Open Terminal in Android Studio
   → Run: ./gradlew --stop
   → Run: ./gradlew build --refresh-dependencies
   ```

---

### Issue: Import Errors (Red Underlines)

**Symptoms:**
- Red underlined imports
- "Unresolved reference" errors
- Missing classes

**Solutions:**

1. **Auto-Import**
   ```
   Click on red underline
   → Press Alt+Enter (Windows/Linux) or Option+Enter (Mac)
   → Select "Import"
   ```

2. **Sync Gradle**
   ```
   Click sync icon 🐘
   → Wait for completion
   → Errors should resolve
   ```

3. **Clean Build**
   ```
   Build → Clean Project
   → Wait for completion
   → Build → Rebuild Project
   ```

---

### Issue: "Duplicate class found" Error

**Symptoms:**
- Build fails with duplicate class errors
- Multiple dex files errors

**Solutions:**

1. **Check Dependencies**
   ```
   In build.gradle.kts (app level)
   → Look for duplicate dependencies
   → Remove duplicates
   ```

2. **Enable MultiDex (if needed)**
   ```kotlin
   // In build.gradle.kts defaultConfig block:
   multiDexEnabled = true
   
   // In dependencies:
   implementation("androidx.multidex:multidex:2.0.1")
   ```

---

### Issue: Wrong Package Name

**Symptoms:**
- "Package does not exist"
- Files in wrong location

**Solutions:**

1. **Verify Package Declaration**
   ```kotlin
   // Top of each .kt file should have:
   package com.screentracker.app
   ```

2. **Recreate Package Structure**
   ```
   Right-click java folder
   → New → Package
   → Enter: com.screentracker.app
   → Move files into it
   ```

---

## 📱 Installation Issues

### Issue: "App not installed" on Phone

**Symptoms:**
- Installation fails
- "App not installed" message
- Package conflicts

**Solutions:**

1. **Uninstall Old Version**
   ```
   Settings → Apps → ScreenTracker
   → Uninstall
   → Try installing again
   ```

2. **Enable Unknown Sources**
   ```
   Settings → Security
   → Unknown sources / Install unknown apps
   → Enable for your file manager
   ```

3. **Check Storage Space**
   ```
   Ensure phone has at least 50MB free
   → Clear cache if needed
   ```

4. **Use Different Install Method**
   ```
   Instead of APK:
   → Connect phone via USB
   → Use "Run" button in Android Studio
   ```

---

### Issue: USB Debugging Issues

**Symptoms:**
- Phone not detected
- "No devices found" in Android Studio
- Authorization prompts not appearing

**Solutions:**

1. **Enable Developer Mode**
   ```
   Settings → About Phone
   → Tap "Build Number" 7 times
   → "You are now a developer!"
   ```

2. **Enable USB Debugging**
   ```
   Settings → System → Developer Options
   → Enable "USB Debugging"
   → Enable "Install via USB"
   ```

3. **Accept Authorization**
   ```
   Disconnect and reconnect phone
   → On phone: "Allow USB debugging?" → Always allow → OK
   ```

4. **Try Different USB Cable/Port**
   ```
   Some cables are charge-only
   → Use data-transfer capable cable
   → Try different USB port on computer
   ```

5. **Restart ADB**
   ```
   In Android Studio Terminal:
   → adb kill-server
   → adb start-server
   → adb devices
   ```

6. **Update USB Drivers (Windows)**
   ```
   Device Manager → Android Device
   → Update driver
   → Or install manufacturer's USB drivers
   ```

---

### Issue: "Installation failed with message INSTALL_FAILED_UPDATE_INCOMPATIBLE"

**Symptoms:**
- Can't update app
- Signature mismatch

**Solutions:**

```
Complete uninstall required:
1. Settings → Apps → ScreenTracker → Uninstall
2. Reinstall fresh copy
```

---

## 🏃‍♂️ Runtime Issues

### Issue: App Crashes on Startup

**Symptoms:**
- App opens then closes immediately
- "ScreenTracker has stopped"

**Solutions:**

1. **Check Logcat**
   ```
   Android Studio → Logcat (bottom panel)
   → Filter: "ScreenTracker"
   → Look for red error messages
   → Find the "FATAL EXCEPTION" line
   ```

2. **Common Causes & Fixes:**

   **Database Issue:**
   ```
   Settings → Apps → ScreenTracker → Storage → Clear Data
   → Restart app
   ```

   **Permission Issue:**
   ```
   Settings → Apps → ScreenTracker → Permissions
   → Grant all requested permissions
   ```

   **Corrupted Installation:**
   ```
   Uninstall completely
   → Restart phone
   → Reinstall app
   ```

---

### Issue: Service Stops After Some Time

**Symptoms:**
- Monitoring stops randomly
- No new events recorded
- Notification disappears

**Solutions:**

1. **Disable Battery Optimization**
   ```
   Settings → Apps → ScreenTracker
   → Battery → Unrestricted
   OR
   Settings → Battery → Battery Optimization
   → All apps → ScreenTracker → Don't optimize
   ```

2. **Disable Adaptive Battery**
   ```
   Settings → Battery → Adaptive Battery
   → Turn OFF
   ```

3. **Lock App in Recent Apps**
   ```
   Recent apps screen
   → Find ScreenTracker
   → Tap app icon → Lock
   (prevents system from killing it)
   ```

4. **Enable Autostart (Xiaomi/Huawei/Oppo)**
   ```
   Settings → Apps → Autostart
   → Enable for ScreenTracker
   ```

5. **Check Background Restrictions**
   ```
   Settings → Apps → ScreenTracker
   → Mobile data & Wi-Fi → Allow background data usage
   → Battery → Background restriction → Remove
   ```

---

### Issue: Flashlight Events Not Detected

**Symptoms:**
- Screen events work
- Flashlight events don't appear
- No flashlight counts

**Solutions:**

1. **Grant Camera Permission**
   ```
   Settings → Apps → ScreenTracker → Permissions
   → Camera → Allow
   
   (Camera permission needed to monitor flashlight,
    but app doesn't actually use camera)
   ```

2. **Test Flashlight**
   ```
   Turn flashlight ON manually
   → Check if event appears
   → If not, restart monitoring service
   ```

3. **Check Camera API Compatibility**
   ```
   Some custom ROMs may not support TorchCallback
   → Try on different device to verify
   ```

---

### Issue: Screen Events Not Detected

**Symptoms:**
- Flashlight works
- Screen unlock events don't appear

**Solutions:**

1. **Restart Monitoring**
   ```
   Stop monitoring
   → Force stop app
   → Restart app
   → Start monitoring
   ```

2. **Check Broadcast Receivers**
   ```
   Verify AndroidManifest.xml has:
   <uses-permission android:name="android.permission.WAKE_LOCK" />
   ```

3. **Test Lock Screen**
   ```
   Lock phone
   → Unlock phone
   → Check if event appears
   → May take a few seconds to register
   ```

---

### Issue: High Battery Drain

**Symptoms:**
- Battery drains faster than expected
- ScreenTracker shows high battery usage

**Solutions:**

1. **Normal Usage:**
   ```
   Expected: 1-2% per day
   If higher: Check Logcat for repeated errors
   ```

2. **Check for Loops**
   ```
   Android Studio → Profiler
   → Check CPU usage
   → Should be near 0% when idle
   ```

3. **Clear Event Database**
   ```
   If database is huge (thousands of events):
   Settings → Apps → ScreenTracker → Storage → Clear Data
   ```

4. **Limit Event Logging**
   ```
   If you want, you can modify Database.kt
   to delete events older than X days
   ```

---

## 🎨 UI Issues

### Issue: App Screen is Blank/White

**Symptoms:**
- App opens but shows nothing
- White or black screen

**Solutions:**

1. **Wait a Moment**
   ```
   First launch may take 2-3 seconds
   → Give it time to load
   ```

2. **Check Theme**
   ```
   App uses dark theme
   → Ensure device brightness is up
   ```

3. **Reinstall**
   ```
   Complete uninstall → Reinstall
   ```

---

### Issue: Events List Not Updating

**Symptoms:**
- Stats show counts
- Event list is empty or not refreshing

**Solutions:**

1. **Switch Tabs**
   ```
   Tap different tabs
   → Triggers refresh
   ```

2. **Restart App**
   ```
   Force close app
   → Reopen
   → Events should load
   ```

3. **Database Issue**
   ```
   Check Logcat for database errors
   → May need to clear data and restart
   ```

---

## 🔐 Permission Issues

### Issue: Notification Permission Denied

**Symptoms:**
- Can't start monitoring
- No notification appears
- Service won't start

**Solutions:**

1. **Manual Grant (Android 13+)**
   ```
   Settings → Apps → ScreenTracker
   → Notifications → Allow notifications
   ```

2. **Request Again**
   ```
   Uninstall app
   → Reinstall
   → Allow when prompted
   ```

---

### Issue: Camera Permission Denied

**Symptoms:**
- Flashlight detection doesn't work
- Permission request doesn't appear

**Solutions:**

```
Settings → Apps → ScreenTracker → Permissions
→ Camera → Allow

Note: Camera permission is ONLY for flashlight monitoring
The app never actually accesses your camera
```

---

## 🐛 Development Issues

### Issue: Logcat Shows Errors

**How to Read Logcat:**

1. **Filter for Your App**
   ```
   In Logcat filter box, type: package:com.screentracker.app
   ```

2. **Look for Red Lines**
   ```
   Red = Error
   Orange = Warning
   Blue = Info
   Gray = Debug/Verbose
   ```

3. **Find Stack Trace**
   ```
   Error will show:
   → Exception type
   → Error message
   → Line numbers where error occurred
   ```

**Common Errors:**

| Error | Meaning | Fix |
|-------|---------|-----|
| NullPointerException | Trying to use null object | Check for null values |
| SecurityException | Missing permission | Add permission to manifest |
| SQLiteException | Database error | Clear app data |
| OutOfMemoryError | Not enough RAM | Reduce event logging |

---

### Issue: Build Takes Very Long

**Solutions:**

1. **Enable Gradle Daemon**
   ```kotlin
   // In gradle.properties:
   org.gradle.daemon=true
   org.gradle.parallel=true
   org.gradle.caching=true
   ```

2. **Increase Heap Size**
   ```kotlin
   // In gradle.properties:
   org.gradle.jvmargs=-Xmx4096m
   ```

3. **Disable Unnecessary Features**
   ```
   Close other programs
   → Disable antivirus scanning of project folder
   ```

---

## 📊 Performance Issues

### Issue: App is Slow/Laggy

**Solutions:**

1. **Enable Release Build**
   ```
   Build → Select Build Variant → release
   (Instead of debug)
   ```

2. **Check Event Count**
   ```
   Too many events (10,000+) can slow UI
   → Clear old events periodically
   ```

3. **Update Compose**
   ```
   Ensure latest Compose version in build.gradle.kts
   ```

---

## 🆘 Emergency Fixes

### Nuclear Option: Complete Reset

If nothing works:

```
1. Uninstall app completely
2. Clear Android Studio caches:
   File → Invalidate Caches → Invalidate and Restart
3. Delete .gradle and build folders in project
4. Restart computer
5. Open project in Android Studio
6. Sync Gradle
7. Clean & Rebuild
8. Run on phone
```

---

## 📞 Getting Help

### What to Include When Reporting Issues:

1. **Device Info**
   ```
   - Phone model
   - Android version
   - ROM (stock/custom)
   ```

2. **Error Details**
   ```
   - What you were doing
   - Expected vs actual behavior
   - Error messages from Logcat
   ```

3. **Steps to Reproduce**
   ```
   1. First I did...
   2. Then I clicked...
   3. Error appeared when...
   ```

4. **Screenshots**
   ```
   - Screenshot of error
   - Logcat excerpt (red error lines)
   ```

---

## ✅ Prevention Tips

**Avoid Issues Before They Happen:**

1. ✓ Always sync Gradle after changing build files
2. ✓ Grant all permissions when prompted
3. ✓ Don't kill the app from recent apps
4. ✓ Keep Android Studio updated
5. ✓ Use latest stable Android Studio version
6. ✓ Test on physical device, not just emulator
7. ✓ Read Logcat when errors occur
8. ✓ Keep phone charged during development
9. ✓ Use good quality USB cable
10. ✓ Backup your project regularly

---

**Still having issues? Check README.md for more details!**
