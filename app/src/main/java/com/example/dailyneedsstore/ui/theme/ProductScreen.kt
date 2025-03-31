package com.example.dailyneedsstore.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dailyneedsstore.data.Product
import com.example.dailyneedsstore.vm.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductScreen(viewModel: ProductViewModel) {
    val products by viewModel.allProducts.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showLowStockOnly by remember { mutableStateOf(false) } //

    val filteredProducts = products.filter { product ->
        val query = searchQuery.trim().lowercase()
        val matchesSearch = product.name.lowercase().contains(query) ||
                product.companyName.lowercase().contains(query) ||
                product.category.lowercase().contains(query)
        val isLowStock = product.stock <= product.lowStockAlert

        matchesSearch && (!showLowStockOnly || isLowStock)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = "DAILY NEEDS Shop Stock List",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000),
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by Company, Name or Category") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Show Low Stock Only",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = showLowStockOnly,
                    onCheckedChange = { showLowStockOnly = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF4CAF50),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Gray
                    )
                )
            }

            LazyColumn {
                items(filteredProducts) { product ->
                    ProductItem(
                        product,
                        onDelete = {
                            scope.launch { viewModel.deleteProduct(product) }
                        },
                        onEdit = {
                            selectedProduct = product
                            showEditDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddProductDialog(viewModel) { showAddDialog = false }
    }

    if (showEditDialog && selectedProduct != null) {
        EditProductDialog(
            product = selectedProduct!!,
            viewModel = viewModel
        ) {
            showEditDialog = false
            selectedProduct = null
        }
    }
}