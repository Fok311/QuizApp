package com.fok.quizappproject.data.repo

import com.fok.quizappproject.core.service.AuthService
import com.fok.quizappproject.data.Model.Quiz
import com.fok.quizappproject.data.Model.QuizQuestion
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class QuizQuestionRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): QuizQuestionRepo {

    private fun getDbRef(): CollectionReference {
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid?.let {
            db.collection("Quiz/$it/quiz")
        } ?: throw Exception("No authentic user found")
    }


    override suspend fun getQuizQuestionsByQuizId(quizId: String): QuizQuestion? {
        val query = getDbRef().whereEqualTo("quizId", quizId).get().await()

        return if (!query.isEmpty) {
            val doc = query.documents[0]
            doc.toObject(QuizQuestion::class.java)?.copy(id = doc.id)
        } else {
            null
        }
    }
}