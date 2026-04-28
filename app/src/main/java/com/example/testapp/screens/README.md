# 📱 screens

> All Activity classes representing the app's screens.  
> Every activity extends `BaseActivity`, which provides the `DatabaseService` singleton and toolbar setup.

### 🔐 Auth

| Activity | Description |
|:---------|:------------|
| `SplashActivity` | Startup splash screen |
| `LandingActivity` | Entry point — navigate to login or register |
| `LoginActivity` | User login form |
| `RegisterActivity` | New user registration form |

### 🏠 Main

| Activity | Description |
|:---------|:------------|
| `MainActivity` | Main dashboard (different options for users vs admins) |
| `UserProfileActivity` | View and edit current user's profile |

### 🍔 Food

| Activity | Description |
|:---------|:------------|
| `FoodItemsActivity` | Browse and manage the food catalog |
| `AddFoodActivity` | Create or edit a food item *(admin)* |

### 🛒 Cart

| Activity | Description |
|:---------|:------------|
| `MyCartsActivity` | View the current user's carts |
| `AllCartsActivity` | View all carts across all users *(admin)* |
| `AddCartActivity` | Create a new cart and add food items |
| `CartDetailActivity` | View details of a specific cart |

### ⚙️ Admin

| Activity | Description |
|:---------|:------------|
| `AdminActivity` | Admin dashboard |
| `UsersListActivity` | View all registered users *(admin)* |

### 🧩 Base

| Activity | Description |
|:---------|:------------|
| `BaseActivity` | Abstract base — initializes `DatabaseService`, configures toolbar with back button |
