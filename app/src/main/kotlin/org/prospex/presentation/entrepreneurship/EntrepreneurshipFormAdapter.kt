package org.prospex.presentation.entrepreneurship

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.prospex.R
import org.prospex.domain.models.EntrepreneurshipForm

class EntrepreneurshipFormAdapter : ListAdapter<EntrepreneurshipForm, EntrepreneurshipFormAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entrepreneurship_form, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
        private val featuresText: TextView = itemView.findViewById(R.id.featuresText)
        private val requirementsText: TextView = itemView.findViewById(R.id.requirementsText)

        fun bind(form: EntrepreneurshipForm) {
            titleText.text = form.title
            descriptionText.text = form.description
            featuresText.text = "Особенности: ${form.features}"
            requirementsText.text = "Требования: ${form.requirements}"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<EntrepreneurshipForm>() {
        override fun areItemsTheSame(old: EntrepreneurshipForm, new: EntrepreneurshipForm) = old.id == new.id
        override fun areContentsTheSame(old: EntrepreneurshipForm, new: EntrepreneurshipForm) =
            old.title == new.title && old.legalType == new.legalType && old.description == new.description &&
                old.features == new.features && old.requirements == new.requirements
    }
}
