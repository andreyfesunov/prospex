package org.prospex.presentation.ideas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.prospex.R
import org.prospex.databinding.FragmentCompareIdeasBinding
import org.prospex.domain.models.LegalType
import org.prospex.presentation.viewmodels.CompareIdeasState
import org.prospex.presentation.viewmodels.CompareIdeasViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompareIdeasFragment : Fragment() {
    private var _binding: FragmentCompareIdeasBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CompareIdeasViewModel by viewModel()
    private lateinit var adapter: IdeasAdapter

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

        adapter = IdeasAdapter(
            onDetailsClick = { idea ->
                findNavController().navigate(R.id.nav_idea_details, Bundle().apply {
                    putString("ideaId", idea.id.toString())
                })
            },
            onReportClick = { idea ->
                findNavController().navigate(R.id.nav_idea_report, Bundle().apply {
                    putString("ideaId", idea.id.toString())
                })
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is CompareIdeasState.NoSelection -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        binding.emptyText.visibility = View.VISIBLE
                        binding.emptyText.text = getString(R.string.compare_ideas)
                    }
                    is CompareIdeasState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                        binding.emptyText.visibility = View.GONE
                    }
                    is CompareIdeasState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        if (state.ideas.isEmpty()) {
                            binding.emptyText.visibility = View.VISIBLE
                            binding.emptyText.text = "Нет идей для выбранного типа"
                            binding.recyclerView.visibility = View.GONE
                        } else {
                            binding.emptyText.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                            adapter.submitList(state.ideas)
                        }
                    }
                    is CompareIdeasState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
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
