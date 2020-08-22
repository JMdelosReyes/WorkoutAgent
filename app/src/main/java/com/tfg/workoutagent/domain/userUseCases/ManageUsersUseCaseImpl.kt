package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

class ManageUsersUseCaseImpl(private val repo: UserRepository) : ManageUsersUseCase {
    override suspend fun getUsersMails(): Resource<MutableList<String>> = repo.getUsersMails()

}