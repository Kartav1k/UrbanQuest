package com.example.urbanquest.retrofit.api

import com.example.urbanquest.data.models.ProductEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductEntity
}