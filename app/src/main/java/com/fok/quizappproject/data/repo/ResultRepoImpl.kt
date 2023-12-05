package com.fok.quizappproject.data.repo

import com.fok.quizappproject.core.service.AuthService
import com.fok.quizappproject.data.Model.Quiz
import com.fok.quizappproject.data.Model.Result
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ResultRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): ResultRepo {
    private fun getDbRef(): CollectionReference {
        return db.collection("Results")
    }

    override suspend fun addResult(result: Result) {
        getDbRef().document().set(result.toHashMap()).await()
    }

    override suspend fun getResult() = callbackFlow {
        val listener = getDbRef().addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val result = mutableListOf<Result>()
            value?.documents?.let { docs ->
                for (doc in docs) {
                    doc.data?.let {
                        it["id"] = doc.id
                        result.add(Result.fromHashMap(it))
                    }
                }
                trySend(result)
            }
        }
        awaitClose {
            listener.remove()
        }
    }

}