package com.example.dailyneedsstore.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imagePath: String,
    val sellingPrice: Double,
    val costPrice: Double,
    val margin: Double,
    val discount: Double,
    val stock: Int,
    val lowStockAlert: Int = 10, // Default alert when stock ≤ 10
    val category: String,
    val companyName: String
)