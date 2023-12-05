package com.fok.quizappproject.ui.DashBoard.viewModel

import com.fok.quizappproject.data.Model.Quiz
import kotlinx.coroutines.flow.StateFlow

interface DashBoardViewModel {
    val quiz: StateFlow<List<Quiz>>

    fun getQuiz()

    fun logout()

    fun getCurrentUser()
}