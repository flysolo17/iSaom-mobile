package com.ketchupzzz.isaom.models.submissions

import com.ketchupzzz.isaom.models.subject.activities.Question
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Responses(
    val choice : String,
    val totalCount : Int,
    val correct : Int,
    val wrong : Int,
)

fun Question.getResponses(submissions: List<Submissions>): List<Responses> {
    val responsesMap = mutableMapOf<String, Pair<Int, Int>>()
    val correctAnswer = this.answer

    this.choices.forEach { choice ->
        responsesMap[choice] = Pair(0, 0)
    }

    submissions.forEach { submission ->
        val answerSheet: Map<String, String> = submission.answerSheet
        val selectedChoice = answerSheet[this.id]

        if (selectedChoice != null) {
            val isCorrect = (selectedChoice == correctAnswer)
            val (correctCount, wrongCount) = responsesMap[selectedChoice] ?: Pair(0, 0)
            responsesMap[selectedChoice] = if (isCorrect) {
                Pair(correctCount + 1, wrongCount)
            } else {
                Pair(correctCount, wrongCount + 1)
            }
        }
    }
    return responsesMap.map { (choice, counts) ->
        val totalCount = counts.first + counts.second
        Responses(choice = choice,totalCount = totalCount, correct = counts.first, wrong = counts.second)
    }
}



data class AnswersByQuestion(
    val question: Question,
    val answers: Map<String, String> = hashMapOf(),
    val correctAnswers : Int = 0,
    val wrongAnswers : Int = 0,
    val totalSubmissions : Int = 0,
) {
    fun toPieChartData(): List<PieChartSlice> {
        val answerCount = question.choices.associateWith { choice ->
            answers.values.count { it == choice }
        }
        return answerCount.map { (choice, count) ->
            PieChartSlice(label = choice, value = count)
        }
    }
}
data class PieChartSlice(val label: String, val value: Int)


data class Submissions(
    val id : String ? = null,
    val subjectID : String ? = null,
    val studentID : String? = null,
    val activityID : String ? = null,
    val activityName : String ? = null,
    val points : Int = 0,
    val maxPoints : Int  = 0,
    val answerSheet : Map<String,String> = hashMapOf(),
    val createdAt : Date = Date(),
)
fun Date.toIsaomFormat(): String {
    val dateFormat = SimpleDateFormat("MMM d, yyyy 'at' hh:mm a", Locale.getDefault())
    return dateFormat.format(this)
}
