package com.fok.quizappproject.data.Model

data class Quiz(
    val id: String ="",
    val QuizId: String = "",
    val title: String = "",
    val csv: String = "",
    val correctAns: Int = 0,
    val totalQuestion: Int = 0,
    val titles:List<String>,
    val options: List<String>,
    val answers: List<String>,
    val createdWith: String = "",
    val time: Long
) {
    fun toHashMap():HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "QuizId" to QuizId,
            "title" to title,
            "csv" to csv,
            "correctAns" to correctAns,
            "totalQuestion" to totalQuestion,
            "titles" to titles,
            "options" to options,
            "answers" to answers,
            "createdWith" to createdWith,
            "time" to time

        )
    }


    companion object {

        fun fromHashMap(hash: Map<String, Any>): Quiz {
            return Quiz(
                id = hash["id"].toString(),
                QuizId = hash["QuizId"].toString(),
                title = hash["title"].toString(),
                csv = hash["csv"].toString(),
                titles = (hash["titles"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                options = (hash["options"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                answers = (hash["answers"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                createdWith = hash["createdWith"].toString(),
                time = hash["time"]?.toString()?.toLong() ?: 0
//                correctAns = hash["correctAns"].,
//                totalQuestion = hash["totalQuestion"].toString()
            )
        }
    }
}
