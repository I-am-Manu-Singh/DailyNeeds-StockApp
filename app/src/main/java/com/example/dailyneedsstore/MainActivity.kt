package com.example.dailyneedsstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.dailyneedsstore.data.AppDatabase
import com.example.dailyneedsstore.data.ProductRepository
import com.example.dailyneedsstore.ui.theme.ProductScreen
import com.example.dailyneedsstore.vm.ProductViewModel
import com.example.dailyneedsstore.vm.ProductViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ProductRepository(database.productDao())
        val viewModel = ViewModelProvider(this, ProductViewModelFactory(repository)).get(ProductViewModel::class.java)

        setContent {
            ProductScreen(viewModel)
        }
    }
}