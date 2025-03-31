package com.example.dailyneedsstore.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.dailyneedsstore.data.Product

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductItem(
    product: Product,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    val isLowStock = product.stock <= product.lowStockAlert
    val calculatedMargin = product.sellingPrice - product.costPrice
    val originalPrice = product.sellingPrice + product.discount
    val calculatedDiscount = if (originalPrice != 0.0) {
        (product.discount / originalPrice) * 100
    } else {
        0.0
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isLowStock) Color(0xFFFFCCCC) else Color(0xFFCCFFCC)
        ),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = {}, // Optional: View details
                onLongClick = onEdit
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(product.imagePath),
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)) // Optional: Add rounded corners for a polished look
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(text = product.companyName, style = MaterialTheme.typography.titleLarge)
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Category: ${product.category}", style = MaterialTheme.typography.titleSmall)
                Text(text = "Selling Price: ₹${product.sellingPrice}")
                Text(text = "Cost Price: ₹${product.costPrice}")
                Text(
                    text = "Discount: ${"%.2f".format(calculatedDiscount)}%")
                Text(
                    text = "Margin: ₹${"%.2f".format(calculatedMargin)}")
                Text(text = "Stock: ${product.stock}")
                if (isLowStock) {
                    Text(
                        text = "⚠️ Low Stock!",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}