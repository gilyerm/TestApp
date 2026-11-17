# 🧩 core

> Infrastructure for the Single Activity + MVVM architecture: navigation, a base fragment, and ViewModel creation.

| Class | Type | Description |
|:------|:-----|:------------|
| `NavigationHost` | Interface | Implemented by `MainActivity`. Lets fragments navigate and update the toolbar without knowing the Activity. |
| `BaseFragment<T extends ViewModel>` | Abstract class | Base class for every screen fragment. Gets the `NavigationHost`, sets the toolbar, and holds the screen's `viewModel`. |
| `ViewModelFactory` | `ViewModelProvider.Factory` | Creates ViewModels and passes them the shared `IDatabaseService`. |

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
- **`getViewModel(Class<T>)`** — returns the screen's ViewModel, created with `ViewModelFactory`.

Subclasses use the protected `navigationHost` field:

```java
navigationHost.navigateTo(new MyCartsFragment(), true);
```

## `ViewModelFactory`

Gets `DatabaseService.getInstance()` and calls the ViewModel's constructor that takes **one `IDatabaseService` argument** (found by reflection). Every ViewModel must have that constructor, even if it does not use the service.

```java
public class LoginFragment extends BaseFragment<LoginViewModel> { ... }

viewModel = getViewModel(LoginViewModel.class);   // BaseFragment helper; uses ViewModelFactory
```

Because the service is passed in instead of created inside the ViewModel, tests can pass a fake `IDatabaseService`.
