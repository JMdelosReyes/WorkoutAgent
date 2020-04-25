package com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels

import android.content.Intent
import androidx.lifecycle.*
import com.tfg.workoutagent.data.repositoriesImpl.StorageRepositoryImpl
import com.tfg.workoutagent.domain.profileUseCases.ManageProfileUseCase
import com.tfg.workoutagent.domain.storageUseCases.UploadPhotoUserUseCaseImpl
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class EditProfileTrainerViewModel(private val manageProfileUseCase: ManageProfileUseCase) : ViewModel() {
    var birthday = MutableLiveData("")
    private val _birthdayError = MutableLiveData("")
    val birthdayError: LiveData<String>
        get() = _birthdayError

    var dni = MutableLiveData("")
    private val _dniError = MutableLiveData("")
    val dniError: LiveData<String>
        get() = _dniError

    var email = MutableLiveData("")
    private val _emailError = MutableLiveData("")
    val emailError: LiveData<String>
        get() = _emailError

    var name = MutableLiveData("")
    private val _nameError = MutableLiveData("")
    val nameError: LiveData<String>
        get() = _nameError

    var surname = MutableLiveData("")
    private val _surnameError = MutableLiveData("")
    val surnameError: LiveData<String>
        get() = _surnameError

    var dataPhoto : Intent? = null
    var photo = MutableLiveData("")
    private val _photoError = MutableLiveData("")
    val photoError: LiveData<String>
        get() = _photoError

    var phone = MutableLiveData("")
    private val _phoneError = MutableLiveData("")
    val phoneError: LiveData<String>
        get() = _phoneError


    var idTrainer = MutableLiveData("")


    val getCustomer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val trainer = manageProfileUseCase.getLoggedUserTrainer()
            emit(trainer)
            if (trainer is Resource.Success){
                loadData(trainer)
            }
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    private val _trainerDeleted = MutableLiveData<Boolean?>(null)
    val trainerDeleted: LiveData<Boolean?>
        get() = _trainerDeleted

    private val _trainerUpdated = MutableLiveData<Boolean?>(null)
    val trainerUpdated: LiveData<Boolean?>
        get() = _trainerUpdated


    private fun loadData(trainer : Resource.Success<Trainer>) {
        birthday.postValue(parseDateToFriendlyDate(trainer.data.birthday))
        dni.postValue(trainer.data.dni)
        email.postValue(trainer.data.email)
        name.postValue(trainer.data.name)
        surname.postValue(trainer.data.surname)
        phone.postValue(trainer.data.phone)
        idTrainer.postValue(trainer.data.id)
    }

    fun onSave(){
        if(checkData()){
            editTrainer()
        }
    }

    private fun checkData():Boolean{
        _dniError.value = checkDni(dni.value)
        _birthdayError.value = checkBirthday(birthday.value)
        _emailError.value = checkEmail(email.value)
        _nameError.value = checkName(name.value)
        _surnameError.value = checkSurname(surname.value)
        return _dniError.value=="" && _birthdayError.value=="" && _emailError.value=="" && _nameError.value=="" && _surnameError.value==""
    }

    private fun editTrainer(){
        viewModelScope.launch {
            try{
                if(dataPhoto != null){
                    val upl = UploadPhotoUserUseCaseImpl(StorageRepositoryImpl())
                    when(val photoUri = upl.uploadPhotoUser(dataPhoto!!)){
                        is Resource.Success -> {
                            //Modificated image
                            photo.value = photoUri.data
                            val trainer = Trainer(id = idTrainer.value!!, birthday = parseStringToDate(birthday.value!!)!!, dni = dni.value!!, email = email.value!!, name = name.value!!, surname = surname.value!!, photo = photo.value!!, phone = phone.value!!)
                            manageProfileUseCase.editProfileTrainer(trainer)
                            _trainerUpdated.value = true
                        }
                        else -> {
                            //Bad upload image
                            photo.value = ""
                            val trainer = Trainer(id = idTrainer.value!!, birthday = parseStringToDate(birthday.value!!)!!, dni = dni.value!!, email = email.value!!, name = name.value!!, surname = surname.value!!, photo = photo.value!!, phone = phone.value!!)
                            manageProfileUseCase.editProfileTrainer(trainer)
                            _trainerUpdated.value = true
                        }
                    }
                }else{
                    //Non modification image
                    val trainer = Trainer(id = idTrainer.value!!, birthday = parseStringToDate(birthday.value!!)!!, dni = dni.value!!, email = email.value!!, name = name.value!!, surname = surname.value!!, photo = photo.value!!, phone = phone.value!!)
                    manageProfileUseCase.editProfileTrainer(trainer)
                    _trainerUpdated.value = true
                }
            }catch (e: Exception){
                _trainerUpdated.value = false
            }
            _trainerUpdated.value = null
        }
    }

    fun onDelete(){
        viewModelScope.launch {
            try {
                manageProfileUseCase.deleteLoggedTrainer()
                _trainerDeleted.value = true
            }catch (e: Exception){
                _trainerDeleted.value = false
            }
        }
    }
}
