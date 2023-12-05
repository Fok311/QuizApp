package com.fok.quizappproject.ui.DashBoard.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fok.quizappproject.core.service.AuthService
import com.fok.quizappproject.core.service.StorageService
import com.fok.quizappproject.data.Model.Quiz
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo,
    private val authService: AuthService,
    private val userRepo: UserRepo,
    private val storageService: StorageService
) : BaseViewModel(), DashBoardViewModel {
    private val _user = MutableStateFlow(User(name = "Unknown", email = "Unknown", role = "Unknown"))
    val user: StateFlow<User> = _user

    private val _finish = MutableSharedFlow<Unit>()
    val finish: SharedFlow<Unit> = _finish

    private val _profileUri = MutableStateFlow<Uri?>(null)
    val profileUri: StateFlow<Uri?> = _profileUri


    private val _quizs = MutableStateFlow<List<Quiz>>(
//        Quiz(titles = emptyList(), options = emptyList(), answers = emptyList())
        emptyList()
    )
    override val quiz: StateFlow<List<Quiz>> = _quizs

    init {
        getQuiz()
        getCurrentUser()
        getProfilePicUri()
    }


    override fun logout() {
        authService.logout()
        viewModelScope.launch {
            _finish.emit(Unit)
        }
    }

    override fun getQuiz() {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepo.getQuizs().collect() {
                _quizs.value = it
            }
        }
    }

    override fun getCurrentUser() {
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

    fun updateProfilePic(uri: Uri) {
        user.value.id?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val name = "$it.jpg"
                storageService.addImage(name, uri)
                getProfilePicUri()
            }
        }
    }

    fun getProfilePicUri() {
        viewModelScope.launch(Dispatchers.IO) {
            authService.getCurrentUser()?.uid?.let {
                _profileUri.value = storageService.getImage("$it.jpg")
            }
        }
    }



}