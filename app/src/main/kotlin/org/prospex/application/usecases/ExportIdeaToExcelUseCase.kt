package org.prospex.application.usecases

import org.prospex.application.utilities.Result
import org.prospex.application.utilities.UseCase
import org.prospex.domain.models.Idea

/**
 * Produces a two-column table (CSV, semicolon-separated) from idea details.
 * Excel-compatible; columns: label, value.
 */
class ExportIdeaToExcelUseCase : UseCase<ExportIdeaToExcelUseCase.Params, String> {

    data class Params(
        val idea: Idea,
        val legalTypeLabel: String,
        val questionAnswers: List<Pair<String, String>>
    )

    override suspend fun execute(params: Params): Result<String> {
        val rows = mutableListOf<Pair<String, String>>()
        rows.add("Название идеи" to params.idea.title)
        rows.add("Описание идеи" to params.idea.description)
        rows.add("Оценка" to params.idea.score.value.toString())
        rows.add("Тип" to params.legalTypeLabel)
        params.questionAnswers.forEach { (question, answer) ->
            rows.add(question to answer)
        }
        val csv = rows.joinToString("\n") { (a, b) ->
            "${escapeCsv(a)};${escapeCsv(b)}"
        }
        return Result.Success(csv)
    }

    private fun escapeCsv(value: String): String {
        val escaped = value.replace("\"", "\"\"")
        return "\"$escaped\""
    }
}
