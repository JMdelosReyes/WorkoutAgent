package com.tfg.workoutagent.presentation.ui.users.trainer.viewModels

import androidx.lifecycle.*
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCase
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.getAgeWithError
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import com.tfg.workoutagent.vo.utils.parseStringToDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class EditDeleteCustomerTrainerViewModel(private val id: String, private val manageCustomerTrainerUseCase: ManageCustomerTrainerUseCase) : ViewModel() {

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

    var photo = MutableLiveData("")
    private val _photoError = MutableLiveData("")
    val photoError: LiveData<String>
        get() = _photoError

    var phone = MutableLiveData("")
    private val _phoneError = MutableLiveData("")
    val phoneError: LiveData<String>
        get() = _phoneError

    private val _customerDeleted = MutableLiveData<Boolean?>(null)
    val customerDeleted : LiveData<Boolean?>
        get() = _customerDeleted

    private val _customerEdited = MutableLiveData<Boolean?>(null)
    val customerEdited : LiveData<Boolean?>
        get() = _customerEdited

    val getCustomer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val customer = manageCustomerTrainerUseCase.getCustomer(id)
            emit(customer)
            if (customer is Resource.Success){
                loadData(customer)
            }
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    fun onSave() {
        if(checkData()){
            editCustomer()
        }
    }

    private fun checkData(): Boolean {
        checkBirthday()
        checkDni()
        checkEmail()
        checkName()
        checkSurname()
        checkPhone()
        return _birthdayError.value == "" && _dniError.value == "" && _emailError.value == "" && _nameError.value == "" && _surnameError.value == "" && _photoError.value == "" && _phoneError.value == ""
    }

    private fun editCustomer(){
        viewModelScope.launch {
            try{
                val customer = Customer(id = id, birthday = parseStringToDate(birthday.value!!)!!, dni = dni.value!!, email = email.value!!, name = name.value!!, surname = surname.value!!, photo = photo.value!!, phone = phone.value!!)
                manageCustomerTrainerUseCase.updateCustomer(customer)
                _customerEdited.value = true
            }catch (e: Exception){
                _customerEdited.value = false
            }
            _customerEdited.value = null
        }
    }

    fun onDelete(){
        viewModelScope.launch {
            try {
                when(val ownCustomers = manageCustomerTrainerUseCase.canDeleteCustomer(id)){
                    is Resource.Success -> {
                        if(ownCustomers.data) {
                            manageCustomerTrainerUseCase.deleteCustomer(id)
                            _customerDeleted.value = true
                        }else{
                            _customerDeleted.value = false
                        }
                    }
                    is Resource.Failure -> {
                        _customerDeleted.value = false
                    }
                }
            }catch (e:Exception){
                _customerDeleted.value = false
            }
        }
    }

    private fun loadData(customer : Resource.Success<Customer>){
        birthday.postValue(parseDateToFriendlyDate(customer.data.birthday))
        dni.postValue(customer.data.dni)
        email.postValue(customer.data.email)
        name.postValue(customer.data.name)
        surname.postValue(customer.data.surname)
        phone.postValue(customer.data.phone)
    }

    private fun checkBirthday() {
        birthday.value?.let {
            val date = parseStringToDate(it)
            if(date == null){
                _birthdayError.value = "Birthday cannot be null"
            }else{
                _birthdayError.value = getAgeWithError(it)
            }
        }
    }

    private fun checkDni() {
        //TODO: Regexpresion de dni de 8 digitos y 1 letra (al menos)
        dni.value?.let {
            _dniError.value = ""
        }
    }

    private fun checkEmail() {
        email.value?.let {
            val isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
            if(isEmail) _emailError.value="" else _emailError.value="Must be a valid email"
        }
    }

    private fun checkName() {
        name.value?.let {
            if (it.length < 4 || it.length > 30) {
                _nameError.value = "The name must be between 4 and 30 characters"
            }else{
                _nameError.value = ""
            }
        }
    }

    private fun checkSurname() {
        surname.value?.let {
            if (it.length < 4 || it.length > 40) {
                _surnameError.value = "The surname must be between 4 and 40 characters"
                return
            } else _surnameError.value = ""
        }
    }

    private fun checkPhone(){
        //TODO: validación para números de teléfono
        phone.value?.let {
            val isPhone = android.util.Patterns.PHONE.matcher(it).matches()
            if(isPhone) _phoneError.value = ""  else _phoneError.value="Must be a valid phone"
        }
    }
}
