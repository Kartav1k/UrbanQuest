package com.example.urbanquest.data.repository

import androidx.lifecycle.LiveData
import com.example.urbanquest.data.dao.ProductDao
import com.example.urbanquest.data.models.ProductEntity
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productDao: ProductDao) {

    fun getAllProducts(): LiveData<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    fun insertProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }
}

