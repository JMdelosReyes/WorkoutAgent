package com.tfg.workoutagent.presentation.ui.users.trainer.viewModels

import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import com.tfg.workoutagent.data.repositoriesImpl.StorageRepositoryImpl
import com.tfg.workoutagent.domain.storageUseCases.ManageFilesUseCaseImpl
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCase
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Weight
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.checkExistingMail
import com.tfg.workoutagent.vo.utils.getAgeWithError
import com.tfg.workoutagent.vo.utils.parseStringToDate
import com.tfg.workoutagent.vo.utils.parseStringToDateBar
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CreateCustomerTrainerViewModel(private val manageCustomerTrainerUseCase: ManageCustomerTrainerUseCase) : ViewModel() {
    var birthday = MutableLiveData("")
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

    var genre : String = ""
    private val _genreError = MutableLiveData("")
    val genreError: LiveData<String>
        get() = _genreError

    var photo : String = ""
    private val _photoError = MutableLiveData("")
    val photoError: LiveData<String>
        get() = _photoError
    //Guardo el resultado y datos del intent hasta hacer el submit y subir la foto
    var dataPhoto : Intent? = null

    var height : Int = 0
    private val _heightError = MutableLiveData("")
    val heightError: LiveData<String>
        get() = _heightError

    var phone : String = ""
    private val _phoneError = MutableLiveData("")
    val phoneError: LiveData<String>
        get() = _phoneError

    var initialWeight : Double = 0.0
    private val _initialWeightError = MutableLiveData("")
    val initialWeightError: LiveData<String>
        get() = _initialWeightError

    private val _customerCreated = MutableLiveData<Boolean?>(null)
    val customerCreated : LiveData<Boolean?>
        get() = _customerCreated

    val pickerDate = MutableLiveData<Date>(Date())

    fun onSubmit() {
        if(checkData()){
            uploadImage()
        }
    }

    fun setDate(time: Long) {
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        pickerDate.value = Date(time)
        val date = simpleDateFormat.format(pickerDate.value)
        birthday.value = date
    }

    private fun uploadImage() {
        viewModelScope.launch {
            try{
                if(dataPhoto != null){
                    val upl = ManageFilesUseCaseImpl(StorageRepositoryImpl())
                    when(val photoUri = upl.uploadPhotoUser(dataPhoto!!)){
                        is Resource.Success -> {
                            photo = photoUri.data
                            val customer = Customer(birthday = parseStringToDateBar(birthday.value)!!, dni = dni.toUpperCase(), email = email, genre = genre, name = name, surname = surname, photo = photo, phone = phone, height = height)
                            val weight = Weight(weight = initialWeight)
                            customer.weights.add(weight)
                            manageCustomerTrainerUseCase.createCustomer(customer)
                            _customerCreated.value = true
                        }
                        else -> {
                            photo = "DEFAULT_IMAGE"
                            val customer = Customer(birthday = parseStringToDateBar(birthday.value)!!, dni = dni.toUpperCase(), email = email, genre = genre, name = name, surname = surname, photo = photo, phone = phone, height = height)
                            val weight = Weight(weight = initialWeight)
                            customer.weights.add(weight)
                            manageCustomerTrainerUseCase.createCustomer(customer)
                            _customerCreated.value = true
                        }
                    }
                }else{
                    photo = "DEFAULT_IMAGE"
                    val customer = Customer(birthday = parseStringToDateBar(birthday.value)!!, dni = dni.toUpperCase(), email = email, genre = genre, name = name, surname = surname, photo = photo, phone = phone, height = height)
                    val weight = Weight(weight = initialWeight)
                    customer.weights.add(weight)
                    manageCustomerTrainerUseCase.createCustomer(customer)
                    _customerCreated.value = true
                }
            }catch (e: Exception){
            }
        }
    }

    private fun checkData(): Boolean {
        _birthdayError.value = com.tfg.workoutagent.vo.utils.checkBirthday(birthday.value)
        _dniError.value = com.tfg.workoutagent.vo.utils.checkDni(dni.toUpperCase())
        checkEmail()
        if(_emailError.value.equals("")){
            viewModelScope.launch {
                try{
                    if(checkExistingMail(email)){
                        _emailError.value = "This mail is already used"
                    }
                }catch (e: Exception) {
                    _emailError.value = "Try with other email"
                }
            }
        }
        checkName()
        checkSurname()
        checkGender()
        checkPhoto()
        checkPhone()
        checkHeight()
        checkInitialWeight()
        return _birthdayError.value == "" && _dniError.value == "" && _emailError.value == "" && _nameError.value == "" && _surnameError.value == "" && _photoError.value == "" && _heightError.value == "" && _initialWeightError.value == "" && _phoneError.value == ""
    }


    private fun checkGender(){
        genre.let {
            if(genre != "M" && genre != "F"){
                _genreError.value = "You should select one genre"
            }else{
                _genreError.value = ""
            }
        }
    }


    private fun checkEmail() {
        email.let {
            val isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
            if(isEmail) _emailError.value="" else _emailError.value="Must be a valid email"
        }
    }

    private fun checkName() {
        name.let {
            if (it.length < 4 || it.length > 30) {
                _nameError.value = "The name must be between 4 and 30 characters"
            }else{
                _nameError.value = ""
            }
        }
    }

    private fun checkSurname() {
        surname.let {
            if (it.length < 4 || it.length > 40) {
                _surnameError.value = "The surname must be between 4 and 40 characters"
                return
            } else _surnameError.value = ""
        }
    }

    private fun checkPhoto(){
        //TODO: validación para las imágenes que se intente subir (?)
        _photoError.value = ""
    }

    private fun checkPhone(){
        //TODO: validación para números de teléfono
        phone.let {
            val isPhone = android.util.Patterns.PHONE.matcher(it).matches()
            if(isPhone) _phoneError.value = ""  else _phoneError.value="Must be a valid phone"
        }
    }

    private fun checkHeight(){
        height.let { if(it <= 0) _heightError.value = "The height must be greater than 0 centimeters" else _heightError.value = "" }
    }

    private fun checkInitialWeight(){
        initialWeight.let { if(it <= 0.0) _initialWeightError.value = "The weight must be greater than 0.0 kilograms" else _initialWeightError.value = "" }
    }
}
