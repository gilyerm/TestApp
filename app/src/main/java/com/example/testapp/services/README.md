# ⚡ services

> Database access layer: interfaces for each entity, plus `DatabaseService`, which picks the backend.

ViewModels only use these **interfaces** (given to them by `ViewModelFactory`), so they work with either backend.

## Interfaces

| Interface | Extends | Methods |
|:----------|:--------|:--------|
| `ICrudService<T extends Identifiable>` | — | `generateId()`, `create`, `getById`, `getAll`, `delete`, `update(id, UnaryOperator<T>, callback)` |
| `IUserService` | `ICrudService<User>` | + `getUserByEmailAndPassword(email, password, cb)` (result is `null` if no match), `checkIfEmailExists(email, cb)` |
| `IFoodService` | `ICrudService<Food>` | — |
| `ICartService` | `ICrudService<Cart>` | + `getUserCartList(uid, cb)` |
| `IDatabaseService` | — | `getUserService()`, `getFoodService()`, `getCartService()` |

### Callbacks

Every call is asynchronous and reports back through `ICrudService.DatabaseCallback<R>`:

```java
databaseService.getCartService().getById(cartId, new ICrudService.DatabaseCallback<Cart>() {
    @Override public void onCompleted(Cart cart) { /* success */ }
    @Override public void onFailed(Exception e)  { /* error */ }
});
```

For `create`, `delete`, and `update` the callback may be `null`.
`update` passes the current object to your function and saves what it returns (runs as a transaction).

## `DatabaseService`

Singleton that implements `IDatabaseService` and creates the Cloud Firestore services.

| Member | Description |
|:-------|:------------|
| `getInstance()` | Returns the shared instance (used by `ViewModelFactory`) |

## Implementations

| Backend | Folder |
|:--------|:-------|
| Cloud Firestore (used) | [`impl/firestore/`](impl/firestore/) |
| Firebase Realtime Database (alternative) | [`impl/rtdb/`](impl/rtdb/) |

Both backends use the same names: `users`, `foods`, `carts`.
