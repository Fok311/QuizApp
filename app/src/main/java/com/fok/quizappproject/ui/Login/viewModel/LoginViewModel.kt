package com.fok.quizappproject.ui.Login.viewModel

import com.fok.quizappproject.data.Model.User

interface LoginViewModel {
    fun login(email: String, pass: String)
    fun getCurrentUser()
}