package com.tfg.workoutagent.domain.storageUseCases

import android.content.Intent
import com.tfg.workoutagent.data.repositories.StorageRepository
import com.tfg.workoutagent.vo.Resource

class ManageFilesUseCaseImpl(private val repo : StorageRepository) : ManageFileUseCase {
    override suspend fun uploadPhotoUser(intent: Intent): Resource<String> = repo.uploadImage(intent)
    override suspend fun uploadPDF(intent: Intent): Resource<String> = repo.uploadPdf(intent)
    override suspend fun uploadMultipleImages(intent: Intent): Resource<MutableList<String>> = repo.uploadMultipleImages(intent)
}