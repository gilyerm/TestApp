# 🔥 services/impl/firestore

> Cloud Firestore implementations of the service interfaces. **This is the default backend.**

| Class | Implements | Collection | Description |
|:------|:-----------|:-----------|:------------|
| `FirestoreService<T>` | `ICrudService<T>` | *(set by subclass)* | Generic base class with all CRUD operations; `update` runs inside a Firestore transaction |
| `UserFirestoreServiceImpl` | `IUserService` | `users` | Adds login lookup (email + password query) and email-exists check |
| `FoodFirestoreServiceImpl` | `IFoodService` | `foods` | CRUD only |
| `CartFirestoreServiceImpl` | `ICartService` | `carts` | Adds `getUserCartList(uid)` — query on the `uid` field |

## Adding a service for a new entity

```java
public class OrderFirestoreServiceImpl extends FirestoreService<Order> implements IOrderService {
    public OrderFirestoreServiceImpl() {
        super("orders", Order.class);
    }
}
```

Then return it from `DatabaseService` for the `FIRESTORE` backend. Subclasses can use `getCollectionReference()` to write their own queries.
