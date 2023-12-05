package com.fok.quizappproject.data.repo

import com.fok.quizappproject.data.Model.User

interface UserRepo {
    suspend fun addUser(user: User)
    suspend fun getUser(uid: String): User?
    suspend fun removeUser()
    suspend fun updateUser(user: User)
}