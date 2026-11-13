# 🔗 adapters

> Adapters that show lists of data on screen.

| Adapter | Base class | Shows | Click listener | Used in |
|:--------|:-----------|:------|:---------------|:--------|
| `CartAdapter` | `RecyclerView.Adapter` | Cart title and item count | `CartClickListener.onCartClick(Cart)` | My Carts, All Carts |
| `FoodsAdapter` | `RecyclerView.Adapter` | Food image, name, quantity, unit price, line total | — | Add Cart, Cart Details, Food Items |
| `UserAdapter` | `RecyclerView.Adapter` | Initials, full name, email, phone, "Admin" chip | `OnUserClickListener.onUserClick` / `onLongUserClick` | Users List |
| `ImageSourceAdapter` | `ArrayAdapter` | Camera / gallery options (icon, title, description) | `OnImageSourceSelectedListener.onImageSourceSelected` | Add Food |

## Notes

- **`FoodsAdapter` groups duplicates.** `addFood` adds 1 to the quantity if a food with the same ID is already listed. `getFoods()` returns a flat list with each food repeated by its quantity (which is how a `Cart` stores them).
- **Updating lists:** `CartAdapter.setCartList(...)` and `UserAdapter.setUserList(...)` replace the whole list; use the `add…` / `remove…` / `updateUser` methods for single items.
