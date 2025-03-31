package com.example.dailyneedsstore.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyneedsstore.data.Product
import com.example.dailyneedsstore.data.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    val allProducts = repository.allProducts

    fun addProduct(product: Product) = viewModelScope.launch { repository.insert(product) }
    fun updateProduct(product: Product) = viewModelScope.launch { repository.update(product) }
    fun deleteProduct(product: Product) = viewModelScope.launch { repository.delete(product) }
}