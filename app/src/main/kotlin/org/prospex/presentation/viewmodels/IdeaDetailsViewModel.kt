package org.prospex.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.prospex.application.usecases.DeleteIdeaUseCase
import org.prospex.application.utilities.Result
import org.prospex.domain.models.Idea
import org.prospex.domain.models.QuestionOption
import org.prospex.domain.repositories.IIdeaRepository
import org.prospex.domain.repositories.ISurveyRepository
import java.util.UUID

class IdeaDetailsViewModel(
    private val ideaRepository: IIdeaRepository,
    private val surveyRepository: ISurveyRepository,
    private val deleteIdeaUseCase: DeleteIdeaUseCase
) : ViewModel() {

    private val _ideaDetailsState = MutableStateFlow<IdeaDetailsState>(IdeaDetailsState.Loading)
    val ideaDetailsState: StateFlow<IdeaDetailsState> = _ideaDetailsState.asStateFlow()

    fun loadIdeaDetails(ideaId: UUID) {
        viewModelScope.launch {
            _ideaDetailsState.value = IdeaDetailsState.Loading

            try {
                val idea = ideaRepository.get(ideaId)
                    ?: run {
                        _ideaDetailsState.value = IdeaDetailsState.Error("Идея не найдена")
                        return@launch
                    }

                val surveyResponse = surveyRepository.getSurveyResponse(ideaId)
                val questions = surveyRepository.getQuestionsByLegalType(idea.legalType)

                val questionsWithAnswers = questions.map { question ->
                    val options = surveyRepository.getOptionsByQuestionId(question.id)
                    val selectedOptions = if (surveyResponse != null) {
                        options.filter { option ->
                            surveyResponse.optionIds.contains(option.id)
                        }
                    } else {
                        emptyList()
                    }
                    QuestionWithAnswer(question, selectedOptions)
                }

                _ideaDetailsState.value = IdeaDetailsState.Success(idea, questionsWithAnswers)
            } catch (e: Exception) {
                _ideaDetailsState.value = IdeaDetailsState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }

    fun deleteIdea() {
        viewModelScope.launch {
            val currentState = _ideaDetailsState.value
            if (currentState is IdeaDetailsState.Success) {
                _ideaDetailsState.value = IdeaDetailsState.Loading
                val result = deleteIdeaUseCase.execute(currentState.idea.id)
                when (result) {
                    is Result.Success -> {
                        _ideaDetailsState.value = IdeaDetailsState.Deleted
                    }
                    is Result.Error -> {
                        _ideaDetailsState.value = IdeaDetailsState.Error(result.message)
                    }
                }
            }
        }
    }
}

data class QuestionWithAnswer(
    val question: org.prospex.domain.models.Question,
    val answers: List<QuestionOption>
)

sealed class IdeaDetailsState {
    data object Loading : IdeaDetailsState()
    data class Success(
        val idea: Idea,
        val questionsWithAnswers: List<QuestionWithAnswer>
    ) : IdeaDetailsState()
    data class Error(val message: String) : IdeaDetailsState()
    data object Deleted : IdeaDetailsState()
}

