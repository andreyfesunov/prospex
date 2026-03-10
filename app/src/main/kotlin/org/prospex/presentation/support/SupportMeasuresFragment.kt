package org.prospex.presentation.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.prospex.R
import org.prospex.databinding.FragmentSupportMeasuresBinding
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.MeasureType
import org.prospex.presentation.viewmodels.SupportMeasuresViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SupportMeasuresFragment : Fragment() {
    private var _binding: FragmentSupportMeasuresBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SupportMeasuresViewModel by viewModel()
    private lateinit var adapter: SupportMeasuresAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupportMeasuresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SupportMeasuresAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        val legalTypeLabels = listOf(getString(R.string.filter_all)) +
            LegalType.entries.map { getLegalTypeText(it) }
        binding.legalTypeFilterSpinner.adapter = android.widget.ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            legalTypeLabels
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        binding.legalTypeFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val legalType = if (position == 0) null else LegalType.entries[position - 1]
                viewModel.setLegalTypeFilter(legalType)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val measureTypeLabels = listOf(
            getString(R.string.filter_all),
            getString(R.string.measure_type_grant),
            getString(R.string.measure_type_social_contract),
            getString(R.string.measure_type_loan),
            getString(R.string.measure_type_guarantee),
            getString(R.string.measure_type_subsidy),
            getString(R.string.measure_type_non_financial)
        )
        binding.measureTypeFilterSpinner.adapter = android.widget.ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            measureTypeLabels
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        binding.measureTypeFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val measureType = if (position == 0) null else MeasureType.entries[position - 1]
                viewModel.setMeasureTypeFilter(measureType)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        setupObservers()
        viewModel.loadMeasures()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.measuresState.collect { state ->
                when (state) {
                    is org.prospex.presentation.viewmodels.SupportMeasuresState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                    }
                    is org.prospex.presentation.viewmodels.SupportMeasuresState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        adapter.submitList(state.measures.toList())
                    }
                    is org.prospex.presentation.viewmodels.SupportMeasuresState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.text = state.message
                        binding.errorText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun getLegalTypeText(legalType: LegalType): String = when (legalType) {
        LegalType.SelfEmployed -> "Самозанятый"
        LegalType.IndividualEntrepreneur -> "ИП"
        LegalType.PersonalSubsidiaryFarm -> "ЛПХ"
        LegalType.LLC -> "ООО"
        LegalType.SocialEntrepreneur -> "Социальный предприниматель"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

