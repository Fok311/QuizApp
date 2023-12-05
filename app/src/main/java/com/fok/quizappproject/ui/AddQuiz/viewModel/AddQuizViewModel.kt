package com.fok.quizappproject.ui.AddQuiz.viewModel

import android.net.Uri
import com.fok.quizappproject.data.Model.Quiz
import kotlinx.coroutines.flow.SharedFlow

interface AddQuizViewModel {
    val finish: SharedFlow<Unit>
    fun addQuiz(QuizId: String, title: String, timer: Long?)
    fun readCsv(lines:List<String>)
}