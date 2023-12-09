package com.example.urbanquest.ui.state_holder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.urbanquest.data.databases.ProductDatabase
import com.example.urbanquest.data.models.ProductEntity
import com.example.urbanquest.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository

    init {
        val productDao = ProductDatabase.getDatabase(application).productDao()
        productRepository = ProductRepository(productDao)
    }

    fun getAllProducts(): LiveData<List<ProductEntity>> {
        return productRepository.getAllProducts()
    }

    fun insertProduct(product: ProductEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.insertProduct(product)
        }
    }
}

    /*private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> = _product

    val allProducts: LiveData<List<ProductEntity>> = repository.getAllProducts()

    fun fetchProductById(id: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getProductById(id)
                _product.postValue(result)
            } catch (e: Exception) {
                Log.d("ProductViewModel", "Ошибка")
            }
        }
    }*/
