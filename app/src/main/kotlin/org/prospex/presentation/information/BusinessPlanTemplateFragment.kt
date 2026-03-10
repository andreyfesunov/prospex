package org.prospex.presentation.information

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import org.prospex.R
import org.prospex.databinding.FragmentBusinessPlanTemplateBinding
import java.io.File

class BusinessPlanTemplateFragment : Fragment() {

    private var _binding: FragmentBusinessPlanTemplateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessPlanTemplateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openTemplateButton.setOnClickListener { openTemplate() }
    }

    private fun openTemplate() {
        try {
            val fileName = "business_plan_subsidy_czn.docx"
            requireContext().assets.open(fileName).use { input ->
                val outFile = File(requireContext().cacheDir, fileName)
                outFile.outputStream().use { output ->
                    input.copyTo(output)
                }
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.fileprovider",
                    outFile
                )
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(intent, getString(R.string.business_plan_open_button)))
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Ошибка открытия файла: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
