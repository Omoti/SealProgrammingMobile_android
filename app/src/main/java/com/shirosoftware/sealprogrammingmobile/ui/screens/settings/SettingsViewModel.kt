package com.shirosoftware.sealprogrammingmobile.ui.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shirosoftware.sealprogrammingmobile.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlin.math.floor

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: SettingsRepository) :
    ViewModel() {
    val threshold = repository.threshold
    val showScore = repository.showScore

    fun updateThreshold(value: Float) {
        Log.d("SettingsViewModel", "update: $value")
        val ceilValue = ((floor(value.toDouble() * 10 + 0.5)) / 10).toFloat()

        Log.d("SettingsViewModel", "update ceil: $ceilValue")
        viewModelScope.launch {
            repository.updateThreshold(ceilValue)
        }
    }

    fun updateShowScore(value: Boolean) {
        viewModelScope.launch {
            repository.updateShowScore(value)
        }
    }
}
