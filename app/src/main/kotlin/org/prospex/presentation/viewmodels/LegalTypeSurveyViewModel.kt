package org.prospex.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.LegalTypeSurveyOption
import org.prospex.domain.models.LegalTypeSurveyQuestion
import org.prospex.domain.repositories.ILegalTypeSurveyRepository

data class LegalTypeSurveyQuestionWithOptions(
    val question: LegalTypeSurveyQuestion,
    val options: List<LegalTypeSurveyOption>
)

class LegalTypeSurveyViewModel(
    private val repository: ILegalTypeSurveyRepository
) : ViewModel() {

    private val _state = MutableStateFlow<LegalTypeSurveyState>(LegalTypeSurveyState.Loading)
    val state: StateFlow<LegalTypeSurveyState> = _state.asStateFlow()

    private val _selectedOptions = mutableMapOf<java.util.UUID, LegalTypeSurveyOption>()
    val selectedOptions: Map<java.util.UUID, LegalTypeSurveyOption> get() = _selectedOptions

    fun loadSurvey() {
        viewModelScope.launch {
            _state.value = LegalTypeSurveyState.Loading
            try {
                val questions = repository.getAllQuestions()
                val withOptions = questions.map { q ->
                    LegalTypeSurveyQuestionWithOptions(q, repository.getOptionsByQuestionId(q.id))
                }
                _state.value = LegalTypeSurveyState.Questions(withOptions)
            } catch (e: Exception) {
                _state.value = LegalTypeSurveyState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }

    fun selectOption(questionId: java.util.UUID, option: LegalTypeSurveyOption) {
        _selectedOptions[questionId] = option
    }

    fun submitSurvey(): LegalType? {
        val current = _state.value
        if (current !is LegalTypeSurveyState.Questions) return null
        val scores = mutableMapOf<LegalType, Int>()
        LegalType.entries.forEach { scores[it] = 0 }
        _selectedOptions.values.forEach { option ->
            scores[option.legalType] = (scores[option.legalType] ?: 0) + option.score
        }
        val recommended = scores.maxByOrNull { it.value }?.key
        return recommended
    }
}

sealed class LegalTypeSurveyState {
    data object Loading : LegalTypeSurveyState()
    data class Questions(val questionsWithOptions: List<LegalTypeSurveyQuestionWithOptions>) : LegalTypeSurveyState()
    data class Error(val message: String) : LegalTypeSurveyState()
}
