package org.prospex.presentation.ideas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.prospex.R
import org.prospex.domain.models.Idea

class IdeasAdapter : ListAdapter<Idea, IdeasAdapter.IdeaViewHolder>(IdeaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_idea, parent, false)
        return IdeaViewHolder(view)
    }

    override fun onBindViewHolder(holder: IdeaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class IdeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
        private val scoreText: TextView = itemView.findViewById(R.id.scoreText)
        private val legalTypeText: TextView = itemView.findViewById(R.id.legalTypeText)

        fun bind(idea: Idea) {
            titleText.text = idea.title
            descriptionText.text = idea.description
            scoreText.text = "Оценка: ${idea.score.value}"
            legalTypeText.text = getLegalTypeText(idea.legalType)
        }

        private fun getLegalTypeText(legalType: org.prospex.domain.models.LegalType): String {
            return when (legalType) {
                org.prospex.domain.models.LegalType.SelfEmployed -> "Самозанятый"
                org.prospex.domain.models.LegalType.IndividualEntrepreneur -> "ИП"
                org.prospex.domain.models.LegalType.PersonalSubsidiaryFarm -> "ЛПХ"
                org.prospex.domain.models.LegalType.LLC -> "ООО"
                org.prospex.domain.models.LegalType.SocialEntrepreneur -> "Социальный предприниматель"
            }
        }
    }

    class IdeaDiffCallback : DiffUtil.ItemCallback<Idea>() {
        override fun areItemsTheSame(oldItem: Idea, newItem: Idea): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Idea, newItem: Idea): Boolean {
            return oldItem == newItem
        }
    }
}

