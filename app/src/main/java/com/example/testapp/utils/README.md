# 🛠️ utils

> Common helper functions used across the app.

| Class | Description |
|:------|:------------|
| `Validator` | Static input validation — email, password (≥6 chars), phone (≥10 digits), name (≥3 chars) |
| `ImageUtil` | Camera/storage permission requests, `ImageView` ↔ base64 conversion |
| `SharedPreferencesUtil` | Persists the logged-in `User` via Gson, handles login state checks and sign-out |
