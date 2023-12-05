package com.fok.quizappproject.ui.JoinQuiz.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fok.quizappproject.data.Model.Quiz
import com.fok.quizappproject.data.Model.QuizQuestion
import com.fok.quizappproject.data.repo.QuizQuestionRepo
import com.fok.quizappproject.data.repo.QuizRepo
import com.fok.quizappproject.ui.base.viewModel.BaseViewModel
import com.fok.quizappproject.ui.home.viewModel.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinQuizViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo
) : BaseViewModel(), JoinQuizViewModel {


    private val _quiz: MutableStateFlow<Quiz> = MutableStateFlow(Quiz(titles = emptyList(), options = emptyList(), answers = emptyList(), time = 0))
    val quiz: StateFlow<Quiz> = _quiz

    fun getQuiz(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
               quizRepo.getQuiz(id)
            }?.let {
                _quiz.value = it
            }
        }
    }

}