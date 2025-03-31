package com.example.dailyneedsstore.ui.theme

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.dailyneedsstore.data.Product
import com.example.dailyneedsstore.vm.ProductViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddProductDialog(
    viewModel: ProductViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // --- State Variables ---
    var companyName by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var sellingPrice by remember { mutableStateOf("") }
    var costPrice by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var lowStockAlert by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Select Category") }

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    // --- Image Handling ---
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var photoFile by remember { mutableStateOf<File?>(null) }

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
    val categories = listOf(
        "Biscuit", "Soap", "Oil", "Waffers", "Toothpaste", "Toffee",
        "Coldrink", "Rashan", "Dairy", "Chocolate", "Fruit", "Vegetables", "Other"
    )
    var expanded by remember { mutableStateOf(false) }

    // --- UI AlertDialog ---
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Product") },
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
                    // ✅ Gallery Button
                    Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                        Text("Gallery")
                    }

                    // ✅ Camera Button with Permission Handling
                    Button(onClick = {
                        if (cameraPermissionState.status.isGranted) {
                            // Permission already granted -> open camera
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
                            // Request permission
                            cameraPermissionState.launchPermissionRequest()
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

                // --- Category Dropdown ---
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
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
                if (validateFields(
                        name,
                        companyName,
                        sellingPrice,
                        costPrice,
                        stock,
                        selectedCategory
                    )
                ) {
                    val margin = sellingPrice.toDouble() - costPrice.toDouble()

                    val savedImagePath = imageUri?.let {
                        saveImageToInternalStorage(context, it)
                    } ?: ""

                    val product = Product(
                        id = 0,
                        companyName = companyName,
                        name = name,
                        imagePath = savedImagePath,
                        sellingPrice = sellingPrice.toDouble(),
                        costPrice = costPrice.toDouble(),
                        margin = margin,
                        discount = 0.0,
                        stock = stock.toInt(),
                        lowStockAlert = lowStockAlert.toIntOrNull() ?: 5,
                        category = selectedCategory
                    )

                    viewModel.addProduct(product)
                    onDismiss()
                } else {
                    Toast.makeText(context, "Please fill all required fields!", Toast.LENGTH_SHORT)
                        .show()
                }
            }) {
                Text("Add Product")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// ✅ Extracted validation function for reuse
private fun validateFields(
    name: String,
    companyName: String,
    sellingPrice: String,
    costPrice: String,
    stock: String,
    selectedCategory: String
): Boolean {
    return name.isNotEmpty() &&
            companyName.isNotEmpty() &&
            sellingPrice.isNotEmpty() &&
            costPrice.isNotEmpty() &&
            stock.isNotEmpty() &&
            selectedCategory != "Select Category"
}

// ✅ Create Image File Function (Reused)
fun createImageFile(context: Context): File {
    val timestamp = System.currentTimeMillis()
    val fileName = "IMG_$timestamp.jpg"
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File(storageDir, fileName)
}

// ✅ Save Image to Internal Storage Function (Reused)
fun saveImageToInternalStorage(context: Context, imageUri: Uri): String {
    val contentResolver = context.contentResolver
    val fileName = "IMG_${System.currentTimeMillis()}.jpg"
    val directory = context.filesDir
    val file = File(directory, fileName)

    try {
        contentResolver.openInputStream(imageUri).use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream?.copyTo(outputStream)
                    ?: throw IOException("Failed to open input stream for URI: $imageUri")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }

    return file.absolutePath
}
