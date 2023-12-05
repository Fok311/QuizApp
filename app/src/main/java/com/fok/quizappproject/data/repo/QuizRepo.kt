package com.fok.quizappproject.data.repo

import com.fok.quizappproject.data.Model.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepo {

    suspend fun getQuizs(): Flow<List<Quiz>>

    suspend fun addQuiz(quiz: Quiz)

    suspend fun getQuiz(id: String): Quiz?
}