# 📱 com.example.testapp

> Root package of the app source code.

The app uses a **Single Activity Architecture** with **MVVM**. `MainActivity` is the only Activity; every screen is a Fragment paired with a ViewModel.

## Entry Point

| Class | Description |
|:------|:------------|
| `MainActivity` | The only Activity. Sets up the toolbar and edge-to-edge insets, shows `SplashFragment` on first launch, and implements `NavigationHost` to swap fragments in `R.id.fragment_container`. |

## Packages

| Package | Contents |
|:--------|:---------|
| [`core/`](core/) | Infrastructure: `BaseFragment`, `NavigationHost`, `ViewModelFactory` |
| [`screens/`](screens/) | One folder per screen, each with a Fragment and a ViewModel |
| [`models/`](models/) | Data entities: `User`, `Food`, `Cart`, and helpers |
| [`services/`](services/) | Database access interfaces, `DatabaseService`, and backend implementations |
| [`adapters/`](adapters/) | Adapters for lists (RecyclerView / dropdown) |
| [`utils/`](utils/) | Validation, image helpers, and local session storage |

## Dependency Direction

```
screens ──► core ──► services ──► models
   │                                ▲
   ├──► adapters ───────────────────┤
   └──► utils ──────────────────────┘
```

Screens depend on everything below them. `services` and `models` never depend on UI code.
