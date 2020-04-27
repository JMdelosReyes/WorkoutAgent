package com.tfg.workoutagent.domain.storageUseCases

import android.content.Intent
import com.tfg.workoutagent.data.repositories.StorageRepository
import com.tfg.workoutagent.vo.Resource

class UploadPhotoUserUseCaseImpl(private val repo : StorageRepository) : UploadImageUserUseCase {
    override suspend fun uploadPhotoUser(intent: Intent): Resource<String> = repo.uploadImage(intent)
}