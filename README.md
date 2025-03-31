# 🛒 Daily Needs Store Stock App 📊

A Jetpack Compose-based shop management app that allows users to add, edit, and manage product inventory with features like image selection, low-stock alerts, and Firebase integration.

---

### ✨ Features :

- 📸 Image Upload: Pick product images from Gallery or Camera.

- 🏷 Product Management: Add, edit, and delete products with company name, price, stock, and category.

- ⚠ Low Stock Alerts: Set low-stock thresholds to track inventory.

- 🔎 Filtering: Toggle between showing all products or low-stock-only products.

- 🔥 Firebase Integration:

1. Firestore Database (for syncing products)
2. Firebase Storage (for storing images)
3. Firebase Authentication (optional, for user login)

- 🛠 MVVM Architecture with Room Database.

---

### 🚀 Tech Stack :

- Kotlin with Jetpack Compose

- MVVM Architecture

- Room Database

- Coil (for image loading)

- Firebase (Firestore, Storage, Auth, Crashlytics, FCM)

- Accompanist Permissions (for camera and storage access)

---

### Dependencies Used :

- Add the following dependencies to your build.gradle file:
    ```
 // UI Components
- implementation(libs.androidx.material3) // Material 3 for modern UI

// Database - Room
- implementation(libs.androidx.room.ktx.v252) // Room with Kotlin extensions
- implementation(libs.androidx.room.runtime) // Room runtime
- kapt("androidx.room:room-compiler:2.6.1") // Annotation processor for Room
- annotationProcessor(libs.androidx.room.compiler.v240beta01) // Alternative for KAPT

// Lifecycle & Permissions
- implementation(libs.androidx.lifecycle.viewmodel.compose) // ViewModel support in Compose
- implementation(libs.accompanist.permissions) // Accompanist for handling runtime permissions

// Image Loading
- implementation(libs.coil.compose) // Coil for image loading in Jetpack Compose
    ```
---

### Project Structure :

```yaml
com.example.dailyneedsstore
│── data                   # Data layer (Database, DAO, Repository)
│   ├── AppDatabase.kt     # Room Database instance
│   ├── Product.kt         # Data model representing a product
│   ├── ProductDao.kt      # Data Access Object (DAO) for database operations
│   ├── ProductRepository.kt # Repository to abstract database operations
│
│── ui.theme               # UI layer (Compose components, themes)
│   ├── AddProductDialog.kt # Dialog for adding a new product
│   ├── Color.kt           # Theme colors
│   ├── EditProductDialog.kt # Dialog for editing an existing product
│   ├── ProductItem.kt     # UI representation of a product in the list
│   ├── ProductScreen.kt   # Main screen showing products
│   ├── Theme.kt           # Application theme
│   ├── Type.kt            # Typography styles
│
│── vm                     # ViewModel layer (Business logic)
│   ├── MainActivity.kt    # Entry point of the app
```

---

### How to Use :
1. Clone the repository
```git clone <repository-url>```

2. Open the project in Android Studio.

3. Add the required dependencies to your build.gradle file if not already included.

4. Run the app on an emulator or a physical device.

5. Use the app to manage your contacts by adding, editing, and deleting them.

---

### Screenshots & App Demo Video:

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/5IBl5NDd8Ys/0.jpg)](https://youtube.com/shorts/5IBl5NDd8Ys?feature=shared)
---

### License

This project is open-source and available under the MIT License.
