package com.fok.quizappproject.data.Model

data class QuizQuestion(
    val id: String ="",
//    val quizName: String = "",
//    val quizId: String = "",
    val question: String = "",
    val option1: String = "",
    val option2: String = "",
    val option3: String = "",
    val option4: String = "",
    val correctAnswer: String = ""
) {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "question" to question,
            "option1" to option1,
            "option2" to option2,
            "option3" to option3,
            "option4" to option4,
            "correctAnswer" to correctAnswer
        )
    }
}