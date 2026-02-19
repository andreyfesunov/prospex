package org.prospex.presentation.ideas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        val blockTitleResIds = listOf(
            R.string.block_1_title, R.string.block_2_title, R.string.block_3_title,
            R.string.block_4_title, R.string.block_5_title
        )
        report.blockScores.forEach { blockScore ->
            val title = getString(blockTitleResIds.getOrNull(blockScore.blockOrder - 1) ?: R.string.block_1_title)
            val maxScore = blockScore.maxScore.coerceAtLeast(1)
            val percent = (blockScore.score * 100) / maxScore
            val (colorRes, levelRes) = when {
                percent >= 90 -> R.color.score_excellent to R.string.score_level_excellent
                percent >= 70 -> R.color.score_very_good to R.string.score_level_very_good
                percent >= 50 -> R.color.score_good to R.string.score_level_good
                percent >= 30 -> R.color.score_satisfactory to R.string.score_level_satisfactory
                else -> R.color.score_poor to R.string.score_level_poor
            }
            val line = TextView(requireContext()).apply {
                text = getString(R.string.idea_report_block_score, title, blockScore.score, blockScore.maxScore) +
                    " (" + getString(levelRes) + ")"
                setTextColor(ContextCompat.getColor(context, colorRes))
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
