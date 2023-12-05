package com.fok.quizappproject.core.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

interface AuthService {
    suspend fun register(email: String, pass: String): FirebaseUser?
    suspend fun login(email: String, pass: String): FirebaseUser?
    fun getCurrentUser(): FirebaseUser?
    suspend fun resetPassword(email: String)
    fun logout()
}