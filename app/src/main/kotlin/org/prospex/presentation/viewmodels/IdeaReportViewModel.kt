package org.prospex.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.prospex.domain.models.Idea
import org.prospex.domain.models.SupportMeasure
import org.prospex.domain.repositories.IIdeaRepository
import org.prospex.domain.repositories.ISupportMeasureRepository
import org.prospex.domain.repositories.ISurveyRepository
import java.util.UUID

data class BlockScore(val blockOrder: Int, val score: Int)

data class IdeaReportUi(
    val idea: Idea,
    val totalScore: Int,
    val blockScores: List<BlockScore>,
    val measures: List<SupportMeasure>
)

class IdeaReportViewModel(
    private val ideaRepository: IIdeaRepository,
    private val surveyRepository: ISurveyRepository,
    private val supportMeasureRepository: ISupportMeasureRepository
) : ViewModel() {

    private val _state = MutableStateFlow<IdeaReportState>(IdeaReportState.Loading)
    val state: StateFlow<IdeaReportState> = _state.asStateFlow()

    fun loadReport(ideaId: UUID) {
        viewModelScope.launch {
            _state.value = IdeaReportState.Loading
            try {
                val idea = ideaRepository.get(ideaId)
                    ?: run {
                        _state.value = IdeaReportState.Error("Идея не найдена")
                        return@launch
                    }
                val surveyResponse = surveyRepository.getSurveyResponse(ideaId)
                val questions = surveyRepository.getQuestionsByLegalType(idea.legalType)
                val optionIds = surveyResponse?.optionIds?.toSet() ?: emptySet()

                val blockScores = (1..5).map { blockOrder ->
                    val blockScore = questions
                        .filter { it.blockOrder == blockOrder }
                        .flatMap { q -> surveyRepository.getOptionsByQuestionId(q.id).toList() }
                        .filter { optionIds.contains(it.id) }
                        .sumOf { it.score.value.toInt() }
                    BlockScore(blockOrder, blockScore)
                }

                val measures = supportMeasureRepository.getByLegalType(idea.legalType).toList()

                _state.value = IdeaReportState.Success(
                    IdeaReportUi(
                        idea = idea,
                        totalScore = idea.score.value.toInt(),
                        blockScores = blockScores,
                        measures = measures
                    )
                )
            } catch (e: Exception) {
                _state.value = IdeaReportState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }
}

sealed class IdeaReportState {
    data object Loading : IdeaReportState()
    data class Success(val report: IdeaReportUi) : IdeaReportState()
    data class Error(val message: String) : IdeaReportState()
}
