package com.tfg.workoutagent.domain.login

import com.tfg.workoutagent.vo.Resource

interface LoginUseCase {
    suspend fun getRole() : Resource<String>
}