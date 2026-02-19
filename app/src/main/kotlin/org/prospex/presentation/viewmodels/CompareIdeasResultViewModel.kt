package org.prospex.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.prospex.domain.models.Idea
import org.prospex.domain.repositories.IIdeaRepository
import org.prospex.domain.repositories.ISurveyRepository
import java.util.UUID

sealed class CompareIdeasResultState {
    data object Loading : CompareIdeasResultState()
    data class Success(
        val idea1: Idea,
        val idea2: Idea,
        val comparisonData: List<BlockComparison>
    ) : CompareIdeasResultState()
    data class Error(val message: String) : CompareIdeasResultState()
}

class CompareIdeasResultViewModel(
    private val ideaRepository: IIdeaRepository,
    private val surveyRepository: ISurveyRepository
) : ViewModel() {

    private val _state = MutableStateFlow<CompareIdeasResultState>(CompareIdeasResultState.Loading)
    val state: StateFlow<CompareIdeasResultState> = _state.asStateFlow()

    fun load(idea1Id: UUID, idea2Id: UUID) {
        viewModelScope.launch {
            _state.value = CompareIdeasResultState.Loading
            val idea1 = ideaRepository.get(idea1Id)
            val idea2 = ideaRepository.get(idea2Id)
            if (idea1 == null || idea2 == null) {
                _state.value = CompareIdeasResultState.Error("Идея не найдена")
                return@launch
            }
            if (idea1.legalType != idea2.legalType) {
                _state.value = CompareIdeasResultState.Error("Можно сравнивать только идеи одной формы")
                return@launch
            }
            val legalType = idea1.legalType
            val questions = surveyRepository.getQuestionsByLegalType(legalType)
            val maxPerBlock = surveyRepository.getMaxScorePerBlock(legalType)
            val optionIds1 = surveyRepository.getSurveyResponse(idea1Id)?.optionIds?.toSet() ?: emptySet()
            val optionIds2 = surveyRepository.getSurveyResponse(idea2Id)?.optionIds?.toSet() ?: emptySet()

            val blockComparisons = (1..5).map { blockOrder ->
                val blockQuestions = questions.filter { it.blockOrder == blockOrder }
                val questionComparisons = blockQuestions.map { q ->
                    val options = surveyRepository.getOptionsByQuestionId(q.id).toList()
                    val leftOpt = options.find { optionIds1.contains(it.id) }
                    val rightOpt = options.find { optionIds2.contains(it.id) }
                    QuestionComparison(
                        questionText = q.text,
                        leftOptionText = leftOpt?.text,
                        leftScore = leftOpt?.score?.value?.toInt() ?: 0,
                        rightOptionText = rightOpt?.text,
                        rightScore = rightOpt?.score?.value?.toInt() ?: 0
                    )
                }
                val leftBlockScore = questionComparisons.sumOf { it.leftScore }
                val rightBlockScore = questionComparisons.sumOf { it.rightScore }
                val maxScore = maxPerBlock[blockOrder] ?: 0
                BlockComparison(
                    blockOrder = blockOrder,
                    maxScore = maxScore,
                    questions = questionComparisons,
                    leftBlockScore = leftBlockScore,
                    rightBlockScore = rightBlockScore
                )
            }
            _state.value = CompareIdeasResultState.Success(
                idea1 = idea1,
                idea2 = idea2,
                comparisonData = blockComparisons
            )
        }
    }
}
