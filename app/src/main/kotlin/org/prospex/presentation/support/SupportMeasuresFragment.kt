package org.prospex.presentation.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.prospex.databinding.FragmentSupportMeasuresBinding
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

