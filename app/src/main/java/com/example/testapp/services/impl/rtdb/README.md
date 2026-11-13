# ⚡ services/impl/rtdb

> Firebase Realtime Database implementations of the service interfaces.

To use this backend instead of Firestore, call `DatabaseService.setBackend(DatabaseService.DatabaseBackend.REALTIME_DATABASE)` before the database is first used.

| Class | Implements | Path | Description |
|:------|:-----------|:-----|:------------|
| `FirebaseService<T>` | `ICrudService<T>` | *(set by subclass)* | Generic base class with all CRUD operations; `update` uses `runTransaction` |
| `UserFirebaseServiceImpl` | `IUserService` | `users` | Adds login lookup and email-exists check using database queries |
| `FoodFirebaseServiceImpl` | `IFoodService` | `foods` | CRUD only |
| `CartFirebaseServiceImpl` | `ICartService` | `carts` | Adds `getUserCartList(uid)` — filters carts by owner |

## Adding a service for a new entity

```java
public class OrderFirebaseServiceImpl extends FirebaseService<Order> implements IOrderService {
    public OrderFirebaseServiceImpl() {
        super("orders", Order.class);
    }
}
```

Then return it from `DatabaseService` for the `REALTIME_DATABASE` backend. Subclasses can use `getTableReference()` to write their own queries.
