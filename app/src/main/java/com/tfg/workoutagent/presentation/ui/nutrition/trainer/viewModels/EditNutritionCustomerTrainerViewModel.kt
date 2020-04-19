package com.tfg.workoutagent.presentation.ui.nutrition.trainer.viewModels

import androidx.lifecycle.*
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCase
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCase
import com.tfg.workoutagent.models.*
import com.tfg.workoutagent.vo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class EditNutritionCustomerTrainerViewModel(
    private val customerId: String,
    private val manageCustomerTrainerUseCase: ManageCustomerTrainerUseCase
) : ViewModel() {

    val selectedFormula = MutableLiveData<String>("")
    var selectedFormulaType = MutableLiveData<String>("")


    val getCustomer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val customer = manageCustomerTrainerUseCase.getCustomer(customerId)
            emit(customer)
            if (customer is Resource.Success) {
                loadData(customer)
            }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    var birthday = MutableLiveData("")
    var dni = MutableLiveData("")
    var email = MutableLiveData("")
    var name = MutableLiveData("")
    var surname = MutableLiveData("")
    var photo = MutableLiveData("")
    var height = MutableLiveData(0)
    var phone = MutableLiveData("")
    var genre = MutableLiveData("")
    var initialWeight = MutableLiveData(0.0)
    var calories = MutableLiveData<String>("")
    var loadedData = MutableLiveData<Boolean?>(null)
    var weights = MutableLiveData<MutableList<Weight>>(mutableListOf())
    var proteins = MutableLiveData<String>()
    var carbohydrates = MutableLiveData<String>()
    var fats = MutableLiveData<String>()
    var formulas =
        MutableLiveData<MutableList<String>>(mutableListOf("Harris-Benedict", "Formula2"))
    var formulaTypes =
        MutableLiveData<MutableList<String>>(mutableListOf("Hypocaloric", "Hipercaloric"))


    private val _customerEdited = MutableLiveData<Boolean?>(null)
    val customerEdited: LiveData<Boolean?>
        get() = _customerEdited

    // Para el spinner
    private fun getFormulas() {
        val pesos: MutableList<Weight> = weights.value!!
        pesos.sortByDescending { weight: Weight ->
            weight.date
        }
        val years = getAge(birthday.value!!)
        if (selectedFormula.value == "Harris-Benedict") {
            if (genre.value == "M") {
                var kcal = ((10 + pesos[0].weight) + (6.25 * height.value!!) - (5 * years) + 5)
                if(selectedFormulaType.value == "Hypocaloric"){
                    kcal *= 0.8
                }else{
                    kcal *= 1.2
                }
                calories.value = kcal.roundToInt().toString()

            } else {
                var kcal = ((10 + pesos[0].weight) + (6.25 * height.value!!) - (5 * years) -161)
                if(selectedFormulaType.value == "Hypocaloric"){
                    kcal *= 0.8
                }else{
                    kcal *= 1.2
                }
                calories.value = kcal.roundToInt().toString()
            }
        } else {
            if (genre.value == "M") {
                var kcal = 1500.0
                if(selectedFormulaType.value == "Hypocaloric"){
                    kcal *= 0.8
                }else{
                    kcal *= 1.2
                }
                calories.value = kcal.roundToInt().toString()

            } else {
                var kcal = 1000.0
                if(selectedFormulaType.value == "Hypocaloric"){
                    kcal *= 0.8
                }else{
                    kcal *= 1.2
                }
                calories.value = kcal.roundToInt().toString()
            }
        }
        proteins.value = ((weights.value!![0].weight * 2.2).roundToInt().toString())
        fats.value = ((weights.value!![0].weight * 0.8).roundToInt().toString())
        carbohydrates.value = ((calories.value!!.toDouble() - ((proteins.value!!.toDouble() * 4) + (fats.value!!.toDouble() * 9))).roundToInt().toString())
    }

    private fun loadFormulas() {
            val pesos: MutableList<Weight> = weights.value!!
            pesos.sortByDescending { weight: Weight ->
                weight.date
            }
            val years = getAge(birthday.value!!)
            if (selectedFormula.value == "Harris-Benedict") {
                if (genre.value == "M") {
                    var kcal = ((10 + pesos[0].weight) + (6.25 * height.value!!) - (5 * years) + 5)
                    if(selectedFormulaType.value == "Hypocaloric"){
                        kcal *= 0.8
                    }else{
                        kcal *= 1.2
                    }
                    calories.postValue(kcal.roundToInt().toString())

                } else {
                    var kcal = ((10 + pesos[0].weight) + (6.25 * height.value!!) - (5 * years) -161)
                    if(selectedFormulaType.value == "Hypocaloric"){
                        kcal *= 0.8
                    }else{
                        kcal *= 1.2
                    }
                    calories.postValue(kcal.roundToInt().toString())
                }
            } else {
                if (genre.value == "M") {
                    var kcal = 1500.0
                    if(selectedFormulaType.value == "Hypocaloric"){
                        kcal *= 0.8
                    }else{
                        kcal *= 1.2
                    }
                    calories.postValue(kcal.roundToInt().toString())

                } else {
                    var kcal = 1000.0
                    if(selectedFormulaType.value == "Hypocaloric"){
                        kcal *= 0.8
                    }else{
                        kcal *= 1.2
                    }
                    calories.postValue(kcal.roundToInt().toString())
                }
            }
        loadedData.postValue(true)
    }

    fun changeCalories(){
        if(loadedData.value == true){
            getFormulas()
        }

    }
    // Para el spinner
    fun selectFormula(formula: String) {
        selectedFormula.value = formula
    }

    fun selectFormulaType(type: String) {
        selectedFormulaType.value = type
    }



    fun onEditNutritionCustomer() {
        viewModelScope.launch {
            try {
                val customer = Customer(
                    id = customerId,
                    birthday = parseStringToDate(birthday.value!!)!!,
                    dni = dni.value!!,
                    email = email.value!!,
                    name = name.value!!,
                    surname = surname.value!!,
                    photo = photo.value!!,
                    genre = genre.value!!,
                    formula = selectedFormula.value!!,
                    formulaType = selectedFormulaType.value!!,
                    phone = phone.value!!,
                    height = height.value!!
                )
                val weight = Weight(weight = initialWeight.value!!)
                customer.weights.add(weight)
                manageCustomerTrainerUseCase.updateCustomer(customer)
                _customerEdited.value = true
            } catch (e: java.lang.Exception) {
                _customerEdited.value = false
            }
            _customerEdited.value = null
        }
    }

    private fun loadData(customer: Resource.Success<Customer>) {
        birthday.postValue(parseDateToFriendlyDate(customer.data.birthday))
        dni.postValue(customer.data.dni)
        email.postValue(customer.data.email)
        name.postValue(customer.data.name)
        surname.postValue(customer.data.surname)
        phone.postValue(customer.data.phone)
        height.postValue(customer.data.height)
        initialWeight.postValue(customer.data.weights.last().weight)
        genre.postValue(customer.data.genre)
        weights.postValue(customer.data.weights)
        selectedFormula.postValue(customer.data.formula)
        selectedFormulaType.postValue(customer.data.formulaType)
        weights.value!!.sortByDescending { weight: Weight ->
            weight.date
        }
        loadFormulas()

        proteins.postValue((weights.value!![0].weight * 2.2).roundToInt().toString())
        fats.postValue((weights.value!![0].weight * 0.8).roundToInt().toString())
        carbohydrates.postValue((calories.value!!.toDouble() - ((proteins.value!!.toDouble() * 4) + (fats.value!!.toDouble() * 9))).roundToInt().toString())





    }
}