package com.example.urbanquest.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.urbanquest.data.models.ProductEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: ProductEntity)

    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<ProductEntity>>

}
