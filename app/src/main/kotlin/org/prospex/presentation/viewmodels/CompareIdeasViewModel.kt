package org.prospex.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.prospex.application.usecases.GetIdeasUseCase
import org.prospex.application.utilities.Result
import org.prospex.domain.models.Idea
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.PageModel
import org.prospex.domain.value_objects.Positive

class CompareIdeasViewModel(
    private val getIdeasUseCase: GetIdeasUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CompareIdeasState>(CompareIdeasState.NoSelection)
    val state: StateFlow<CompareIdeasState> = _state.asStateFlow()

    fun selectLegalType(legalType: LegalType) {
        viewModelScope.launch {
            _state.value = CompareIdeasState.Loading
            val result = getIdeasUseCase.execute(
                GetIdeasUseCase.Params(
                    page = Positive(1u),
                    pageSize = Positive(100u),
                    legalType = legalType
                )
            )
            when (result) {
                is Result.Success -> {
                    _state.value = CompareIdeasState.Success(
                        legalType = legalType,
                        ideas = result.data.items.toList()
                    )
                }
                is Result.Error -> {
                    _state.value = CompareIdeasState.Error(result.message)
                }
            }
        }
    }
}

sealed class CompareIdeasState {
    data object NoSelection : CompareIdeasState()
    data object Loading : CompareIdeasState()
    data class Success(val legalType: LegalType, val ideas: List<Idea>) : CompareIdeasState()
    data class Error(val message: String) : CompareIdeasState()
}
