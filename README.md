<div align="center">

# 🍽️ TestApp

**A reference Android application for students**

[![Java](https://img.shields.io/badge/Java-11-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Android](https://img.shields.io/badge/Android-API%2035-green?logo=android)](https://developer.android.com/)
[![Firebase](https://img.shields.io/badge/Firebase-Realtime%20DB-yellow?logo=firebase)](https://firebase.google.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

An educational example project demonstrating common Android development patterns — authentication, CRUD operations, Firebase integration, and clean layered architecture.

---

</div>

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Architecture](#-architecture)
- [License](#-license)

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🔐 Authentication | User registration & login |
| 👤 User Profile | View and edit profile with image support |
| 🛒 Shopping Cart | Create, view, and manage carts |
| 🍕 Food Management | Add and browse food items |
| 🛡️ Admin Panel | User management for administrators |
| 📷 Image Support | Camera and gallery integration |
| ☁️ Cloud Database | Firebase Realtime Database |
| 💾 Local Storage | SharedPreferences persistence |

## 🛠️ Tech Stack

| Technology | Details |
|---|---|
| **Language** | Java 11 |
| **Min SDK** | 35 (Android 15) |
| **Build System** | Gradle 8.11.1 |
| **UI** | Material Design 1.12 |
| **Database** | Firebase Realtime Database |
| **Serialization** | Gson 2.11 |

## 📁 Project Structure

```
app/src/main/java/com/example/testapp/
│
├── 📂 adapters/        # RecyclerView adapters
├── 📂 models/          # Data models (User, Food, Cart)
├── 📂 screens/         # Activities (UI layer)
├── 📂 services/        # Business logic & Firebase integration
│   └── 📂 Impl/       # Service implementations
└── 📂 utils/           # Validator, ImageUtil, SharedPreferences
```

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable)
- JDK 11+
- A Firebase project with Realtime Database enabled

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/gilyerm/TestApp.git

# 2. Open in Android Studio

# 3. Add your google-services.json to the app/ directory

# 4. Sync Gradle & Run on emulator/device (API 35+)
```

> [!NOTE]
> You must create your own Firebase project and download the `google-services.json` file from the [Firebase Console](https://console.firebase.google.com/).

## 🏗️ Architecture

The app follows a **layered architecture** pattern:

```
┌─────────────────────────────────────┐
│            Screens (UI)             │  ← Activities & user interaction
├─────────────────────────────────────┤
│         Services (Logic)            │  ← Interfaces + Implementations
├─────────────────────────────────────┤
│          Models (Data)              │  ← POJOs with Identifiable
├─────────────────────────────────────┤
│       Utils (Helpers)               │  ← Validation, images, storage
└─────────────────────────────────────┘
```

| Layer | Responsibility |
|-------|---------------|
| **Screens** | Activities handling UI and user interaction |
| **Services** | `IUserService`, `ICartService`, `IFoodService` with concrete implementations |
| **Models** | POJOs implementing `Identifiable` for generic CRUD |
| **Adapters** | RecyclerView adapters for list displays |
| **Utils** | Validation, image handling, and local storage helpers |

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**Made with ❤️ by [Gilyerm](https://github.com/gilyerm)**

</div>
