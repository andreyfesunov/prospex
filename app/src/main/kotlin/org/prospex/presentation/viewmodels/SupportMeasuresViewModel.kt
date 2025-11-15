package org.prospex.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.prospex.domain.models.SupportMeasure
import org.prospex.domain.repositories.ISupportMeasureRepository

class SupportMeasuresViewModel(
    private val supportMeasureRepository: ISupportMeasureRepository
) : ViewModel() {

    private val _measuresState = MutableStateFlow<SupportMeasuresState>(SupportMeasuresState.Loading)
    val measuresState: StateFlow<SupportMeasuresState> = _measuresState.asStateFlow()

    fun loadMeasures() {
        viewModelScope.launch {
            _measuresState.value = SupportMeasuresState.Loading

            try {
                val measures = supportMeasureRepository.getAll()
                _measuresState.value = SupportMeasuresState.Success(measures)
            } catch (e: Exception) {
                _measuresState.value = SupportMeasuresState.Error("Ошибка загрузки мер поддержки: ${e.message}")
            }
        }
    }
}

sealed class SupportMeasuresState {
    data object Loading : SupportMeasuresState()
    data class Success(val measures: Array<SupportMeasure>) : SupportMeasuresState()
    data class Error(val message: String) : SupportMeasuresState()
}

