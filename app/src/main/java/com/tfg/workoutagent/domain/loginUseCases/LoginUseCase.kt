package com.tfg.workoutagent.domain.loginUseCases

import com.tfg.workoutagent.vo.Resource

interface LoginUseCase {
    suspend fun getRole() : Resource<String>
    suspend fun updateToken(): Resource<String>
}