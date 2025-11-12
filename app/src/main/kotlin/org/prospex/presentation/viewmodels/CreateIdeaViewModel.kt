package org.prospex.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.prospex.application.usecases.CreateIdeaUseCase
import org.prospex.application.utilities.Result
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.Question
import org.prospex.domain.models.QuestionOption
import org.prospex.domain.repositories.ISurveyRepository
import java.util.UUID

class CreateIdeaViewModel(
    private val createIdeaUseCase: CreateIdeaUseCase,
    private val surveyRepository: ISurveyRepository
) : ViewModel() {

    private val _createIdeaState = MutableStateFlow<CreateIdeaState>(CreateIdeaState.Idle)
    val createIdeaState: StateFlow<CreateIdeaState> = _createIdeaState.asStateFlow()

    private val _questions = MutableStateFlow<List<QuestionWithOptions>>(emptyList())
    val questions: StateFlow<List<QuestionWithOptions>> = _questions.asStateFlow()

    private val _selectedOptions = MutableStateFlow<Map<UUID, Set<UUID>>>(emptyMap())
    val selectedOptions: StateFlow<Map<UUID, Set<UUID>>> = _selectedOptions.asStateFlow()

    private var currentLegalType: LegalType? = null

    fun loadQuestions(legalType: LegalType) {
        viewModelScope.launch {
            currentLegalType = legalType
            _createIdeaState.value = CreateIdeaState.Loading

            try {
                val questions = surveyRepository.getQuestionsByLegalType(legalType)
                val questionsWithOptions = questions.map { question ->
                    val options = surveyRepository.getOptionsByQuestionId(question.id)
                    QuestionWithOptions(question, options.toList())
                }
                _questions.value = questionsWithOptions
                _selectedOptions.value = emptyMap()
                _createIdeaState.value = CreateIdeaState.Loaded
            } catch (e: Exception) {
                _createIdeaState.value = CreateIdeaState.Error("Ошибка загрузки вопросов: ${e.message}")
            }
        }
    }

    fun selectOption(questionId: UUID, optionId: UUID, isMultiple: Boolean) {
        val current = _selectedOptions.value.toMutableMap()
        if (isMultiple) {
            val currentSet = current[questionId]?.toMutableSet() ?: mutableSetOf()
            if (currentSet.contains(optionId)) {
                currentSet.remove(optionId)
            } else {
                currentSet.add(optionId)
            }
            if (currentSet.isEmpty()) {
                current.remove(questionId)
            } else {
                current[questionId] = currentSet
            }
        } else {
            current[questionId] = setOf(optionId)
        }
        _selectedOptions.value = current
    }

    fun createIdea(title: String, description: String) {
        viewModelScope.launch {
            val legalType = currentLegalType
            if (legalType == null) {
                _createIdeaState.value = CreateIdeaState.Error("Не выбран тип юридического лица")
                return@launch
            }

            val allOptionIds = _selectedOptions.value.values.flatten().toTypedArray()
            if (allOptionIds.isEmpty()) {
                _createIdeaState.value = CreateIdeaState.Error("Необходимо ответить на все вопросы")
                return@launch
            }

            _createIdeaState.value = CreateIdeaState.Creating

            val result = createIdeaUseCase.execute(
                CreateIdeaUseCase.Params(
                    idea = CreateIdeaUseCase.IdeaParams(
                        title = title,
                        description = description,
                        legalType = legalType
                    ),
                    surveyResponse = org.prospex.domain.models.SurveyResponse(
                        ideaId = UUID.randomUUID(),
                        optionIds = allOptionIds
                    )
                )
            )

            when (result) {
                is Result.Success -> {
                    _createIdeaState.value = CreateIdeaState.Success(result.data)
                }
                is Result.Error -> {
                    _createIdeaState.value = CreateIdeaState.Error(result.message)
                }
            }
        }
    }

    fun resetState() {
        _createIdeaState.value = CreateIdeaState.Idle
    }
}

data class QuestionWithOptions(
    val question: Question,
    val options: List<QuestionOption>
)

sealed class CreateIdeaState {
    data object Idle : CreateIdeaState()
    data object Loading : CreateIdeaState()
    data object Loaded : CreateIdeaState()
    data object Creating : CreateIdeaState()
    data class Success(val idea: org.prospex.domain.models.Idea) : CreateIdeaState()
    data class Error(val message: String) : CreateIdeaState()
}

