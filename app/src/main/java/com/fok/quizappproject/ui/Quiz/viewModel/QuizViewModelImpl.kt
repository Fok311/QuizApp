package com.fok.quizappproject.ui.Quiz.viewModel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fok.quizappproject.core.service.AuthService
import com.fok.quizappproject.data.Model.Quiz
import com.fok.quizappproject.data.Model.Result
import com.fok.quizappproject.data.Model.User
import com.fok.quizappproject.data.repo.QuizRepo
import com.fok.quizappproject.data.repo.ResultRepo
import com.fok.quizappproject.data.repo.UserRepo
import com.fok.quizappproject.ui.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModelImpl @Inject constructor (
    private val quizRepo: QuizRepo,
    private val resultRepo: ResultRepo,
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel(), QuizViewModel {

    private val _quiz: MutableStateFlow<Quiz> = MutableStateFlow(Quiz(titles = emptyList(), options = emptyList(), answers = emptyList(), time = 0))
    val quiz: StateFlow<Quiz> = _quiz

    private val _user = MutableStateFlow(User(name = "Unknown", email = "Unknown", role = "Unknown"))
    val user: StateFlow<User> = _user

    init {
        getCurrentUser()
    }

    fun getQuiz(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                quizRepo.getQuiz(id)
            }?.let {
                _quiz.value = it
            }
        }
    }

    fun countDownTimer() {
//        val countdownTimer = object : CountDownTimer(4000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                // Update the countdown TextView with the remaining time
//                val secondsRemaining = millisUntilFinished / 1000
//                binding.tvCountdown.text = "$secondsRemaining"
//            }
//
//            override fun onFinish() {
//                // Handle what to do when the countdown finishes
//                navigateToNextPage()
//            }
//        }

        // Start the countdown timer
//        countdownTimer.start()
    }

     fun getCurrentUser() {
        val firebaseUser = authService.getCurrentUser()
        Log.d("debugging", firebaseUser?.uid.toString())
        firebaseUser?.let {
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler { userRepo.getUser(it.uid) }?.let {  user ->
                    Log.d("debugging", user.toString())
                    _user.value = user
                }
            }
        }
    }

    fun addResult(result: String, quizId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                resultRepo.addResult(
                    Result(result = result, name = user.value.name, quizId = quizId)
                )
            }

        }
    }

}