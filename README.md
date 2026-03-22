# ZaveProject

ZaveProject is an Android application built with **Jetpack Compose**, focusing on location-based store discovery and seamless user authentication.

## 🚀 Features

- **Google Authentication**: Integrated with Google One Tap and Firebase Auth for a smooth sign-in experience.
- **Store Search**: Search for nearby stores, restaurants, or categories using the **Google Places API**.
- **Interactive Results**: View store details, distance, and location. Open directly in **Google Maps** for navigation.
- **Dynamic Content**: Featured banners controlled remotely via **Firebase Remote Config**.
- **Offline First**: Search results are cached locally using **Room Database** for offline access.
- **Search History**: Keeps track of your last 3 searches using **Preferences**.
- **Modern UI**: Fully built with Jetpack Compose.

## 🛠 Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Networking**: Retrofit & Gson
- **Local Database**: Room
- **Image Loading**: Coil
- **Authentication**: Firebase Auth
- **Remote Configuration**: Firebase Remote Config
- **Maps & Location**: Google Maps 

## 🏗 Project Structure

The project follows a clean architecture pattern:

```text
com.assignment.zaveproject
├── data
│   ├── remote     # API interfaces (Places API) and Firebase logic
│   ├── local      # Room Database, DAOs, and SharedPreferences
│   └── repository # Data orchestration between local and remote
├── domain         # Core business models
├── ui
│   ├── auth       # Login screens and Auth ViewModel
│   ├── home       # Main screen, search bar, and store list
│   └── common     # Splash Screens
└── helper         # Location and Utility helpers
```

## ⚙️ Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/adword01/ZaveProject.git
   ```

2. **Google Maps API Key**:
   - Obtain an API key from the [Google Cloud Console](https://console.cloud.google.com/).
   - Add the key to your `app/build.gradle.kts` (or a `secrets.properties` file if configured):
     ```kotlin
     buildConfigField("String", "MAPS_API_KEY", "\"YOUR_API_KEY_HERE\"")
   - Also add in 'strings.xml' file.
      ```

3. **Firebase Setup**:
   - Create a project in the [Firebase Console](https://console.firebase.google.com/).
   - Add your Android app and download the `google-services.json` file.
   - Place `google-services.json` in the `app/` directory.

4. **Build and Run**:
   - Open the project in **Android Studio (Ladybug or newer)**.
   - Sync Gradle and run the app on an emulator or physical device.

## 📸 Screenshots
   
   Present in Screenshots Folder


## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
