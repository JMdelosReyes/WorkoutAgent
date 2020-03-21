package com.tfg.workoutagent.data.repoimpl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.tfg.workoutagent.data.repositories.LoginRepository
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl : LoginRepository {
    override suspend fun getRole(): Resource<String> {
        val user_mail = FirebaseAuth.getInstance().currentUser!!.email
        Log.i("LoginRepository", "user_email: ${user_mail}")
        val result = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", user_mail)
            .limit(1)
            .get()
            .await()
        val role = result.documents[0].getString("role")
        return Resource.Success(role!!)
    }
}