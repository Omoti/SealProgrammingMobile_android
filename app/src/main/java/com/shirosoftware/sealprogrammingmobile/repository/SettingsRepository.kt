package com.shirosoftware.sealprogrammingmobile.repository

import com.shirosoftware.sealprogrammingmobile.data.SettingsDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SettingsRepository @Inject constructor(private val dataStore: SettingsDataStore) {
    val threshold: Flow<Float> = dataStore.threshold
    suspend fun updateThreshold(value: Float) {
        dataStore.updateThreshold(value)
    }

    val showScore: Flow<Boolean> = dataStore.showScore
    suspend fun updateShowScore(value: Boolean) {
        dataStore.updateShowScore(value)
    }
}
