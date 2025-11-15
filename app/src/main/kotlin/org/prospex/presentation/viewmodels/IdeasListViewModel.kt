package org.prospex.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.prospex.application.usecases.DeleteIdeaUseCase
import org.prospex.application.usecases.GetIdeasUseCase
import org.prospex.application.utilities.Result
import org.prospex.domain.models.Idea
import org.prospex.domain.models.PageModel
import org.prospex.domain.value_objects.Positive
import java.util.UUID

class IdeasListViewModel(
    private val getIdeasUseCase: GetIdeasUseCase,
    private val deleteIdeaUseCase: DeleteIdeaUseCase
) : ViewModel() {

    private val _ideasState = MutableStateFlow<IdeasState>(IdeasState.Loading)
    val ideasState: StateFlow<IdeasState> = _ideasState.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    private val pageSize = 10

    init {
        loadIdeas()
    }

    fun loadIdeas() {
        viewModelScope.launch {
            _ideasState.value = IdeasState.Loading

            val result = getIdeasUseCase.execute(
                GetIdeasUseCase.Params(
                    page = Positive(_currentPage.value.toUInt()),
                    pageSize = Positive(pageSize.toUInt())
                )
            )

            when (result) {
                is Result.Success -> {
                    _ideasState.value = IdeasState.Success(result.data)
                }
                is Result.Error -> {
                    _ideasState.value = IdeasState.Error(result.message)
                }
            }
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            val currentState = _ideasState.value
            if (currentState is IdeasState.Success) {
                val nextPage = _currentPage.value + 1
                _currentPage.value = nextPage

                val result = getIdeasUseCase.execute(
                    GetIdeasUseCase.Params(
                        page = Positive(nextPage.toUInt()),
                        pageSize = Positive(pageSize.toUInt())
                    )
                )

                when (result) {
                    is Result.Success -> {
                        val combinedItems = currentState.pageModel.items + result.data.items
                        _ideasState.value = IdeasState.Success(
                            PageModel(
                                items = combinedItems,
                                page = result.data.page,
                                pageSize = result.data.pageSize,
                                totalItems = result.data.totalItems
                            )
                        )
                    }
                    is Result.Error -> {
                        _ideasState.value = IdeasState.Error(result.message)
                    }
                }
            }
        }
    }

    fun refresh() {
        _currentPage.value = 1
        loadIdeas()
    }

    fun deleteIdea(ideaId: UUID) {
        viewModelScope.launch {
            val result = deleteIdeaUseCase.execute(ideaId)
            if (result is Result.Success) {
                refresh()
            }
        }
    }
}

sealed class IdeasState {
    data object Loading : IdeasState()
    data class Success(val pageModel: PageModel<Idea>) : IdeasState()
    data class Error(val message: String) : IdeasState()
}

