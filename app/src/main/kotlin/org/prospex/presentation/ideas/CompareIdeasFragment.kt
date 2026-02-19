package org.prospex.presentation.ideas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.prospex.R
import org.prospex.databinding.FragmentCompareIdeasBinding
import org.prospex.domain.models.LegalType
import org.prospex.presentation.viewmodels.CompareIdeasState
import org.prospex.presentation.viewmodels.CompareIdeasViewModel
import org.prospex.presentation.viewmodels.IdeaWithBlockScores
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompareIdeasFragment : Fragment() {

    private var _binding: FragmentCompareIdeasBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CompareIdeasViewModel by viewModel()

    private var ideasForSpinners: List<IdeaWithBlockScores> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompareIdeasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val legalTypes = LegalType.entries.map { getLegalTypeText(it) }
        binding.legalTypeSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            legalTypes
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.legalTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selectLegalType(LegalType.entries[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.compareButton.setOnClickListener {
            val pos1 = binding.idea1Spinner.selectedItemPosition
            val pos2 = binding.idea2Spinner.selectedItemPosition
            if (ideasForSpinners.getOrNull(pos1) != null && ideasForSpinners.getOrNull(pos2) != null && pos1 != pos2) {
                val id1 = ideasForSpinners[pos1].idea.id
                val id2 = ideasForSpinners[pos2].idea.id
                findNavController().navigate(R.id.nav_compare_ideas_result, Bundle().apply {
                    putString("idea1Id", id1.toString())
                    putString("idea2Id", id2.toString())
                })
            }
        }

        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is CompareIdeasState.NoSelection -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        binding.step1Container.visibility = View.VISIBLE
                        binding.step2Container.visibility = View.GONE
                        binding.step3Container.visibility = View.GONE
                        binding.emptyText.visibility = View.VISIBLE
                        binding.emptyText.text = getString(R.string.compare_ideas)
                    }
                    is CompareIdeasState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                        binding.emptyText.visibility = View.GONE
                        binding.step2Container.visibility = View.GONE
                        binding.step3Container.visibility = View.GONE
                    }
                    is CompareIdeasState.IdeasLoaded -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        binding.step1Container.visibility = View.VISIBLE
                        ideasForSpinners = state.ideasWithBlockScores
                        if (state.ideasWithBlockScores.isEmpty()) {
                            binding.step2Container.visibility = View.GONE
                            binding.emptyText.visibility = View.VISIBLE
                            binding.emptyText.text = "Нет идей для выбранного типа"
                        } else {
                            binding.emptyText.visibility = View.GONE
                            binding.step2Container.visibility = View.VISIBLE
                            val titles = state.ideasWithBlockScores.map { it.idea.title }
                            binding.idea1Spinner.adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                titles
                            ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                            binding.idea2Spinner.adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                titles
                            ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                            if (state.ideasWithBlockScores.size >= 2) {
                                binding.idea1Spinner.setSelection(0)
                                binding.idea2Spinner.setSelection(1)
                                binding.compareButton.isEnabled = true
                            } else {
                                binding.compareButton.isEnabled = false
                            }
                        }
                    }
                    is CompareIdeasState.Comparing -> {
                        // Navigate to result screen instead; this branch is unreachable in normal flow
                    }
                    is CompareIdeasState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.step2Container.visibility = View.GONE
                        binding.step3Container.visibility = View.GONE
                        binding.emptyText.visibility = View.GONE
                        binding.errorText.text = state.message
                        binding.errorText.visibility = View.VISIBLE
                    }
                }
            }
        }
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
