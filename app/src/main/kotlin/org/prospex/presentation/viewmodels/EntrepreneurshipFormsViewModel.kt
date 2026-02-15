package org.prospex.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.prospex.domain.models.EntrepreneurshipForm
import org.prospex.domain.repositories.IEntrepreneurshipFormRepository

class EntrepreneurshipFormsViewModel(
    private val repository: IEntrepreneurshipFormRepository
) : ViewModel() {

    private val _state = MutableStateFlow<EntrepreneurshipFormsState>(EntrepreneurshipFormsState.Loading)
    val state: StateFlow<EntrepreneurshipFormsState> = _state.asStateFlow()

    fun loadForms() {
        viewModelScope.launch {
            _state.value = EntrepreneurshipFormsState.Loading
            try {
                val forms = repository.getAll()
                _state.value = EntrepreneurshipFormsState.Success(forms)
            } catch (e: Exception) {
                _state.value = EntrepreneurshipFormsState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }
}

sealed class EntrepreneurshipFormsState {
    data object Loading : EntrepreneurshipFormsState()
    data class Success(val forms: Array<EntrepreneurshipForm>) : EntrepreneurshipFormsState()
    data class Error(val message: String) : EntrepreneurshipFormsState()
}
