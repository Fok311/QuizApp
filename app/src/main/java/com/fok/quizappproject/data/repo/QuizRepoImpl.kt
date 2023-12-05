package com.fok.quizappproject.data.repo

import android.util.Log
import com.fok.quizappproject.core.service.AuthService
import com.fok.quizappproject.data.Model.Quiz
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class QuizRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): QuizRepo {

    private fun getDbRef(): CollectionReference {
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid?.let {
            db.collection("Quiz")
        } ?: throw Exception("No authentic user found")
    }

    override suspend fun getQuizs() = callbackFlow {
        val listener = getDbRef().addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val quiz = mutableListOf<Quiz>()
            value?.documents?.let { docs ->
                for (doc in docs) {
                    doc.data?.let {
                        it["id"] = doc.id
                        quiz.add(Quiz.fromHashMap(it))
                    }
                }
                trySend(quiz)
            }
        }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun addQuiz(quiz: Quiz) {
        getDbRef().document(quiz.QuizId).set(quiz.toHashMap()).await()
    }

    override suspend fun getQuiz(id: String): Quiz? {
        val querySnapshot = getDbRef().whereEqualTo("QuizId", id).get().await()
        Log.d("debugging", querySnapshot.documents.toString())
        return if (!querySnapshot.isEmpty) {
            val doc = querySnapshot.documents[0]
            doc.data?.let {
                it["id"] = doc.id
                Quiz.fromHashMap(it)
            }
        } else {
            // No matching document found
            null
        }
    }

}