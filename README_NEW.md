# ScreenTracker 📱

[![Android](https://img.shields.io/badge/Platform-Android-green.svg?logo=android)](https://www.android.com/)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg?logo=android)](https://android-arsenal.com/api?level=26)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue.svg?logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.02.00-4285F4.svg?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Material Design 3](https://img.shields.io/badge/Material%20Design-3%20Expressive-6750A4.svg?logo=materialdesign)](https://m3.material.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/YOUR_USERNAME/ScreenTracker?style=social)](https://github.com/YOUR_USERNAME/ScreenTracker/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/YOUR_USERNAME/ScreenTracker?style=social)](https://github.com/YOUR_USERNAME/ScreenTracker/network/members)

A lightweight Android app that monitors and logs phone activity with a beautiful Material Design 3 Expressive UI.

> **🎨 Design Philosophy**: Built with Material 3 Expressive (2026) design language - featuring bold colors, spring animations, and the premium Google Pixel aesthetic.

## 📸 Screenshots

| Home Screen | Monitoring Active | Event List |
|------------|-------------------|------------|
| ![Home](screenshots/home.png) | ![Active](screenshots/active.png) | ![Events](screenshots/events.png) |

*Note: Add screenshots after building the app*

---

## ✨ Features

### 🔍 Tracking Capabilities
- **Screen Events**: Monitors every time your screen turns on
- **Unlock Detection**: Tracks when you unlock your device
- **Flashlight Usage**: Logs flashlight on/off events
- **Real-time Updates**: Events appear instantly in the UI
- **Persistent Storage**: All events saved to local database

### 🎨 Modern UI/UX
- **Material Design 3 Expressive** (2026 Pixel style)
- **Purple-forward** color scheme with dynamic theming
- **Spring-based animations** for smooth, organic motion
- **Tonal elevation** system (depth without shadows)
- **Dark theme** optimized for OLED displays
- **28dp corner radius** on cards (premium feel)
- **Bold visual hierarchy** with size variations

### 📊 Analytics
- Real-time statistics cards
- Event count by type
- Chronological event timeline
- Filter by event type (All/Screen/Flashlight)

### 🛠️ Technical Features
- **Foreground Service** for reliable background monitoring
- **Room Database** for efficient data persistence
- **Kotlin Coroutines** for async operations
- **Jetpack Compose** for modern reactive UI
- **MVVM Architecture** pattern
- **Material 3 Components** throughout

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Hedgehog (2023.1.1) or newer
- **JDK**: 17 or higher
- **Android SDK**: API 26+ (Android 8.0 Oreo)
- **Physical Device**: Recommended for testing (emulators may not trigger all events)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/ScreenTracker.git
   cd ScreenTracker
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open" → Navigate to project folder
   - Wait for Gradle sync to complete

3. **Generate Launcher Icons**
   - Right-click `app/src/main/res`
   - New → Image Asset
   - Configure and generate icons

4. **Build the project**
   ```bash
   ./gradlew build
   ```

5. **Run on device**
   - Connect your Android phone via USB
   - Enable Developer Options & USB Debugging
   - Click the green Play button (▶) in Android Studio

---

## 🎯 Usage

### First Launch
1. **Grant Permissions**: Allow notifications and camera access
2. **Start Monitoring**: Tap the "Start Monitoring" button
3. **Background Service**: A persistent notification will appear
4. **Test Events**: Lock/unlock your phone or toggle flashlight

### Managing Data
- **View Stats**: See total screen unlocks and flashlight uses
- **Filter Events**: Use tabs to filter by event type
- **Clear Data**: Tap the delete icon (🗑️) in the top bar

### Battery Optimization
For best results, disable battery optimization:
- Settings → Apps → ScreenTracker → Battery → Unrestricted

---

## 🏗️ Architecture

```
app/
├── src/main/
│   ├── java/com/screentracker/app/
│   │   ├── MainActivity.kt          # UI layer (Compose)
│   │   ├── MonitoringService.kt     # Background service
│   │   └── Database.kt              # Room database
│   ├── AndroidManifest.xml          # App configuration
│   └── res/                         # Resources
├── build.gradle.kts                 # App dependencies
└── proguard-rules.pro               # Release optimization
```

### Tech Stack
| Component | Technology |
|-----------|-----------|
| **Language** | Kotlin 1.9.20 |
| **UI Framework** | Jetpack Compose |
| **Design System** | Material Design 3 |
| **Database** | Room 2.6.1 |
| **Async** | Kotlin Coroutines |
| **Background** | Foreground Service |
| **Min SDK** | 26 (Android 8.0) |
| **Target SDK** | 34 (Android 14) |

---

## 🎨 Design System

### Material 3 Expressive (2026)

This app showcases the latest Material Design 3 Expressive guidelines:

#### Color Palette
- **Primary**: `#D0BCFF` (Pixel Purple)
- **Secondary**: `#CCC2DC` (Soft Purple)
- **Tertiary**: `#EFB8C8` (Pink)
- **Background**: `#1C1B1F` (Dark)

#### Shape Tokens
- **Large**: 28dp (main cards)
- **Medium**: 24dp (stats cards)
- **Small**: 20dp (list items)
- **Full**: 999dp (status indicator)

#### Animation
- **Spring-based** motion (not linear)
- **Damping Ratio**: Medium Bouncy
- **Stiffness**: Low (organic feel)

---

## 📦 Dependencies

```kotlin
// Core Android
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
implementation("androidx.activity:activity-compose:1.8.2")

// Compose
implementation(platform("androidx.compose:compose-bom:2024.02.00"))
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

---

## 🔒 Permissions

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_DATA_SYNC" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

**Privacy Note**: The camera permission is only used to monitor flashlight state. The app never accesses the camera itself.

---

## 🐛 Known Issues

- **Samsung/Xiaomi/Oppo**: May require manual "autostart" permission
- **Android 12+**: Battery optimization may kill service - disable for best results
- **Event Timing**: Small delay (100-500ms) between event and logging is normal

---

## 🛣️ Roadmap

- [ ] Data export (CSV/JSON)
- [ ] Usage charts & visualizations
- [ ] Weekly/monthly statistics
- [ ] Home screen widget
- [ ] Settings screen (theme customization)
- [ ] Notification customization
- [ ] Swipe to delete events
- [ ] Event search functionality

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@YOUR_USERNAME](https://github.com/YOUR_USERNAME)
- Email: your.email@example.com

---

## 🙏 Acknowledgments

- [Material Design 3](https://m3.material.io/) - Design guidelines
- [Android Developers](https://developer.android.com/) - Documentation
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - UI framework
- [Room Database](https://developer.android.com/training/data-storage/room) - Persistence library

---

## 📊 Project Stats

![GitHub repo size](https://img.shields.io/github/repo-size/YOUR_USERNAME/ScreenTracker)
![GitHub code size](https://img.shields.io/github/languages/code-size/YOUR_USERNAME/ScreenTracker)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/YOUR_USERNAME/ScreenTracker)
![GitHub last commit](https://img.shields.io/github/last-commit/YOUR_USERNAME/ScreenTracker)

---

## 💡 Support

If you find this project helpful, please consider giving it a ⭐!

Found a bug? [Open an issue](https://github.com/YOUR_USERNAME/ScreenTracker/issues)

---

<p align="center">
  Made with ❤️ using Kotlin & Jetpack Compose
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Made%20with-Kotlin-0095D5?logo=kotlin&logoColor=white" alt="Made with Kotlin">
  <img src="https://img.shields.io/badge/Built%20with-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white" alt="Built with Jetpack Compose">
  <img src="https://img.shields.io/badge/Designed%20with-Material%203-6750A4?logo=materialdesign&logoColor=white" alt="Designed with Material 3">
</p>
