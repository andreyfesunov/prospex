package org.prospex.presentation.support

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.prospex.R
import org.prospex.domain.models.SupportMeasure

class SupportMeasuresAdapter : ListAdapter<SupportMeasure, SupportMeasuresAdapter.MeasureViewHolder>(MeasureDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_support_measure, parent, false)
        return MeasureViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeasureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MeasureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val measureTypeText: TextView = itemView.findViewById(R.id.measureTypeText)
        private val amountText: TextView = itemView.findViewById(R.id.amountText)
        private val legalTypesText: TextView = itemView.findViewById(R.id.legalTypesText)
        private val featuresText: TextView = itemView.findViewById(R.id.featuresText)
        private val coversText: TextView = itemView.findViewById(R.id.coversText)
        private val whereToApplyText: TextView = itemView.findViewById(R.id.whereToApplyText)

        fun bind(measure: SupportMeasure) {
            titleText.text = measure.title
            measureTypeText.text = getMeasureTypeText(measure.measureType)
            amountText.text = "Сумма: ${measure.amount}"
            legalTypesText.text = "Для кого: ${measure.legalTypes.joinToString(", ") { getLegalTypeText(it) }}"
            featuresText.text = "Особенности: ${measure.features}"
            coversText.text = "Что покрывает: ${measure.covers}"
            whereToApplyText.text = "Куда обращаться: ${measure.whereToApply}"
        }

        private fun getMeasureTypeText(measureType: org.prospex.domain.models.MeasureType): String {
            return when (measureType) {
                org.prospex.domain.models.MeasureType.Grant -> "💎 Грант"
                org.prospex.domain.models.MeasureType.SocialContract -> "🤝 Социальный контракт"
                org.prospex.domain.models.MeasureType.Loan -> "📈 Займ"
                org.prospex.domain.models.MeasureType.Guarantee -> "🛡️ Гарантия"
                org.prospex.domain.models.MeasureType.Subsidy -> "💸 Субсидия"
                org.prospex.domain.models.MeasureType.NonFinancial -> "🎓 Нефинансовая поддержка"
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

    class MeasureDiffCallback : DiffUtil.ItemCallback<SupportMeasure>() {
        override fun areItemsTheSame(oldItem: SupportMeasure, newItem: SupportMeasure): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SupportMeasure, newItem: SupportMeasure): Boolean {
            return oldItem == newItem
        }
    }
}

