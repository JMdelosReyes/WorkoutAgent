package com.tfg.workoutagent.data.repositoriesImpl

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.tfg.workoutagent.data.repositories.StorageRepository
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.tasks.await
import java.util.*

class StorageRepositoryImpl : StorageRepository {

    override suspend fun uploadImage(intent : Intent) : Resource<String> {
        val filename = UUID.randomUUID().toString()
        val storageReference = FirebaseStorage.getInstance().getReference("/photos/$filename")
        val uploadTask = storageReference.putFile(intent.data!!)
        val task = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                //Log.i("ERROR", "ERROR UPLOADING PHOTO REPOSITORY")
            }
            storageReference.downloadUrl
        }.await()
        return Resource.Success(task.toString())
    }

    override suspend fun uploadMultipleImages(intent: Intent): Resource<MutableList<String>> {
        val res : MutableList<String> = mutableListOf()
        if(intent.clipData != null){
            for (index in 0 until intent.clipData!!.itemCount) run {
                val filename = UUID.randomUUID().toString()
                val storageReference = FirebaseStorage.getInstance().getReference("/photos/$filename")
                val photoUri: Uri = intent.clipData!!.getItemAt(index).uri
                val uploadTask = storageReference.putFile(photoUri)
                val task = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        //Log.i("ERROR", "ERROR UPLOADING PHOTO REPOSITORY")
                    }
                    storageReference.downloadUrl
                }.await()
                res.add(task.toString())
            }
        }else if(intent.data != null){
            when(val result = this.uploadImage(intent)) {
                is Resource.Success -> {
                    res.add(result.data)
                }
            }
        }
        return Resource.Success(res)
    }

    override suspend fun uploadPdf(intent: Intent): Resource<String> {
        val filename = UUID.randomUUID().toString()
        val storageReference = FirebaseStorage.getInstance().getReference("/academicTitles/$filename")
        val uploadTask = storageReference.putFile(intent.data!!)
        val task = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                //Log.i("ERROR", "ERROR UPLOADING PDF REPOSITORY")
            }
            storageReference!!.downloadUrl
        }.await()
        return Resource.Success(task.toString())
    }
}