package com.fok.quizappproject.ui.Register.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fok.quizappproject.core.service.AuthService
import com.fok.quizappproject.data.Model.User
import com.fok.quizappproject.data.repo.UserRepo
import com.fok.quizappproject.ui.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel(), RegisterViewModel {
    override fun register(name: String, email: String, pass: String, confirmPass: String, role: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = errorHandler {
                authService.register(email, pass)
            }
            if(user != null) {
                _success.emit("Registered successfully")
                errorHandler {
                    userRepo.addUser(
                        User(name = name, email = email, role = role)
                    )
                }
            }
        }
    }
}