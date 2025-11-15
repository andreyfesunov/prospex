package org.prospex.presentation.ideas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.prospex.R
import org.prospex.databinding.FragmentIdeaDetailsBinding
import org.prospex.presentation.viewmodels.IdeaDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class IdeaDetailsFragment : Fragment() {
    private var _binding: FragmentIdeaDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: IdeaDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIdeaDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ideaIdString = arguments?.getString("ideaId") ?: return
        val ideaId = UUID.fromString(ideaIdString)
        viewModel.loadIdeaDetails(ideaId)

        binding.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.ideaDetailsState.collect { state ->
                when (state) {
                    is org.prospex.presentation.viewmodels.IdeaDetailsState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                    }
                    is org.prospex.presentation.viewmodels.IdeaDetailsState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        displayIdea(state.idea, state.questionsWithAnswers)
                    }
                    is org.prospex.presentation.viewmodels.IdeaDetailsState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.text = state.message
                        binding.errorText.visibility = View.VISIBLE
                    }
                    is org.prospex.presentation.viewmodels.IdeaDetailsState.Deleted -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun displayIdea(
        idea: org.prospex.domain.models.Idea,
        questionsWithAnswers: List<org.prospex.presentation.viewmodels.QuestionWithAnswer>
    ) {
        binding.titleText.text = idea.title
        binding.descriptionText.text = idea.description
        binding.scoreText.text = "Оценка: ${idea.score.value}"
        binding.legalTypeText.text = getLegalTypeText(idea.legalType)

        binding.questionsContainer.removeAllViews()
        questionsWithAnswers.forEach { questionWithAnswer ->
            addQuestionView(questionWithAnswer)
        }
    }

    private fun addQuestionView(questionWithAnswer: org.prospex.presentation.viewmodels.QuestionWithAnswer) {
        val question = questionWithAnswer.question
        val answers = questionWithAnswer.answers

        val questionLayout = android.widget.LinearLayout(requireContext()).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16)
            }
        }

        val questionText = TextView(requireContext()).apply {
            text = question.text
            textSize = 16f
            setTypeface(null, android.graphics.Typeface.BOLD)
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 8)
            }
        }
        questionLayout.addView(questionText)

        if (answers.isNotEmpty()) {
            val answersText = TextView(requireContext()).apply {
                text = answers.joinToString("\n") { "• ${it.text}" }
                textSize = 14f
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.MATCH_PARENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 0, 0, 0)
                }
            }
            questionLayout.addView(answersText)
        } else {
            val noAnswerText = TextView(requireContext()).apply {
                text = "Нет ответа"
                textSize = 14f
                setTypeface(null, android.graphics.Typeface.ITALIC)
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.MATCH_PARENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 0, 0, 0)
                }
            }
            questionLayout.addView(noAnswerText)
        }

        binding.questionsContainer.addView(questionLayout)
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_idea))
            .setMessage(getString(R.string.delete_idea_confirmation))
            .setPositiveButton(getString(android.R.string.yes)) { _, _ ->
                viewModel.deleteIdea()
            }
            .setNegativeButton(getString(android.R.string.no), null)
            .show()
    }

    private fun getLegalTypeText(legalType: org.prospex.domain.models.LegalType): String {
        return when (legalType) {
            org.prospex.domain.models.LegalType.SelfEmployed -> "Самозанятый"
            org.prospex.domain.models.LegalType.IndividualEntrepreneur -> "ИП"
            org.prospex.domain.models.LegalType.PersonalSubsidiaryFarm -> "ЛПХ"
            org.prospex.domain.models.LegalType.LLC -> "ООО"
            org.prospex.domain.models.LegalType.SocialEntrepreneur -> "Социальный предприниматель"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

