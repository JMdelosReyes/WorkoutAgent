package com.tfg.workoutagent.domain.storageUseCases

import android.content.Intent
import com.tfg.workoutagent.vo.Resource

interface UploadImageUserUseCase {
    suspend fun uploadPhotoUser(intent : Intent): Resource<String>
}