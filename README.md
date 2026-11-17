<div align="center">

# 🍽️ TestApp

**A reference Android application for students**

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Android](https://img.shields.io/badge/Android-API%2035-green?logo=android)](https://developer.android.com/)
[![Firebase](https://img.shields.io/badge/Firebase-Firestore%20%26%20Realtime%20DB-yellow?logo=firebase)](https://firebase.google.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

An educational example project demonstrating common Android development patterns — **Single Activity + MVVM** architecture, fragment navigation, authentication, CRUD operations, and a Cloud Firestore backend (with Realtime Database implementations included as an alternative).

---

</div>

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#️-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Architecture](#️-architecture)
- [Screen Flow](#️-screen-flow)
- [Adding a New Screen](#-adding-a-new-screen)
- [Switching the Database Backend](#-switching-the-database-backend)
- [License](#-license)

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🔐 Authentication | Register and log in; the session is kept in SharedPreferences |
| 👤 User Profile | View and edit your profile; admins can view and edit any user |
| 🛒 Carts | Build a cart from food items, view your carts and their details |
| 🍕 Food Management | Add food items with a name, price, and photo |
| 🛡️ Admin Panel | Browse all users, all food items, and all carts |
| 📷 Images | Camera / gallery picker; images stored as base64 (WEBP) |
| ☁️ Cloud Database | Cloud Firestore (Realtime Database implementations included) |

## 🛠️ Tech Stack

| Technology | Details |
|---|---|
| **Language** | Java 17 |
| **Compile / Min / Target SDK** | 35 (Android 15) |
| **Build System** | Gradle 9.8.2, Android Gradle Plugin 8.10.1 |
| **Architecture** | Single Activity, MVVM (AndroidX `ViewModel` + `LiveData`) |
| **UI** | AndroidX Fragment 1.9.1, RecyclerView 1.4.0, Material Components 1.12.0 |
| **Database** | Firebase BoM 33.9.0 — Cloud Firestore & Realtime Database |
| **Serialization** | Gson 2.11.0 (stores the logged-in user locally) |

## 📁 Project Structure

```
app/src/main/java/com/example/testapp/
│
├── MainActivity.java      # The only Activity — hosts every screen, implements NavigationHost
├── 📂 core/               # BaseFragment, BaseViewModel, NavigationHost
├── 📂 screens/            # One folder per screen: <Name>Fragment + <Name>ViewModel
├── 📂 models/             # Data entities: User, Food, Cart (+ Identifiable)
├── 📂 services/           # Data-access interfaces + DatabaseService
│   └── 📂 impl/
│       ├── 📂 firestore/  # Cloud Firestore implementations (default)
│       └── 📂 rtdb/       # Realtime Database implementations
├── 📂 adapters/           # RecyclerView / list adapters
└── 📂 utils/              # Validator, ImageUtil, ImagePicker, SharedPreferencesUtil
```

Each folder has its own `README.md` with details.

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (recent stable release)
- **JDK 17+** to run Gradle
- A Firebase project with **Cloud Firestore** enabled (and/or Realtime Database)
- An emulator or device running **Android 15 (API 35)** or newer

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/gilyerm/TestApp.git

# 2. Open the project in Android Studio

# 3. Put your google-services.json in the app/ directory

# 4. Sync Gradle, then run on an emulator/device (API 35+)
```

Build from the command line:

```bash
./gradlew assembleDebug        # build the debug APK
./gradlew testDebugUnitTest    # run unit tests
```

> [!NOTE]
> `google-services.json` is not in the repository. Create your own Firebase project and download the file from the [Firebase Console](https://console.firebase.google.com/).

## 🏗️ Architecture

The app uses a **Single Activity Architecture** with **MVVM**. `MainActivity` is the only Activity; every screen is a Fragment with its own ViewModel.

```
┌──────────────────────────────────────────────┐
│  MainActivity  (implements NavigationHost)   │  ← toolbar + fragment container
├──────────────────────────────────────────────┤
│  screens/<name>/<Name>Fragment               │  ← View: renders UI, observes LiveData
│  screens/<name>/<Name>ViewModel              │  ← state + logic, exposes LiveData
├──────────────────────────────────────────────┤
│  core/BaseViewModel                          │  ← gives ViewModels the IDatabaseService
├──────────────────────────────────────────────┤
│  services/  IDatabaseService → IUser/IFood/ICartService
│  services/impl/firestore  |  services/impl/rtdb
├──────────────────────────────────────────────┤
│  models/  User · Food · Cart                 │  ← plain Java objects (Identifiable)
└──────────────────────────────────────────────┘
```

| Layer | Responsibility |
|-------|---------------|
| **MainActivity** | Hosts fragments, owns the toolbar, implements `NavigationHost` (`navigateTo`, `goBack`, title, back button) |
| **core** | `BaseFragment` (gets the `NavigationHost`, sets title/back button), `BaseViewModel` (gives ViewModels `databaseService`) |
| **screens** | Fragment = View, ViewModel = state and calls to services |
| **services** | Async CRUD interfaces with callbacks; Firestore and Realtime DB implementations |
| **models** | Entities stored in the database |
| **adapters** | Display lists of carts, foods, and users |
| **utils** | Input validation, image conversion and picking, local session storage |

**Data flow:** Fragment calls a ViewModel method → ViewModel calls a service with a `DatabaseCallback` → callback updates `MutableLiveData` → Fragment's observer updates the UI.

## 🗺️ Screen Flow

```
Splash ──(logged in)──────────────────────────► Dashboard
   │                                              │
   └──(not logged in)──► Landing                  ├─► Add Food
                          ├─► Login ──────────────┤
                          └─► Register ───────────┤
                                                  ├─► Add Cart
                                                  ├─► My Carts ──► Cart Details
                                                  ├─► User Profile ──(sign out)──► Landing
                                                  └─► Admin (admins only)
                                                        ├─► Users List ──► User Profile
                                                        ├─► Food Items
                                                        └─► All Carts ──► Cart Details
```

## ➕ Adding a New Screen

1. Create `screens/<name>/` with `<Name>Fragment` and `<Name>ViewModel` (skip the ViewModel if the screen has no data or logic; then use `BaseFragment<BaseViewModel>` and skip steps 2 and 4 — see `landing/`).
2. Make the ViewModel extend `BaseViewModel` (no constructor needed; use the inherited `databaseService` field).
3. Make the Fragment extend `BaseFragment<<Name>ViewModel>`, implement `getTitle()`, and override `shouldShowBackButton()` if it is a root screen.
4. Assign the inherited `viewModel` field: `viewModel = getViewModel(<Name>ViewModel.class);`.
5. Add a layout `res/layout/fragment_<name>.xml`.
6. Open it from another screen with `navigationHost.navigateTo(new <Name>Fragment(), true)`.

No manifest change is needed — only `MainActivity` is declared.

## 🔀 Switching the Database Backend

The app uses Cloud Firestore. To use Realtime Database instead, change the three lines in the `DatabaseService` constructor:

```java
this.userService = new UserFirebaseServiceImpl();
this.foodService = new FoodFirebaseServiceImpl();
this.cartService = new CartFirebaseServiceImpl();
```

Both backends use the same paths/collections: `users`, `foods`, `carts`.

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**Made with ❤️ by [Gilyerm](https://github.com/gilyerm)**

</div>
