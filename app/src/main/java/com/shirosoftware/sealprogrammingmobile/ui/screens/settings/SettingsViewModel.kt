package com.shirosoftware.sealprogrammingmobile.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shirosoftware.sealprogrammingmobile.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: SettingsRepository) :
    ViewModel() {
    val threshold = repository.threshold.map { it / 10 }

    fun updateThreshold(value: Float) {
        viewModelScope.launch {
            repository.updateThreshold(value * 10)
        }
    }
}
