package org.prospex.presentation.ideas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.prospex.R
import org.prospex.databinding.FragmentIdeasListBinding
import org.prospex.domain.models.LegalType
import org.prospex.presentation.viewmodels.IdeasListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class IdeasListFragment : Fragment() {
    private var _binding: FragmentIdeasListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: IdeasListViewModel by viewModel()
    private lateinit var adapter: IdeasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIdeasListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.nav_create_idea)
        }

        val filterLabels = listOf(getString(R.string.filter_all)) +
            LegalType.entries.map { getLegalTypeText(it) }
        binding.legalTypeFilterSpinner.adapter = android.widget.ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            filterLabels
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.legalTypeFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val legalType = if (position == 0) null else LegalType.entries[position - 1]
                viewModel.setLegalTypeFilter(legalType)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.ideasState.collect { state ->
                when (state) {
                    is org.prospex.presentation.viewmodels.IdeasState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                    }
                    is org.prospex.presentation.viewmodels.IdeasState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        adapter.submitList(state.pageModel.items.toList())
                    }
                    is org.prospex.presentation.viewmodels.IdeasState.Error -> {
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

