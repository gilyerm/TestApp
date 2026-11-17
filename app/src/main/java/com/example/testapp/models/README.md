# 📦 models

> Data classes for the main entities of the app. These are the objects stored in the database.

| Class | Fields | Notes |
|:------|:-------|:------|
| `User` | `id`, `email`, `password`, `firstName`, `lastName`, `phone`, `isAdmin`, `imageBase64` (profile picture, may be `null`) | `getFullName()` joins first and last name |
| `Food` | `id`, `name`, `price`, `imageBase64` | Copy constructor `Food(Food other)`; image is a base64 string |
| `Cart` | `id`, `title`, `foods` (`List<Food>`), `uid` (owner's user ID) | `addFood`, `addFoods`, `removeFood`, `getFood`, `clear`, `getTotalPrice()` |
| `Identifiable` | — | Interface with `getId()`; required by the generic `ICrudService<T>` |

## Rules for database entities

`User`, `Food`, and `Cart` all:

- implement `Identifiable` (so the CRUD services can find them by ID) and `Serializable`;
- have a **public no-argument constructor** and getters/setters — Firebase needs these to convert documents to objects;
- override `equals` / `hashCode` / `toString`.

Follow the same rules when adding a new entity.
