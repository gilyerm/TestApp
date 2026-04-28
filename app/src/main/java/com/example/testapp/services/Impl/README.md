# 🔥 services/Impl

> Firebase Realtime Database implementations of the service interfaces.

| Class | Implements | Firebase Table | Description |
|:------|:-----------|:---------------|:------------|
| `FirebaseService<T>` | `ICrudService<T>` | *(generic)* | Base class handling all CRUD via Firebase — read, write, delete, transaction-based update. Subclasses only specify table name and entity class. |
| `UserServiceImpl` | `IUserService` | `users` | Adds login lookup and email existence check |
| `FoodServiceImpl` | `IFoodService` | `foods` | Standard CRUD, no extra methods |
| `CartServiceImpl` | `ICartService` | `carts` | Adds `getUserCartList()` to filter carts by user ID |
