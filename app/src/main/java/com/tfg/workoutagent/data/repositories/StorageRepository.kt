package com.tfg.workoutagent.data.repositories

import android.content.Intent
import com.tfg.workoutagent.vo.Resource

interface StorageRepository {
    suspend fun uploadImage(intent : Intent) : Resource<String>
    suspend fun uploadMultipleImages(intent: Intent) : Resource<MutableList<String>>
    suspend fun uploadPdf(intent: Intent) : Resource<String>
}