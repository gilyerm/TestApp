# 🖥️ screens

> Every UI screen of the app. Each screen is a folder with a **Fragment** (the View) and a **ViewModel** (state and logic).

All fragments extend [`BaseFragment`](../core/) and are shown inside `MainActivity`. ViewModels are created with [`ViewModelFactory`](../core/), which passes them the `IDatabaseService`.

## Screens

| Folder | Toolbar title | Back button | What it does | Services used |
|:-------|:--------------|:-----------:|:-------------|:--------------|
| `splash/` | — | ❌ | Waits 3 seconds, then opens Dashboard if logged in, otherwise Landing | — |
| `landing/` | Landing | ❌ | Buttons to Login or Register | — |
| `login/` | Login | ✅ | Validates input and logs in; saves the user locally and opens Dashboard | `getUserByEmailAndPassword` |
| `register/` | Register | ✅ | Validates input, checks the email is free, creates the user, opens Dashboard | `checkIfEmailExists`, `create` |
| `dashboard/` | Dashboard | ❌ | Home screen: Add Food, Add Cart, My Carts, Edit Profile; Admin card only for admins | user `getById` |
| `addfood/` | Add Food | ✅ | Create a food item with name, price, and photo (camera or gallery) | food `create` |
| `addcart/` | Add Cart | ✅ | Pick foods from a dropdown, name the cart, save it | food `getAll`, cart `create` |
| `mycarts/` | My Carts | ✅ | The logged-in user's carts; tap to open details, button to add a cart | `getUserCartList` |
| `cartdetail/` | Cart Details | ✅ | Title, item count, total price, and items of one cart (`cart_id` argument) | cart `getById` |
| `userprofile/` | User Profile | ✅ | View/edit a profile and sign out. Opens the logged-in user, or another user for admins (`newInstance(userId)`) | user `getById`, `update` |
| `admin/` | Admin Dashboard | ✅ | Links to Users List, Food Items, All Carts | — |
| `userslist/` | Users List | ✅ | All users with a count; tap a user to open their profile | user `getAll` |
| `fooditems/` | Food Items | ✅ | All food items | food `getAll` |
| `allcarts/` | All Carts | ✅ | Every cart in the database; tap to open details | cart `getAll` |

## Pattern

A shortened version of the real [`userslist/`](userslist/) screen:

```java
// <Name>ViewModel
public class UsersListViewModel extends ViewModel {
    private final IDatabaseService databaseService;
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();

    public UsersListViewModel(IDatabaseService databaseService) {   // required by ViewModelFactory
        this.databaseService = databaseService;
    }

    public LiveData<List<User>> getUsers() { return users; }

    public void loadUsers() {
        databaseService.getUserService().getAll(new ICrudService.DatabaseCallback<>() {
            @Override public void onCompleted(List<User> list) { users.postValue(list); }
            @Override public void onFailed(Exception e) { /* post an error */ }
        });
    }
}

// <Name>Fragment
public class UsersListFragment extends BaseFragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UsersListViewModel vm = new ViewModelProvider(this, new ViewModelFactory()).get(UsersListViewModel.class);
        vm.getUsers().observe(getViewLifecycleOwner(), users -> { /* update UI */ });
        vm.loadUsers();
    }

    @Override
    protected String getTitle() { return "Users List"; }
}
```

## Rules

- **Fragments only show data.** Rendering and forwarding clicks only; database calls belong in the ViewModel.
- **Expose state as `LiveData`.** Keep `MutableLiveData` private; observe with `getViewLifecycleOwner()`.
- **Navigate through `navigationHost`.** Never start Activities; use `navigateTo(...)` / `goBack()`.
- **Pass arguments with a `Bundle`** (`setArguments`) or a static `newInstance(...)` factory, not constructor parameters.
- **One folder per screen.** Name files `<Name>Fragment` / `<Name>ViewModel`, layout `fragment_<name>.xml`.
