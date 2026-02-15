package org.prospex.presentation.legaltype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.prospex.R
import org.prospex.databinding.FragmentLegalTypeRecommendationBinding
import org.prospex.domain.models.LegalType
import androidx.navigation.fragment.findNavController

class LegalTypeRecommendationFragment : Fragment() {
    private var _binding: FragmentLegalTypeRecommendationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLegalTypeRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recommendedName = arguments?.getString("recommendedLegalType") ?: return
        val legalType = try {
            LegalType.valueOf(recommendedName)
        } catch (e: Exception) {
            return
        }
        binding.recommendedTypeText.text = getLegalTypeText(legalType)
        binding.createIdeaButton.setOnClickListener {
            findNavController().navigate(
                R.id.nav_create_idea,
                Bundle().apply { putString("suggestedLegalType", legalType.name) }
            )
        }
    }

    private fun getLegalTypeText(legalType: LegalType): String {
        return when (legalType) {
            LegalType.SelfEmployed -> "Самозанятый"
            LegalType.IndividualEntrepreneur -> "ИП"
            LegalType.PersonalSubsidiaryFarm -> "ЛПХ"
            LegalType.LLC -> "ООО"
            LegalType.SocialEntrepreneur -> "Социальный предприниматель"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
