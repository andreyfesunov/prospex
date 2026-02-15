package org.prospex.presentation.ideas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import org.prospex.R
import org.prospex.domain.models.SupportMeasure

class IdeaReportMeasuresAdapter(
    private val onDetailsClick: () -> Unit
) : ListAdapter<SupportMeasure, IdeaReportMeasuresAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_idea_report_measure, parent, false)
        return ViewHolder(view, onDetailsClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        itemView: View,
        private val onDetailsClick: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.measureTitleText)
        private val measureTypeText: TextView = itemView.findViewById(R.id.measureTypeText)
        private val legalTypesText: TextView = itemView.findViewById(R.id.legalTypesText)
        private val detailsButton: MaterialButton = itemView.findViewById(R.id.detailsButton)

        fun bind(measure: SupportMeasure) {
            titleText.text = measure.title
            measureTypeText.text = getMeasureTypeText(measure.measureType)
            legalTypesText.text = "Для кого: ${measure.legalTypes.joinToString(", ") { getLegalTypeText(it) }}"
            detailsButton.setOnClickListener { onDetailsClick() }
        }

        private fun getMeasureTypeText(measureType: org.prospex.domain.models.MeasureType): String {
            return when (measureType) {
                org.prospex.domain.models.MeasureType.Grant -> "Грант"
                org.prospex.domain.models.MeasureType.SocialContract -> "Социальный контракт"
                org.prospex.domain.models.MeasureType.Loan -> "Займ"
                org.prospex.domain.models.MeasureType.Guarantee -> "Гарантия"
                org.prospex.domain.models.MeasureType.Subsidy -> "Субсидия"
                org.prospex.domain.models.MeasureType.NonFinancial -> "Нефинансовая поддержка"
            }
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

    class DiffCallback : DiffUtil.ItemCallback<SupportMeasure>() {
        override fun areItemsTheSame(old: SupportMeasure, new: SupportMeasure) = old.id == new.id
        override fun areContentsTheSame(old: SupportMeasure, new: SupportMeasure) =
            old.title == new.title && old.measureType == new.measureType &&
                old.legalTypes.contentEquals(new.legalTypes) && old.amount == new.amount &&
                old.features == new.features && old.covers == new.covers && old.whereToApply == new.whereToApply
    }
}
