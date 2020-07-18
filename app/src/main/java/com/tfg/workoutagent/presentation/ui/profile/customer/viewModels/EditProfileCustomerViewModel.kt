package com.tfg.workoutagent.presentation.ui.profile.customer.viewModels

import android.content.Intent
import androidx.lifecycle.*
import com.tfg.workoutagent.data.repositoriesImpl.StorageRepositoryImpl
import com.tfg.workoutagent.domain.profileUseCases.ManageProfileUseCase
import com.tfg.workoutagent.domain.storageUseCases.ManageFilesUseCaseImpl
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileCustomerViewModel(private val manageProfileUseCase: ManageProfileUseCase) : ViewModel() {

    var id = MutableLiveData("")

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

    var height = MutableLiveData(0)
    private val _heightError = MutableLiveData("")
    val heightError: LiveData<String>
        get() = _heightError

    private val _customerDeleted = MutableLiveData<Boolean?>(null)
    val customerDeleted : LiveData<Boolean?>
        get() = _customerDeleted

    private val _customerEdited = MutableLiveData<Boolean?>(null)
    val customerEdited : LiveData<Boolean?>
        get() = _customerEdited

    val getCustomer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val customer = manageProfileUseCase.getLoggedUserCustomer()
            emit(customer)
            if (customer is Resource.Success){
                loadData(customer)
            }
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    private fun loadData(customer : Resource.Success<Customer>){
        id.postValue(customer.data.id)
        birthday.postValue(parseDateToFriendlyDate(customer.data.birthday))
        dni.postValue(customer.data.dni)
        email.postValue(customer.data.email)
        name.postValue(customer.data.name)
        surname.postValue(customer.data.surname)
        phone.postValue(customer.data.phone)
        photo.postValue(customer.data.photo)
        height.postValue(customer.data.height)
    }

    fun onSave() {
        if(checkData()){
            editProfileCustomer()
        }
    }

    fun onDelete(){
        viewModelScope.launch {
            try {
                manageProfileUseCase.deleteLoggedCustomer()
                 _customerDeleted.value = true
            }catch (e:Exception){
                _customerDeleted.value = false
            }
        }
    }

    private fun checkData(): Boolean {
        _birthdayError.value = checkBirthday(birthday.value)
        _dniError.value = checkDni(dni.value)
        _emailError.value = checkEmail(email.value)
        _nameError.value = checkName(name.value)
        _surnameError.value = checkSurname(surname.value)
        _phoneError.value = checkPhone(phone.value)
        _heightError.value = checkHeight(height.value)
        return _birthdayError.value == "" && _dniError.value == "" && _emailError.value == "" && _nameError.value == "" && _surnameError.value == "" && _photoError.value == "" && _phoneError.value == ""
    }

    private fun editProfileCustomer(){
        viewModelScope.launch {
            try{
                if(dataPhoto != null){
                    val upload = ManageFilesUseCaseImpl(StorageRepositoryImpl())
                    when(val photoUri = upload.uploadPhotoUser(dataPhoto!!)){
                        is Resource.Success -> {
                            //Modificated image
                            photo.value = photoUri.data
                            val customer = Customer(id = id.value!!, birthday = parseStringToDate(birthday.value!!)!!, dni = dni.value!!, email = email.value!!, name = name.value!!, surname = surname.value!!, photo = photo.value!!, phone = phone.value!!)
                            manageProfileUseCase.editProfileCustomer(customer)
                            _customerEdited.value = true
                        }
                        else -> {
                            photo.value = ""
                            val customer = Customer(id = id.value!!, birthday = parseStringToDate(birthday.value!!)!!, dni = dni.value!!, email = email.value!!, name = name.value!!, surname = surname.value!!, photo = photo.value!!, phone = phone.value!!)
                            manageProfileUseCase.editProfileCustomer(customer)
                            _customerEdited.value = true
                        }
                    }
                }else{
                    val customer = Customer(id = id.value!!, birthday = parseStringToDate(birthday.value!!)!!, dni = dni.value!!, email = email.value!!, name = name.value!!, surname = surname.value!!, photo = photo.value!!, phone = phone.value!!)
                    manageProfileUseCase.editProfileCustomer(customer)
                    _customerEdited.value = true
                }
            }catch (e: Exception){
                _customerEdited.value = false
            }
            _customerEdited.value = null
        }
    }
}
