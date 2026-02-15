package org.prospex.presentation.legaltype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.prospex.R
import org.prospex.databinding.FragmentLegalTypeSurveyBinding
import org.prospex.domain.models.LegalTypeSurveyOption
import org.prospex.domain.models.LegalTypeSurveyQuestion
import org.prospex.presentation.viewmodels.LegalTypeSurveyState
import org.prospex.presentation.viewmodels.LegalTypeSurveyViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class LegalTypeSurveyFragment : Fragment() {
    private var _binding: FragmentLegalTypeSurveyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LegalTypeSurveyViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLegalTypeSurveyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadSurvey()

        binding.submitButton.setOnClickListener {
            val recommended = viewModel.submitSurvey()
            if (recommended != null) {
                findNavController().navigate(
                    R.id.nav_legal_type_recommendation,
                    Bundle().apply { putString("recommendedLegalType", recommended.name) }
                )
            } else {
                binding.errorText.text = "Ответьте на все вопросы"
                binding.errorText.visibility = View.VISIBLE
            }
        }

        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is LegalTypeSurveyState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                    }
                    is LegalTypeSurveyState.Questions -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        binding.questionsContainer.removeAllViews()
                        state.questionsWithOptions.forEach { qWithOpts ->
                            addQuestionView(qWithOpts.question, qWithOpts.options)
                        }
                    }
                    is LegalTypeSurveyState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.text = state.message
                        binding.errorText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun addQuestionView(question: LegalTypeSurveyQuestion, options: List<LegalTypeSurveyOption>) {
        val card = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 24, 0, 24)
        }
        val questionText = TextView(requireContext()).apply {
            text = question.text
            textSize = 16f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 0, 0, 12)
        }
        card.addView(questionText)
        val radioGroup = RadioGroup(requireContext()).apply {
            options.forEach { option ->
                val radio = RadioButton(requireContext()).apply {
                    id = View.generateViewId()
                    text = option.text
                    setOnClickListener { viewModel.selectOption(question.id, option) }
                }
                addView(radio)
            }
        }
        card.addView(radioGroup)
        binding.questionsContainer.addView(card)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
