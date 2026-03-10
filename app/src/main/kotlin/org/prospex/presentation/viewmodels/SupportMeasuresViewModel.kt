package org.prospex.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.MeasureType
import org.prospex.domain.models.SupportMeasure
import org.prospex.domain.repositories.ISupportMeasureRepository

class SupportMeasuresViewModel(
    private val supportMeasureRepository: ISupportMeasureRepository
) : ViewModel() {

    private val _measuresState = MutableStateFlow<SupportMeasuresState>(SupportMeasuresState.Loading)
    val measuresState: StateFlow<SupportMeasuresState> = _measuresState.asStateFlow()

    private val _legalTypeFilter = MutableStateFlow<LegalType?>(null)
    private val _measureTypeFilter = MutableStateFlow<MeasureType?>(null)

    fun loadMeasures() {
        viewModelScope.launch {
            _measuresState.value = SupportMeasuresState.Loading

            try {
                val all = _legalTypeFilter.value?.let { supportMeasureRepository.getByLegalType(it) }
                    ?: supportMeasureRepository.getAll()
                val filtered = _measureTypeFilter.value?.let { type ->
                    all.filter { it.measureType == type }
                } ?: all.toList()
                _measuresState.value = SupportMeasuresState.Success(filtered.toTypedArray())
            } catch (e: Exception) {
                _measuresState.value = SupportMeasuresState.Error("Ошибка загрузки мер поддержки: ${e.message}")
            }
        }
    }

    fun setLegalTypeFilter(legalType: LegalType?) {
        _legalTypeFilter.value = legalType
        loadMeasures()
    }

    fun setMeasureTypeFilter(measureType: MeasureType?) {
        _measureTypeFilter.value = measureType
        loadMeasures()
    }
}

sealed class SupportMeasuresState {
    data object Loading : SupportMeasuresState()
    data class Success(val measures: Array<SupportMeasure>) : SupportMeasuresState()
    data class Error(val message: String) : SupportMeasuresState()
}

