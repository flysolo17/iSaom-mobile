package com.ketchupzzz.isaom.models.submissions

import com.ketchupzzz.isaom.models.subject.activities.Question
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//the goal of this data class is to know what is the most answered choices
//put in pie chart
data class AnswersByQuestion(
    val question: Question,
    val answers: Map<String, String> = hashMapOf(), //submissionID , answer
    val correctAnswers : Int = 0,
    val wrongAnswers : Int = 0,
    val totalSubmissions : Int = 0,
) {
    fun toPieChartData(): List<PieChartSlice> {
        // Count the number of answers for each choice
        val answerCount = question.choices.associateWith { choice ->
            answers.values.count { it == choice }
        }
        // Convert the counts to PieChartSlice objects
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
