package com.fok.quizappproject.data.repo

import com.fok.quizappproject.data.Model.Quiz
import com.fok.quizappproject.data.Model.QuizQuestion

interface QuizQuestionRepo {

    suspend fun getQuizQuestionsByQuizId(quizId: String): QuizQuestion?

}