package com.tfg.workoutagent.presentation.ui.users.admin.viewModels

import androidx.lifecycle.*
import com.tfg.workoutagent.domain.userUseCases.ManageTrainerAdminUseCase
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.*
import com.tfg.workoutagent.vo.utils.*
import kotlinx.coroutines.launch
import java.lang.Exception

class CreateTrainerAdminViewModel(private val manageTrainerAdminUseCase: ManageTrainerAdminUseCase) : ViewModel() {

    var birthday : String = "dd-MM-yyyy"
    private val _birthdayError = MutableLiveData("")
    val birthdayError: LiveData<String>
        get() = _birthdayError

    var dni : String = ""
    private val _dniError = MutableLiveData("")
    val dniError: LiveData<String>
        get() = _dniError

    var email : String = ""
    private val _emailError = MutableLiveData("")
    val emailError: LiveData<String>
        get() = _emailError

    var name : String = ""
    private val _nameError = MutableLiveData("")
    val nameError: LiveData<String>
        get() = _nameError

    var surname : String = ""
    private val _surnameError = MutableLiveData("")
    val surnameError: LiveData<String>
        get() = _surnameError

    var photo : String = ""
    private val _photoError = MutableLiveData("")
    val photoError: LiveData<String>
        get() = _photoError

    var phone : String = ""
    private val _phoneError = MutableLiveData("")
    val phoneError: LiveData<String>
        get() = _phoneError

    private val _createdTrainer = MutableLiveData<Boolean?>(null)
    val createdTrainer: LiveData<Boolean?>
        get() = _createdTrainer

    fun onSubmit(){
        if(checkData()) createTrainer()
    }

    private fun checkData(): Boolean {
        _birthdayError.value = checkBirthday(birthday)
        _dniError.value = checkDni(dni)
        _emailError.value = checkEmail(email)
        _nameError.value = checkName(name)
        _surnameError.value = checkSurname(surname)
        _photoError.value = checkPhoto(photo)
        _phoneError.value = checkPhone(phone)
        return _birthdayError.value=="" && _dniError.value=="" && _emailError.value=="" && _nameError.value=="" && _surnameError.value=="" && _photoError.value=="" && _phoneError.value==""
    }

    private fun  createTrainer() {
        viewModelScope.launch {
            try {
                val trainer = Trainer(birthday = parseStringToDate(birthday)!!, dni = dni, email = email, name = name, surname = surname, photo = photo, phone = phone)
                manageTrainerAdminUseCase.createTrainer(trainer)
                _createdTrainer.value = true
            } catch (e: Exception) {
                _createdTrainer.value = false
            }
        }
    }

}
