package org.prospex.presentation.entrepreneurship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.prospex.databinding.FragmentEntrepreneurshipFormsBinding
import org.prospex.presentation.viewmodels.EntrepreneurshipFormsState
import org.prospex.presentation.viewmodels.EntrepreneurshipFormsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EntrepreneurshipFormsFragment : Fragment() {
    private var _binding: FragmentEntrepreneurshipFormsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EntrepreneurshipFormsViewModel by viewModel()
    private lateinit var adapter: EntrepreneurshipFormAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntrepreneurshipFormsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = EntrepreneurshipFormAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        setupObservers()
        viewModel.loadForms()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is EntrepreneurshipFormsState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                    }
                    is EntrepreneurshipFormsState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        adapter.submitList(state.forms.toList())
                    }
                    is EntrepreneurshipFormsState.Error -> {
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
