package org.prospex.presentation.ideas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.prospex.databinding.FragmentCreateIdeaBinding
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.QuestionType
import org.prospex.presentation.viewmodels.CreateIdeaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateIdeaFragment : Fragment() {
    private var _binding: FragmentCreateIdeaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateIdeaViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateIdeaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.legalTypeSpinner.adapter = android.widget.ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            LegalType.entries.map { getLegalTypeText(it) }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.loadQuestionsButton.setOnClickListener {
            val selectedPosition = binding.legalTypeSpinner.selectedItemPosition
            if (selectedPosition >= 0) {
                val legalType = LegalType.entries[selectedPosition]
                viewModel.loadQuestions(legalType)
            }
        }

        binding.createButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            if (title.isNotBlank() && description.isNotBlank()) {
                viewModel.createIdea(title, description)
            } else {
                binding.errorText.text = "Заполните все поля"
                binding.errorText.visibility = View.VISIBLE
            }
        }

        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.questions.collect { questions ->
                binding.questionsContainer.removeAllViews()
                questions.forEach { questionWithOptions ->
                    addQuestionView(questionWithOptions)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.createIdeaState.collect { state ->
                when (state) {
                    is org.prospex.presentation.viewmodels.CreateIdeaState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                    }
                    is org.prospex.presentation.viewmodels.CreateIdeaState.Loaded -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is org.prospex.presentation.viewmodels.CreateIdeaState.Creating -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                    }
                    is org.prospex.presentation.viewmodels.CreateIdeaState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        viewModel.resetState()
                        findNavController().popBackStack()
                    }
                    is org.prospex.presentation.viewmodels.CreateIdeaState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.text = state.message
                        binding.errorText.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun addQuestionView(questionWithOptions: org.prospex.presentation.viewmodels.QuestionWithOptions) {
        val question = questionWithOptions.question
        val options = questionWithOptions.options
        val selectedOptions = viewModel.selectedOptions.value[question.id] ?: emptySet()

        val questionLayout = android.widget.LinearLayout(requireContext()).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16)
            }
        }

        val questionText = android.widget.TextView(requireContext()).apply {
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

        if (question.type == QuestionType.Radio) {
            val radioGroup = RadioGroup(requireContext()).apply {
                orientation = RadioGroup.VERTICAL
            }
            options.forEach { option ->
                val radioButton = RadioButton(requireContext()).apply {
                    text = option.text
                    id = View.generateViewId()
                    isChecked = selectedOptions.contains(option.id)
                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            viewModel.selectOption(question.id, option.id, false)
                        }
                    }
                }
                radioGroup.addView(radioButton)
            }
            questionLayout.addView(radioGroup)
        } else {
            options.forEach { option ->
                val checkBox = CheckBox(requireContext()).apply {
                    text = option.text
                    isChecked = selectedOptions.contains(option.id)
                    setOnCheckedChangeListener { _, isChecked ->
                        viewModel.selectOption(question.id, option.id, true)
                    }
                }
                questionLayout.addView(checkBox)
            }
        }

        binding.questionsContainer.addView(questionLayout)
    }

    private fun getLegalTypeText(legalType: LegalType): String {
        return when (legalType) {
            LegalType.SelfEmployed -> "Самозанятый"
            LegalType.IndividualEntrepreneur -> "ИП"
            LegalType.PersonalSubsidiaryFarm -> "ЛПХ"
            LegalType.LLC -> "ООО"
            LegalType.SocialEntrepreneur -> "Социальный предприниматель"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

