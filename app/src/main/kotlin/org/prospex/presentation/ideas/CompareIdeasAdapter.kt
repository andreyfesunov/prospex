package org.prospex.presentation.ideas

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.widget.PopupMenu
import org.prospex.R
import org.prospex.domain.models.Idea
import org.prospex.presentation.viewmodels.BlockScore
import org.prospex.presentation.viewmodels.IdeaWithBlockScores

class CompareIdeasAdapter(
    private val onDetailsClick: (Idea) -> Unit,
    private val onReportClick: (Idea) -> Unit
) : ListAdapter<IdeaWithBlockScores, CompareIdeasAdapter.CompareIdeaViewHolder>(CompareIdeaDiffCallback()) {

    private val blockTitleResIds = listOf(
        R.string.block_1_title, R.string.block_2_title, R.string.block_3_title,
        R.string.block_4_title, R.string.block_5_title
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompareIdeaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_compare_idea, parent, false)
        return CompareIdeaViewHolder(view, onDetailsClick, onReportClick, blockTitleResIds)
    }

    override fun onBindViewHolder(holder: CompareIdeaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CompareIdeaViewHolder(
        itemView: View,
        private val onDetailsClick: (Idea) -> Unit,
        private val onReportClick: (Idea) -> Unit,
        private val blockTitleResIds: List<Int>
    ) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val totalScoreText: TextView = itemView.findViewById(R.id.totalScoreText)
        private val blocksContainer: android.widget.LinearLayout = itemView.findViewById(R.id.blocksContainer)
        private val overflowButton: View = itemView.findViewById(R.id.overflowButton)

        fun bind(item: IdeaWithBlockScores) {
            val idea = item.idea
            titleText.text = idea.title
            totalScoreText.text = itemView.context.getString(R.string.idea_report_total_score, idea.score.value.toInt())
            overflowButton.setOnClickListener { v ->
                PopupMenu(v.context, v).apply {
                    MenuInflater(v.context).inflate(R.menu.menu_idea_overflow, menu)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.action_idea_details -> { onDetailsClick(idea); true }
                            R.id.action_idea_report -> { onReportClick(idea); true }
                            else -> false
                        }
                    }
                    show()
                }
            }

            blocksContainer.removeAllViews()
            item.blockScores.forEach { blockScore ->
                val title = itemView.context.getString(
                    blockTitleResIds.getOrNull(blockScore.blockOrder - 1) ?: R.string.block_1_title
                )
                val maxScore = blockScore.maxScore.coerceAtLeast(1)
                val percent = (blockScore.score * 100) / maxScore
                val colorRes = when {
                    percent >= 90 -> R.color.score_excellent
                    percent >= 70 -> R.color.score_very_good
                    percent >= 50 -> R.color.score_good
                    percent >= 30 -> R.color.score_satisfactory
                    else -> R.color.score_poor
                }
                val line = TextView(itemView.context).apply {
                    text = itemView.context.getString(
                        R.string.idea_report_block_score,
                        title,
                        blockScore.score,
                        blockScore.maxScore
                    )
                    setTextColor(ContextCompat.getColor(context, colorRes))
                    textSize = 12f
                    setPadding(0, 2, 0, 2)
                }
                blocksContainer.addView(line)
            }
        }
    }

    class CompareIdeaDiffCallback : DiffUtil.ItemCallback<IdeaWithBlockScores>() {
        override fun areItemsTheSame(oldItem: IdeaWithBlockScores, newItem: IdeaWithBlockScores): Boolean {
            return oldItem.idea.id == newItem.idea.id
        }

        override fun areContentsTheSame(oldItem: IdeaWithBlockScores, newItem: IdeaWithBlockScores): Boolean {
            if (oldItem.idea != newItem.idea) return false
            if (oldItem.blockScores != newItem.blockScores) return false
            return true
        }
    }
}
