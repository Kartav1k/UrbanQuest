package com.example.urbanquest.ui.state_holder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.urbanquest.data.models.ProductEntity
import com.example.urbanquest.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(application: Application, val productRepository: ProductRepository) : AndroidViewModel(application) {
    fun getAllProducts(): LiveData<List<ProductEntity>> {
        return productRepository.getAllProducts()
    }
    fun insertProduct(product: ProductEntity){
        productRepository.insertProduct(product)
    }

}


