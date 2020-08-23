package com.tfg.workoutagent.presentation.ui.nutrition.trainer.viewModels

import androidx.lifecycle.*
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCase
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCase
import com.tfg.workoutagent.models.*
import com.tfg.workoutagent.vo.*
import com.tfg.workoutagent.vo.utils.getAge
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import com.tfg.workoutagent.vo.utils.parseStringToDate
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
        MutableLiveData<MutableList<String>>(mutableListOf("Harris-Benedict", "Miffin-St.Jeor"))
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
                var kcal =  66.474 + (13.751 * pesos[0].weight) + (5.0033 * height.value!!) - (6.7550 * years)
                if(selectedFormulaType.value == "Hypocaloric"){
                    kcal *= 0.8
                }else{
                    kcal *= 1.2
                }
                calories.value = kcal.roundToInt().toString()

            } else {
                var kcal = 655.1 + (9.463 * pesos[0].weight) + (1.8* height.value!!) - (4.6756 * years)
                if(selectedFormulaType.value == "Hypocaloric"){
                    kcal *= 0.8
                }else{
                    kcal *= 1.2
                }
                calories.value = kcal.roundToInt().toString()
            }
        } else {
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
        }
        proteins.value=((weights.value!![0].weight * 1.9).roundToInt().toString())
        fats.value=((weights.value!![0].weight * 0.8).roundToInt().toString())
        carbohydrates.value=((calories.value!!.toDouble() * 0.2).roundToInt().toString())
    }

    private fun loadFormulas() {
            val pesos: MutableList<Weight> = weights.value!!
            pesos.sortByDescending { weight: Weight ->
                weight.date
            }
            val years = getAge(birthday.value!!)
            if (selectedFormula.value == "Harris-Benedict") {
                if (genre.value == "M") {
                    var kcal =  66.474 + (13.751 * pesos[0].weight) + (5.0033 * height.value!!) - (6.7550 * years)
                    if(selectedFormulaType.value == "Hypocaloric"){
                        kcal *= 0.8
                    }else{
                        kcal *= 1.2
                    }
                    calories.value=(kcal.roundToInt().toString())

                } else {
                    var kcal = 655.1 + (9.463 * pesos[0].weight) + (1.8* height.value!!) - (4.6756 * years)
                    if(selectedFormulaType.value == "Hypocaloric"){
                        kcal *= 0.8
                    }else{
                        kcal *= 1.2
                    }
                    calories.value=(kcal.roundToInt().toString())
                }
            } else {
                if (genre.value == "M") {
                    var kcal = ((10 + pesos[0].weight) + (6.25 * height.value!!) - (5 * years) + 5)
                    if(selectedFormulaType.value == "Hypocaloric"){
                        kcal *= 0.8
                    }else{
                        kcal *= 1.2
                    }
                    calories.value=(kcal.roundToInt().toString())

                } else {
                    var kcal = ((10 + pesos[0].weight) + (6.25 * height.value!!) - (5 * years) -161)
                    if(selectedFormulaType.value == "Hypocaloric"){
                        kcal *= 0.8
                    }else{
                        kcal *= 1.2
                    }
                    calories.value=(kcal.roundToInt().toString())
                }
            }
        loadedData.value= (true)
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

    fun loadData(customer: Resource.Success<Customer>) {
        birthday.value =(parseDateToFriendlyDate(customer.data.birthday))
        dni.value =(customer.data.dni)
        email.value =(customer.data.email)
        name.value= (customer.data.name)
        surname.value=(customer.data.surname)
        phone.value=(customer.data.phone)
        height.value=(customer.data.height)
        initialWeight.value=(customer.data.weights.last().weight)
        genre.value=(customer.data.genre)
        weights.value=(customer.data.weights)
        selectedFormula.value=(customer.data.formula)
        selectedFormulaType.value=(customer.data.formulaType)
        weights.value!!.sortByDescending { weight: Weight ->
            weight.date
        }
        loadFormulas()

        proteins.value=((weights.value!![0].weight * 1.9).roundToInt().toString())
        fats.value=((weights.value!![0].weight * 0.8).roundToInt().toString())
        carbohydrates.value=((calories.value!!.toDouble() * 0.2).roundToInt().toString())





    }
}