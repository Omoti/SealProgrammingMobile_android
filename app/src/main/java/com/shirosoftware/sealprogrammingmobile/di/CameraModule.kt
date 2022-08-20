package com.shirosoftware.sealprogrammingmobile.di

import android.content.Context
import com.shirosoftware.sealprogrammingmobile.camera.CameraController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CameraModule {
    @Provides
    fun provideCameraController(@ApplicationContext context: Context): CameraController {
        return CameraController(context)
    }
}