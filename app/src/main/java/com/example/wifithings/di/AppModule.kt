package com.example.wifithings.di

import android.content.Context
import com.example.wifithings.data.api.DeviceApiService
import com.example.wifithings.data.datastore.AppDataStore
import com.example.wifithings.data.repository.DeviceRepositoryImpl
import com.example.wifithings.domain.repository.DeviceRepository
import com.example.wifithings.domain.usecase.DeviceUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideMirrorApiService(client: OkHttpClient): DeviceApiService {
        return DeviceApiService(client)
    }

    @Provides
    @Singleton
    fun provideAppDataStore(@ApplicationContext context: Context): AppDataStore {
        return AppDataStore(context)
    }

    @Provides
    @Singleton
    fun provideMirrorRepository(
        apiService: DeviceApiService,
        dataStore: AppDataStore
    ): DeviceRepository {
        return DeviceRepositoryImpl(apiService, dataStore)
    }

    @Provides
    @Singleton
    fun provideMirrorUseCases(repository: DeviceRepository): DeviceUseCases {
        return DeviceUseCases(repository)
    }
}