package com.fok.quizappproject.data.Model

data class User(
    val id: String? = null,
    val name: String,
    val email: String,
    val profilePicUrl: String = "",
    val role: String
) {
    fun toHashMap(): HashMap<String, String?> {
        return hashMapOf(
            "name" to name,
            "email" to email,
            "profilePicUrl" to profilePicUrl,
            "role" to role
        )
    }

    companion object {
        fun fromHashMap(hash: Map<String, Any>): User {
            return User(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                email = hash["email"].toString(),
                profilePicUrl = hash["profilePicUrl"].toString(),
                role = hash["role"].toString()
            )
        }
    }
}
