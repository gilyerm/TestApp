# 📦 models

> Data classes representing the core entities of the application.

| Class | Description |
|:------|:------------|
| `User` | App user — email, password, name, phone, admin flag |
| `Food` | Single food item — name, price, base64 image |
| `Cart` | Shopping cart owned by a user, holds a list of `Food` items with add/remove and total price calculation |
| `Identifiable` | Interface requiring `getId()` — used by the generic CRUD service layer |
| `ImageSourceOption` | UI helper for image-source selection dialogs (title, description, icon) |

All entity classes implement `Serializable` and `Identifiable`.
