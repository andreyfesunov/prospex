package org.prospex.presentation.ideas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.prospex.R
import org.prospex.databinding.FragmentCompareIdeasResultBinding
import org.prospex.presentation.viewmodels.BlockComparison
import org.prospex.presentation.viewmodels.CompareIdeasResultState
import org.prospex.presentation.viewmodels.CompareIdeasResultViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class CompareIdeasResultFragment : Fragment() {

    private var _binding: FragmentCompareIdeasResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CompareIdeasResultViewModel by viewModel()

    private val blockTitleResIds = listOf(
        R.string.block_1_title, R.string.block_2_title, R.string.block_3_title,
        R.string.block_4_title, R.string.block_5_title
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompareIdeasResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idea1Id = arguments?.getString("idea1Id")?.let { UUID.fromString(it) }
        val idea2Id = arguments?.getString("idea2Id")?.let { UUID.fromString(it) }
        if (idea1Id == null || idea2Id == null) {
            binding.errorText.text = "Не указаны идеи для сравнения"
            binding.errorText.visibility = View.VISIBLE
            return
        }
        viewModel.load(idea1Id, idea2Id)
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is CompareIdeasResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                        binding.headingText.visibility = View.GONE
                        binding.score1Text.visibility = View.GONE
                        binding.score2Text.visibility = View.GONE
                        binding.analysisTitleText.visibility = View.GONE
                        binding.blocksContainer.visibility = View.GONE
                    }
                    is CompareIdeasResultState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        binding.headingText.visibility = View.VISIBLE
                        binding.score1Text.visibility = View.VISIBLE
                        binding.score2Text.visibility = View.VISIBLE
                        binding.analysisTitleText.visibility = View.VISIBLE
                        binding.blocksContainer.visibility = View.VISIBLE

                        binding.headingText.text = getString(R.string.compare_ideas_heading, state.idea1.title, state.idea2.title)
                        binding.score1Text.text = getString(R.string.compare_ideas_score_line, state.idea1.title, state.idea1.score.value.toInt())
                        binding.score2Text.text = getString(R.string.compare_ideas_score_line, state.idea2.title, state.idea2.score.value.toInt())
                        buildBlocks(state.idea1.title, state.idea2.title, state.comparisonData)
                    }
                    is CompareIdeasResultState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.headingText.visibility = View.GONE
                        binding.score1Text.visibility = View.GONE
                        binding.score2Text.visibility = View.GONE
                        binding.analysisTitleText.visibility = View.GONE
                        binding.blocksContainer.visibility = View.GONE
                        binding.errorText.text = state.message
                        binding.errorText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun buildBlocks(idea1Title: String, idea2Title: String, data: List<BlockComparison>) {
        val container = binding.blocksContainer
        container.removeAllViews()
        val ctx = requireContext()

        fun levelAndColor(score: Int, maxScore: Int): Pair<Int, Int> {
            val max = maxScore.coerceAtLeast(1)
            val percent = (score * 100) / max
            val levelRes = when {
                percent >= 90 -> R.string.score_level_excellent
                percent >= 70 -> R.string.score_level_very_good
                percent >= 50 -> R.string.score_level_good
                percent >= 30 -> R.string.score_level_satisfactory
                else -> R.string.score_level_poor
            }
            val colorRes = when {
                percent >= 90 -> R.color.score_excellent
                percent >= 70 -> R.color.score_very_good
                percent >= 50 -> R.color.score_good
                percent >= 30 -> R.color.score_satisfactory
                else -> R.color.score_poor
            }
            return levelRes to colorRes
        }

        data.forEach { block ->
            val blockTitleRes = blockTitleResIds.getOrNull(block.blockOrder - 1) ?: R.string.block_1_title
            val blockTitle = getString(blockTitleRes)

            val card = com.google.android.material.card.MaterialCardView(ctx).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = dp(12) }
                setContentPadding(dp(16), dp(12), dp(16), dp(12))
                radius = 8f
                cardElevation = 4f
            }
            val cardContent = LinearLayout(ctx).apply {
                orientation = LinearLayout.VERTICAL
            }

            cardContent.addView(TextView(ctx).apply {
                text = blockTitle
                textSize = 14f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setPadding(0, 0, 0, dp(8))
            })

            val (level1Res, color1Res) = levelAndColor(block.leftBlockScore, block.maxScore)
            val line1 = TextView(ctx).apply {
                text = "$idea1Title: ${getString(R.string.idea_report_block_score_short, block.leftBlockScore, block.maxScore)} ${getString(level1Res)}"
                setTextColor(ContextCompat.getColor(ctx, color1Res))
                textSize = 14f
                setPadding(0, 0, 0, dp(4))
            }
            cardContent.addView(line1)

            val (level2Res, color2Res) = levelAndColor(block.rightBlockScore, block.maxScore)
            val line2 = TextView(ctx).apply {
                text = "$idea2Title: ${getString(R.string.idea_report_block_score_short, block.rightBlockScore, block.maxScore)} ${getString(level2Res)}"
                setTextColor(ContextCompat.getColor(ctx, color2Res))
                textSize = 14f
            }
            cardContent.addView(line2)

            card.addView(cardContent)
            container.addView(card)
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
