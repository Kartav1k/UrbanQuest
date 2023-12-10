package com.example.urbanquest.data.di

import com.example.urbanquest.data.databases.ProductDatabase
import com.example.urbanquest.data.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideProductRepository(productDatabase: ProductDatabase): ProductRepository=ProductRepository(productDatabase.productDao())
}