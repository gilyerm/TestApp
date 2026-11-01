# ⚡ services

> Service interfaces defining the data access layer, plus the `DatabaseService` singleton that wires everything together.

### Interfaces

| Interface | Extends | Description |
|:----------|:--------|:------------|
| `ICrudService<T>` | — | Generic CRUD operations (create, getById, getAll, delete, update) with async `DatabaseCallback<R>` |
| `IUserService` | `ICrudService<User>` | Adds `getUserByEmailAndPassword()` and `checkIfEmailExists()` |
| `IFoodService` | `ICrudService<Food>` | No additional methods |
| `ICartService` | `ICrudService<Cart>` | Adds `getUserCartList()` to fetch carts by user ID |
| `IDatabaseService` | — | Root interface exposing `getUserService()`, `getFoodService()`, `getCartService()` |

### Classes

| Class | Description |
|:------|:------------|
| `DatabaseService` | Singleton implementing `IDatabaseService` — wires each service to its implementation. Swap the backend by changing the `impl` classes here. |

> 📂 Implementations are organized by backend:
> - [`impl/rtdb/`](impl/rtdb/) — Firebase Realtime Database implementations
> - [`impl/firestore/`](impl/firestore/) — Cloud Firestore implementations
