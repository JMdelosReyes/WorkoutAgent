package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

interface ManageUsersUseCase {
    suspend fun getUsersMails() : Resource<List<String>>

}