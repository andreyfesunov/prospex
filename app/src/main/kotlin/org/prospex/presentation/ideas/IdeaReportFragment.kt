package org.prospex.presentation.ideas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.prospex.R
import org.prospex.databinding.FragmentIdeaReportBinding
import org.prospex.presentation.viewmodels.IdeaReportState
import org.prospex.presentation.viewmodels.IdeaReportViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class IdeaReportFragment : Fragment() {
    private var _binding: FragmentIdeaReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: IdeaReportViewModel by viewModel()
    private lateinit var measuresAdapter: IdeaReportMeasuresAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIdeaReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ideaIdString = arguments?.getString("ideaId") ?: run {
            binding.errorText.text = "Идея не указана"
            binding.errorText.visibility = View.VISIBLE
            return
        }
        val ideaId = UUID.fromString(ideaIdString)
        viewModel.loadReport(ideaId)

        measuresAdapter = IdeaReportMeasuresAdapter(onDetailsClick = {
            findNavController().navigate(R.id.nav_support_measures)
        })
        binding.measuresRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.measuresRecyclerView.adapter = measuresAdapter

        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is IdeaReportState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                    }
                    is IdeaReportState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        displayReport(state.report)
                    }
                    is IdeaReportState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.text = state.message
                        binding.errorText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun displayReport(report: org.prospex.presentation.viewmodels.IdeaReportUi) {
        binding.titleText.text = report.idea.title
        binding.totalScoreText.text = getString(R.string.idea_report_total_score, report.totalScore)

        binding.blocksContainer.removeAllViews()
        report.blockScores.forEach { blockScore ->
            val line = TextView(requireContext()).apply {
                text = getString(R.string.idea_report_block_score, blockScore.blockOrder, blockScore.score)
                textSize = 14f
                setPadding(0, 4, 0, 4)
            }
            binding.blocksContainer.addView(line)
        }

        measuresAdapter.submitList(report.measures)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
