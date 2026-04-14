package com.example.deliveryapp.core.network.di

import com.example.deliveryapp.core.network.NetworkDataSource
import com.example.deliveryapp.core.network.retrofit.ProdNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkProvidesModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindsModule {

    @Binds
    abstract fun bindsNetworkDatasource(impl: ProdNetworkDataSource): NetworkDataSource
}