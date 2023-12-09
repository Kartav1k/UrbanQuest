package com.example.urbanquest.data.repository

import androidx.lifecycle.LiveData
import com.example.urbanquest.data.dao.ProductDao
import com.example.urbanquest.data.models.ProductEntity

class ProductRepository(private val productDao: ProductDao) {

    fun getAllProducts(): LiveData<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    fun insertProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }
}

    /*suspend fun getProductById(id: Int): Product {
        val product = productApi.getProductById(id)
        val productEntity = ProductEntity(id, product.title, product.description)
        productDao.insertProduct(productEntity)
        return product
    }

    fun getAllProducts(): LiveData<List<ProductEntity>> {
        return getAllProducts()
    }


    fun insertProductFromProduct(product: Product) {
        val productEntity = ProductEntity(
            product.id,
            product.title,
            product.description
        )
        productDao.insertProduct(productEntity)
    }*/

