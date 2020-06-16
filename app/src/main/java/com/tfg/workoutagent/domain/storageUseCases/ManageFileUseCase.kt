package com.tfg.workoutagent.domain.storageUseCases

import android.content.Intent
import com.tfg.workoutagent.vo.Resource

interface ManageFileUseCase {
    suspend fun uploadPhotoUser(intent : Intent): Resource<String>
    suspend fun uploadPDF(intent: Intent): Resource<String>
}