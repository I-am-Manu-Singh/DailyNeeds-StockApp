package com.example.dailyneedsstore.ui.theme

import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.dailyneedsstore.data.Product
import com.example.dailyneedsstore.vm.ProductViewModel
import java.io.File
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductDialog(
    product: Product,
    viewModel: ProductViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // --- State Variables ---
    var companyName by remember { mutableStateOf(product.companyName) }
    var name by remember { mutableStateOf(product.name) }
    var sellingPrice by remember { mutableStateOf(product.sellingPrice.toString()) }
    var costPrice by remember { mutableStateOf(product.costPrice.toString()) }
    var discount by remember { mutableStateOf(product.discount.toString()) }
    var stock by remember { mutableStateOf(product.stock.toString()) }
    var lowStockAlert by remember { mutableStateOf(product.lowStockAlert.toString()) }
    var selectedCategory by remember { mutableStateOf(product.category.ifEmpty { "Select Category" }) }

    // --- Image Handling ---
    var imageUri by remember { mutableStateOf<Uri?>(Uri.parse(product.imagePath)) }
    var photoFile by remember { mutableStateOf<File?>(null) }

    // --- Camera Permission Handling ---
    val cameraPermission = android.Manifest.permission.CAMERA

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Updates permission status when the Composable recomposes
    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Camera permission is required to take pictures!", Toast.LENGTH_SHORT).show()
        }
    }

    // --- Image Pickers ---
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri = it }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = Uri.fromFile(photoFile)
        }
    }

    // --- Category Dropdown ---
    val categories = listOf("Biscuit", "Soap", "Oil", "Waffers", "Toothpaste", "Toffee", "Coldrink", "Rashan", "Dairy", "Chocolate", "Fruit", "Vegetables", "Other")
    var expanded by remember { mutableStateOf(false) }

    // --- UI AlertDialog ---
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Product") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()) // Enable scrolling
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    imageUri?.let {
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Text("No Image Selected")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // --- Buttons: Gallery and Camera ---
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                        Text("Gallery")
                    }

                    Button(onClick = {
                        if (hasCameraPermission) {
                            // Permission granted: proceed with camera
                            photoFile = createImageFile(context)

                            photoFile?.let { file ->
                                val photoUri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.provider",
                                    file
                                )
                                cameraLauncher.launch(photoUri)
                            } ?: run {
                                Toast.makeText(context, "Error creating image file", Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            // Request permission if not granted yet
                            cameraPermissionLauncher.launch(cameraPermission)
                        }
                    }) {
                        Text("Camera")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // --- Input Fields ---
                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Company Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = sellingPrice,
                    onValueChange = { sellingPrice = it },
                    label = { Text("Selling Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = costPrice,
                    onValueChange = { costPrice = it },
                    label = { Text("Cost Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // --- Dropdown for Category ---
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = lowStockAlert,
                    onValueChange = { lowStockAlert = it },
                    label = { Text("Low Stock Alert Threshold") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotEmpty() &&
                    sellingPrice.isNotEmpty() &&
                    costPrice.isNotEmpty() &&
                    stock.isNotEmpty() &&
                    selectedCategory != "Select Category"
                ) {
                    val margin = sellingPrice.toDouble() - costPrice.toDouble()

                    val savedImagePath = imageUri?.let {
                        saveImageToInternalStorage(context, it)
                    } ?: product.imagePath

                    val updatedProduct = product.copy(
                        companyName = companyName,
                        name = name,
                        imagePath = savedImagePath,
                        sellingPrice = sellingPrice.toDouble(),
                        costPrice = costPrice.toDouble(),
                        margin = margin,
                        discount = discount.toDoubleOrNull() ?: 0.0,
                        stock = stock.toInt(),
                        lowStockAlert = lowStockAlert.toIntOrNull() ?: 5,
                        category = selectedCategory
                    )

                    viewModel.updateProduct(updatedProduct)
                    onDismiss()

                } else {
                    Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Update Product")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
