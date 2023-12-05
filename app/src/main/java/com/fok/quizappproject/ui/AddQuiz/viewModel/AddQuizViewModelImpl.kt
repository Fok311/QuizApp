package com.fok.quizappproject.ui.AddQuiz.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fok.quizappproject.core.service.AuthService
import com.fok.quizappproject.data.Model.Quiz
import com.fok.quizappproject.data.Model.QuizQuestion
import com.fok.quizappproject.data.Model.User
import com.fok.quizappproject.data.repo.QuizRepo
import com.fok.quizappproject.data.repo.UserRepo
import com.fok.quizappproject.ui.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQuizViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val quizRepo: QuizRepo,
    private val userRepo: UserRepo
) : BaseViewModel(), AddQuizViewModel {
    private val _finish = MutableSharedFlow<Unit>()
    override val finish: SharedFlow<Unit> = _finish

    private val _quizQuestion = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val quizQuestion: StateFlow<List<QuizQuestion>> = _quizQuestion

    private val _user = MutableStateFlow(User(name = "Unknown", email = "Unknown", role = "Unknown"))
    val user: StateFlow<User> = _user

    init {
        getCurrentUser()
    }

    override fun addQuiz(QuizId: String, title: String,  timer: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            val titles = mutableListOf<String>()
            val options = mutableListOf<String>()
            val answers = mutableListOf<String>()
            _quizQuestion.value.map {
                titles.add(it.question)
                options.add(it.option1)
                options.add(it.option2)
                options.add(it.option3)
                options.add(it.option4)
                answers.add(it.correctAnswer)
            }
            errorHandler {
                quizRepo.addQuiz(
                    Quiz(QuizId = QuizId,title = title, titles = titles, options = options, answers = answers, createdWith = user.value.name, time = timer!!)
                )
            }
            _finish.emit(Unit)
        }
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

    private var timerMinutes: Long = 0

    fun saveTimer(timer: Long) {
        this.timerMinutes = timer
    }

    override fun readCsv(lines: List<String>) {
        viewModelScope.launch {
            try {
                lines.map { line ->
                    val title = line.split(",")
                    QuizQuestion(
                        question = title[0],
                        option1 = title[1],
                        option2 = title[2],
                        option3 = title[3],
                        option4 = title[4],
                        correctAnswer = title[5]
                    )
                }.toList().let {
                    if (it.all { true }) {
                        _quizQuestion.emit(it)
                        _success.emit("CSV Import Successful")
                        Log.d("debugging", "CSV Import Successful: ${it.size} questions imported.")
                    } else {
                        Log.e("debugging", "CSV Import Failed: Null values found in QuizQuestions.")
                    }
                }
            } catch (e: Exception) {
                Log.e("debugging", "Error parsing CSV file: ${e.message}")
            }
        }
    }
}