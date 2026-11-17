# 🔗 adapters

> Adapters that show lists of data on screen.

All adapters extend **`BaseAdapter<T extends Identifiable, VH>`**, which holds the item list and notifies the RecyclerView.

| Adapter | Item type | Shows | Click listener | Used in |
|:--------|:----------|:------|:---------------|:--------|
| `CartAdapter` | `Cart` | Cart title and item count | `CartClickListener.onCartClick(Cart)` | My Carts, All Carts |
| `FoodsAdapter` | `FoodRow` (food + quantity) | Food image, name, quantity, unit price, line total | — | Add Cart, Cart Details, Food Items |
| `UserAdapter` | `User` | Profile picture (initials if none), full name, email, phone, "Admin" chip | `OnUserClickListener.onUserClick` / `onLongUserClick` | Users List |

## `BaseAdapter` methods

Items are matched by `getId()`, so passing an updated copy of an item works.

| Method | What it does |
|:-------|:-------------|
| `setItems(List<T>)` | Replace all items (null clears). Use from the LiveData observer. |
| `addItem(T)` | Add to the end. |
| `updateItem(T)` | Replace the item with the same ID; returns `false` if not found. |
| `removeItem(T)` | Remove the item with the same ID; returns `false` if not found. |
| `getItem(int)` | Item at a position (use in `onBindViewHolder`). |
| `indexOf(String id)` | Position of an ID, or `-1`. |

The ViewModel still owns the data. Use `add` / `update` / `remove` only to show a change the ViewModel already made (e.g. it emits one edited user), to get a row animation instead of a full redraw. Never use them to keep screen state in the adapter.

A new adapter only implements `onCreateViewHolder` and `onBindViewHolder`:

```java
public class FooAdapter extends BaseAdapter<Foo, FooAdapter.ViewHolder> {
    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { ... }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foo foo = getItem(position);
        ...
    }
}
```

> Not to be confused with `android.widget.BaseAdapter` (ListView / Spinner).

## `FoodsAdapter`

`setFoods(List<Food>)` takes foods as a flat list (each food repeated by its quantity, the way a `Cart` stores them) and shows one row per food ID with the quantity and line total. Use it instead of `setItems`.

## Notes

- **Adapters only display data (MVVM).** The ViewModel owns the list as `LiveData`; the Fragment observes it and passes it to `setItems` / `setFoods`. Never read data back out of an adapter or keep screen state in it — it is lost on rotation.

```java
// Fragment
viewModel.selectedFoods.observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
    @Override public void onChanged(List<Food> foods) { foodsAdapter.setFoods(foods); }
});
addButton.setOnClickListener(new View.OnClickListener() {
    // change state in the ViewModel, not the adapter
    @Override public void onClick(View v) { viewModel.addSelectedFood(food); }
});
```
