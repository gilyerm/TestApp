# 🔥 services/impl/firestore

> Cloud Firestore implementations of the service interfaces.

| Class | Implements | Firestore Collection | Description |
|:------|:-----------|:---------------------|:------------|
| `FirestoreService<T>` | `ICrudService<T>` | *(generic)* | Base class handling all CRUD via Cloud Firestore (create, getById, getAll, delete, transaction-based update). |
| `UserFirestoreServiceImpl` | `IUserService` | `users` | Adds login lookup and email existence check via indexed Firestore queries. |
| `FoodFirestoreServiceImpl` | `IFoodService` | `foods` | Standard CRUD operations for foods. |
| `CartFirestoreServiceImpl` | `ICartService` | `carts` | Adds `getUserCartList()` to filter carts by user ID. |
