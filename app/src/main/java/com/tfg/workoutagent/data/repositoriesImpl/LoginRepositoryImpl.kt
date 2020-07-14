package com.tfg.workoutagent.data.repositoriesImpl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdReceiver
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

    override suspend fun updateToken(): Resource<String> {
        var token = ""
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener{
            it.result?.token?.let {it->
                token = it
            }
        }
        val resultData = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get().await().documents[0]

        val data : HashMap<String, Any?> = hashMapOf("token" to token)
        FirebaseFirestore.getInstance().collection("users").document(resultData.id).update(data).await()
        return Resource.Success(token)
    }
}