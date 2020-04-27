package com.tfg.workoutagent.data.repositoriesImpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.workoutagent.data.repositories.LoginRepository
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl : LoginRepository {
    override suspend fun getRole(): Resource<String> {
        val result = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .limit(1)
            .get()
            .await()
        if(result.documents[0] != null){
            return Resource.Success(result.documents[0].getString("role")!!)
        }else{
            return Resource.Success("NO_ACCOUNT")
        }
    }
}