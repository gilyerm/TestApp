# ⚡ services/impl/rtdb

> Firebase Realtime Database implementations of the service interfaces.

| Class | Implements | Firebase Path / Table | Description |
|:------|:-----------|:----------------------|:------------|
| `FirebaseService<T>` | `ICrudService<T>` | *(generic)* | Base class handling all CRUD via Firebase Realtime Database (read, write, delete, transaction-based update). |
| `UserFirebaseServiceImpl` | `IUserService` | `users` | Adds login lookup and email existence check via server-side queries. |
| `FoodFirebaseServiceImpl` | `IFoodService` | `foods` | Standard CRUD operations for foods. |
| `CartFirebaseServiceImpl` | `ICartService` | `carts` | Adds `getUserCartList()` to filter carts by user ID. |
