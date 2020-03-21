package com.tfg.workoutagent.data.repositories

import com.tfg.workoutagent.vo.Resource

interface LoginRepository {
    suspend fun getRole() : Resource<String>
}