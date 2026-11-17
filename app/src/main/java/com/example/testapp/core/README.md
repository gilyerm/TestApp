# 🧩 core

> Infrastructure for the Single Activity + MVVM architecture: navigation, a base fragment, and ViewModel creation.

| Class | Type | Description |
|:------|:-----|:------------|
| `NavigationHost` | Interface | Implemented by `MainActivity`. Lets fragments navigate and update the toolbar without knowing the Activity. |
| `BaseFragment<T extends BaseViewModel>` | Abstract class | Base class for every screen fragment. Gets the `NavigationHost`, sets the toolbar, and holds the screen's `viewModel`. |
| `BaseViewModel` | Abstract class | Base class for every screen ViewModel. Holds the shared `IDatabaseService` as the protected `databaseService` field. |

## `NavigationHost`

| Method | What it does |
|:-------|:-------------|
| `navigateTo(Fragment, boolean addToBackStack)` | Replaces the current fragment. Pass `false` for root screens (Splash → Landing / Dashboard, Login → Dashboard) so Back does not return to them. |
| `goBack()` | Pops the back stack, or closes the app if the stack is empty. |
| `setToolbarTitle(String)` | Sets the toolbar title. |
| `showBackButton(boolean)` | Shows or hides the toolbar's Up arrow. |

## `BaseFragment`

- **`onAttach`** — stores the host Activity as `navigationHost` (throws if the Activity does not implement `NavigationHost`).
- **`onDetach`** — clears `navigationHost`.
- **`onResume`** — sets the toolbar title from `getTitle()` and the back button from `shouldShowBackButton()`.
- **`getTitle()`** — *abstract*; return the toolbar title.
- **`shouldShowBackButton()`** — defaults to `true`; override to return `false` on root screens (Splash, Landing, Dashboard).
- **`viewModel`** — protected field of type `T`; assign it in the screen with `getViewModel(...)`.
- **`getViewModel(Class<T>)`** — returns the screen's ViewModel, created by the default `ViewModelProvider`.

Subclasses use the protected `navigationHost` field:

```java
navigationHost.navigateTo(new MyCartsFragment(), true);
```

## `BaseViewModel`

Sets the protected `databaseService` field from `DatabaseService.getInstance()`. Screen ViewModels extend it and need no constructor.

```java
public class LoginViewModel extends BaseViewModel { ... }

viewModel = getViewModel(LoginViewModel.class);   // BaseFragment helper
```
