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
import org.prospex.domain.models.QuestionOption
import org.prospex.domain.repositories.ISurveyRepository
import org.prospex.domain.value_objects.Positive
import java.util.UUID

data class IdeaWithBlockScores(val idea: Idea, val blockScores: List<BlockScore>)

data class QuestionComparison(
    val questionText: String,
    val leftOptionText: String?,
    val leftScore: Int,
    val rightOptionText: String?,
    val rightScore: Int
)

data class BlockComparison(
    val blockOrder: Int,
    val maxScore: Int,
    val questions: List<QuestionComparison>,
    val leftBlockScore: Int,
    val rightBlockScore: Int
)

class CompareIdeasViewModel(
    private val getIdeasUseCase: GetIdeasUseCase,
    private val surveyRepository: ISurveyRepository
) : ViewModel() {

    private val _state = MutableStateFlow<CompareIdeasState>(CompareIdeasState.NoSelection)
    val state: StateFlow<CompareIdeasState> = _state.asStateFlow()

    private var lastIdeasLoaded: CompareIdeasState.IdeasLoaded? = null

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
                    val ideas = result.data.items.toList()
                    val questions = surveyRepository.getQuestionsByLegalType(legalType)
                    val maxPerBlock = surveyRepository.getMaxScorePerBlock(legalType)
                    val ideasWithBlockScores = ideas.map { idea ->
                        val optionIds = surveyRepository.getSurveyResponse(idea.id)?.optionIds?.toSet() ?: emptySet()
                        val blockScores = (1..5).map { blockOrder ->
                            val score = questions
                                .filter { it.blockOrder == blockOrder }
                                .flatMap { q -> surveyRepository.getOptionsByQuestionId(q.id).toList() }
                                .filter { optionIds.contains(it.id) }
                                .sumOf { it.score.value.toInt() }
                            val maxScore = maxPerBlock[blockOrder] ?: 0
                            BlockScore(blockOrder, score, maxScore)
                        }
                        IdeaWithBlockScores(idea, blockScores)
                    }
                    val loaded = CompareIdeasState.IdeasLoaded(
                        legalType = legalType,
                        ideasWithBlockScores = ideasWithBlockScores
                    )
                    lastIdeasLoaded = loaded
                    _state.value = loaded
                }
                is Result.Error -> {
                    _state.value = CompareIdeasState.Error(result.message)
                }
            }
        }
    }

    fun selectTwoIdeas(idea1Id: UUID, idea2Id: UUID) {
        val loaded = lastIdeasLoaded ?: return
        val idea1 = loaded.ideasWithBlockScores.find { it.idea.id == idea1Id }?.idea ?: return
        val idea2 = loaded.ideasWithBlockScores.find { it.idea.id == idea2Id }?.idea ?: return
        if (idea1Id == idea2Id) return

        viewModelScope.launch {
            val questions = surveyRepository.getQuestionsByLegalType(loaded.legalType)
            val maxPerBlock = surveyRepository.getMaxScorePerBlock(loaded.legalType)
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
            _state.value = CompareIdeasState.Comparing(
                idea1 = idea1,
                idea2 = idea2,
                comparisonData = blockComparisons
            )
        }
    }

    fun backToSelectIdeas() {
        lastIdeasLoaded?.let { _state.value = it }
    }

    fun backToSelectLegalType() {
        _state.value = CompareIdeasState.NoSelection
        lastIdeasLoaded = null
    }
}

sealed class CompareIdeasState {
    data object NoSelection : CompareIdeasState()
    data object Loading : CompareIdeasState()
    data class IdeasLoaded(
        val legalType: LegalType,
        val ideasWithBlockScores: List<IdeaWithBlockScores>
    ) : CompareIdeasState()
    data class Comparing(
        val idea1: Idea,
        val idea2: Idea,
        val comparisonData: List<BlockComparison>
    ) : CompareIdeasState()
    data class Error(val message: String) : CompareIdeasState()
}
