package com.tfg.workoutagent.presentation.ui.users.trainer.viewModels

import android.content.Intent
import androidx.lifecycle.*
import com.tfg.workoutagent.data.repositoriesImpl.StorageRepositoryImpl
import com.tfg.workoutagent.domain.storageUseCases.ManageFilesUseCaseImpl
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCase
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

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

    var dataPhoto : Intent? = null
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

    val pickerDate = MutableLiveData<Date>()

    fun setDate(time: Long) {
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        pickerDate.value = Date(time)
        val date = simpleDateFormat.format(pickerDate.value)
        birthday.value = date
    }

    fun onSave() {
        if(checkData()){
            editCustomer()
        }
    }

    private fun checkData(): Boolean {
        _birthdayError.value = checkBirthday(birthday.value)
        _dniError.value = checkDni(dni.value)
        checkEmail()
        checkName()
        checkSurname()
        checkPhone()
        return _birthdayError.value == "" && _dniError.value == "" && _emailError.value == "" && _nameError.value == "" && _surnameError.value == "" && _photoError.value == "" && _phoneError.value == ""
    }

    private fun editCustomer(){
        viewModelScope.launch {
            try{
                if(dataPhoto!= null){
                    val upl = ManageFilesUseCaseImpl(StorageRepositoryImpl())
                    when(val photoUri = upl.uploadPhotoUser(dataPhoto!!)){
                        is Resource.Success -> {
                            photo.value  = photoUri.data
                            val customer = Customer(id = id, birthday = parseStringToDateBar(birthday.value!!)!!, dni = dni.value!!, email = email.value!!, name = name.value!!, surname = surname.value!!, photo = photo.value!!, phone = phone.value!!)
                            manageCustomerTrainerUseCase.updateCustomer(customer)
                            _customerEdited.value = true
                        }
                        else -> {
                            photo.value = "DEFAULT_IMAGE"
                            val customer = Customer(id = id, birthday = parseStringToDateBar(birthday.value!!)!!, dni = dni.value!!, email = email.value!!, name = name.value!!, surname = surname.value!!, photo = photo.value!!, phone = phone.value!!)
                            manageCustomerTrainerUseCase.updateCustomer(customer)
                            _customerEdited.value = true
                        }
                    }
                }else{
                    val customer = Customer(id = id, birthday = parseStringToDateBar(birthday.value!!)!!, dni = dni.value!!, email = email.value!!, name = name.value!!, surname = surname.value!!, photo = photo.value!!, phone = phone.value!!)
                    manageCustomerTrainerUseCase.updateCustomer(customer)
                    _customerEdited.value = true
                }
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
        birthday.postValue(parseDateToFriendlyDateBar(customer.data.birthday))
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
