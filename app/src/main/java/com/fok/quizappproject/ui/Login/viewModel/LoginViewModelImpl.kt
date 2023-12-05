package com.fok.quizappproject.ui.Login.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fok.quizappproject.core.service.AuthService
import com.fok.quizappproject.data.Model.User
import com.fok.quizappproject.data.repo.UserRepo
import com.fok.quizappproject.ui.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel(), LoginViewModel {

    private val _user = MutableStateFlow(User(name = "Unknown", email = "Unknown", role = "Unknown"))
    val user: StateFlow<User> = _user

    override fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = errorHandler {
                authService.login(email, pass)
            }
            if(result != null) {
                _success.emit("Login successful")
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
    fun resetPassword(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                authService.resetPassword(email)
                _success.emit("Reset password email send successfully !!!")
            }
        }
    }
}