package org.prospex.presentation.ideas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.prospex.R
import org.prospex.databinding.FragmentIdeasListBinding
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

        adapter = IdeasAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.nav_create_idea)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

