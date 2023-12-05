package com.fok.quizappproject.ui.Register.viewModel

interface RegisterViewModel {
    fun register(name: String, email: String, pass: String, confirmPass: String, role: String)
}