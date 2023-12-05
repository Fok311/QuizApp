package com.fok.quizappproject.data.repo

import com.fok.quizappproject.data.Model.Quiz
import com.fok.quizappproject.data.Model.Result
import kotlinx.coroutines.flow.Flow

interface ResultRepo {
    suspend fun addResult(result: Result)
    suspend fun getResult() : Flow<List<Result>>
}