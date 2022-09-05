package com.shirosoftware.sealprogrammingmobile.ui.navigation

import androidx.lifecycle.ViewModel
import com.shirosoftware.sealprogrammingmobile.data.SealDetectionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _result = MutableStateFlow<SealDetectionResult?>(null)
    val result: StateFlow<SealDetectionResult?> = _result

    fun updateResult(result: SealDetectionResult) {
        _result.value = result
    }
}
