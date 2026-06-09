# First — FinTech Subscription App
**Case Study Submission – Front End Engineer – Joses Murungi**

---

## Overview
First is a mobile application for a FinTech company offering subscription
services to its customers. The app allows customers to register an account,
log in securely, browse available subscription services, subscribe to plans,
and view their active subscriptions.

---

## Tech Stack
| Layer              | Technology                             |
|--------------------|----------------------------------------|
| Language           | Kotlin                                 |
| UI Framework       | Jetpack Compose (Material 3)           |
| Architecture       | MVVM + Repository Pattern              |
| Networking         | Retrofit 2 + OkHttp                    |
| Token Storage      | DataStore (EncryptedSharedPreferences) |
| Async              | Kotlin Coroutines + StateFlow          |
| Navigation         | Navigation Compose                     |
| Min SDK            | API 24 (Android 7.0)                   |
| Target SDK         | API 35 (Android 14)                    |

---

## 🏗️ Architecture & Project Structure

The project strictly adheres to a clean, modular architecture with a clear separation of concerns divided into three main layers: **Core**, **Data**, and **UI**.

```text
com.first.app
├── core/
│   ├── Constants.kt              # API endpoints and storage keys
│   ├── network/
│   │   ├── ApiService.kt         # Retrofit interface definitions
│   │   ├── AuthInterceptor.kt    # Attaches dynamic Bearer token headers
│   │   └── RetrofitClient.kt     # Configures OkHttp client & Retrofit instance
│   └── storage/
│       └── TokenDataStore.kt     # Secure, async authentication token persistence
├── data/
│   ├── models/
│   │   └── Models.kt             # Request/Response DTO data classes
│   └── repository/
│       ├── AuthRepository.kt     # Boundary for register, login, and logout logic
│       ├── ServiceRepository.kt  # Boundary for data sourcing, sub management
│       └── ErrorUtils.kt         # Centralized API network exception mapping
└── ui/
    ├── navigation/
    │   └── Navigation.kt         # Type-safe NavHost routes and graph definitions
    ├── screens/
    │   ├── SplashScreen.kt       # Dynamic auth-state gateway check on launch
    │   ├── auth/
    │   │   ├── LoginScreen.kt    # Input fields with localized error-state feedback
    │   │   └── RegisterScreen.kt # Multi-field form validation wrapper
    │   └── services/
    │       ├── ServicesScreen.kt # Catalog of available products with state handling
    │       └── SubscriptionsScreen.kt # Active subscription status rendering
    ├── theme/
    │   └── Theme.kt              # Customized Material 3 design tokens & colors
    ├── viewmodel/
    │   ├── AuthViewModel.kt      # State management proxy for identity workflows
    │   └── ServicesViewModel.kt  # State management proxy for digital assets data
    └── widgets/
        └── Widgets.kt            # Reusable Atomic UI blocks (Cards, Skeletons, States)

---

## Features Implemented
- [x] Customer registration (full name, email, phone, password)
- [x] Secure login with JWT token persistence
- [x] Token stored in DataStore with EncryptedSharedPreferences
- [x] Auth header automatically attached to all secured requests
- [x] Browse service catalogue
- [x] Discounted services visually distinguished — badge, strikethrough
      original price, orange colour, dedicated Special Offers section
- [x] Subscribe to a service
- [x] View active subscriptions (Bonus API)
- [x] Loading states on all async operations
- [x] Error states with retry on all screens
- [x] Empty states with contextual messaging
- [x] Form validation — email format, password length, phone number,
      confirm password match
- [x] Persistent login — token checked on splash, skips login if valid
- [x] Logout clears all stored credentials

---

## Security
- Token stored via DataStore with `encryptedSharedPreferences = true`
  backed by Android Keystore — never stored in plaintext
- Auth header attached at the OkHttp interceptor layer — no manual
  header management required per API call
- HTTPS enforced for all network communication
- No sensitive data stored beyond the authentication token
- Input validation on all form fields before any API call is made

---

## How to Build and Run

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Android device or emulator running API 24 or higher
- Active internet connection (API hosted at mawingu.cbaloop.com)

### Run from Android Studio
1. Open Android Studio
2. Select **File → Open** and choose the project root folder
3. Wait for Gradle sync to complete
4. Connect a device or start an emulator
5. Press the green **Run** button or `Shift + F10`

### Build release APK
```bash
./gradlew assembleRelease
```
Output will be at:
app/release/app-release.apk

### Verify environment
```bash
./gradlew tasks   # Lists all available Gradle tasks
```

---

## API Reference
**Base URL:** `https://mawingu.cbaloop.com/cba`

| Endpoint | Method | Auth |
|----------|--------|------|
| `/api/v1/user/register` | POST | None |
| `/api/v1/access/login` | POST | None |
| `/api/v1/service/services` | GET | Required |
| `/api/v1/subscription/subscribe` | POST | Required |
| `/api/v1/subscription/subscriptions/{email}` | GET | Required |

---

## Test Credentials
You may register a new account or use the following to test:
- **Email:** *(register a fresh account via the app)*
- **Password:** *(minimum 8 characters as required by the API)*

---

## Known API Behaviour
- The subscriptions endpoint returns HTTP 400 with valid data in the
  response body. The app handles this gracefully by reading the
  response body regardless of status code.
- The login endpoint returns a JWT token nested inside a `data` object
  rather than at the root level. This is handled in the login response
  parsing.
