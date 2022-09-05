package com.shirosoftware.sealprogrammingmobile.di

import android.content.Context
import com.shirosoftware.sealprogrammingmobile.camera.ImageDataSource
import com.shirosoftware.sealprogrammingmobile.data.SettingsDataStore
import com.shirosoftware.sealprogrammingmobile.device.bluetooth.BluetoothConnection
import com.shirosoftware.sealprogrammingmobile.device.bluetooth.BluetoothController
import com.shirosoftware.sealprogrammingmobile.ml.SealDetector
import com.shirosoftware.sealprogrammingmobile.repository.ImageRepository
import com.shirosoftware.sealprogrammingmobile.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Modules {
    @Provides
    fun provideImageDataSource(@ApplicationContext context: Context): ImageDataSource {
        return ImageDataSource(context)
    }

    @Provides
    fun provideImageRepository(imageDataSource: ImageDataSource): ImageRepository {
        return ImageRepository(imageDataSource)
    }

    @Provides
    fun provideSealDetector(@ApplicationContext context: Context): SealDetector {
        return SealDetector(context)
    }

    @Provides
    fun provideBluetoothController(
        @ApplicationContext context: Context,
        connection: BluetoothConnection,
    ): BluetoothController {
        return BluetoothController(context, connection)
    }

    @Provides
    fun provideBluetoothConnection(): BluetoothConnection {
        return BluetoothConnection()
    }

    @Provides
    fun provideSettingsDataStore(
        @ApplicationContext context: Context,
    ): SettingsDataStore {
        return SettingsDataStore(context)
    }

    @Provides
    fun provideSettingsRepository(dataStore: SettingsDataStore): SettingsRepository {
        return SettingsRepository(dataStore)
    }
}
