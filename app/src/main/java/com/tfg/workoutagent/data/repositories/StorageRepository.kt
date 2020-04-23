package com.tfg.workoutagent.data.repositories

import android.content.Intent
import com.tfg.workoutagent.vo.Resource

interface StorageRepository {
    suspend fun uploadImage(intent : Intent) : Resource<String>
}