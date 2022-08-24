package com.shirosoftware.sealprogrammingmobile.di

import android.content.Context
import com.shirosoftware.sealprogrammingmobile.camera.CameraController
import com.shirosoftware.sealprogrammingmobile.device.bluetooth.BluetoothConnection
import com.shirosoftware.sealprogrammingmobile.device.bluetooth.BluetoothController
import com.shirosoftware.sealprogrammingmobile.ml.SealDetector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Modules {
    @Provides
    fun provideCameraController(@ApplicationContext context: Context): CameraController {
        return CameraController(context)
    }

    @Provides
    fun provideSealDetector(@ApplicationContext context: Context): SealDetector {
        return SealDetector(context)
    }

    @Provides
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController {
        return BluetoothController(context)
    }

    @Provides
    fun provideBluetoothConnection(): BluetoothConnection {
        return BluetoothConnection()
    }
}
