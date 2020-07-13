package com.tfg.workoutagent.presentation.ui.nutrition.customer.viewModels

import androidx.lifecycle.*
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCase
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCase
import com.tfg.workoutagent.models.*
import com.tfg.workoutagent.vo.*
import com.tfg.workoutagent.vo.utils.getAge
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import com.tfg.workoutagent.vo.utils.parseStringToDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.math.roundToInt

class DisplayNutritionCustomerViewModel(private val displayProfileUserUseCase: DisplayProfileUserUseCase) : ViewModel() {

    val selectedFormula = MutableLiveData<String>("")
    var selectedFormulaType = MutableLiveData<String>("")


    val getCustomer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val customer = displayProfileUserUseCase.getLoggedUserCustomer()
            emit(customer)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    var birthday = MutableLiveData("")
    var height = MutableLiveData(0)
    var genre = MutableLiveData("")
    var initialWeight = MutableLiveData(0.0)
    var calories = MutableLiveData<String>("")
    var weights = MutableLiveData<MutableList<Weight>>(mutableListOf())
    var proteins = MutableLiveData<String>()
    var carbohydrates = MutableLiveData<String>()
    var fats = MutableLiveData<String>()

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
                    calories.value = (kcal.roundToInt().toString())

                } else {
                    var kcal = ((10 + pesos[0].weight) + (6.25 * height.value!!) - (5 * years) -161)
                    if(selectedFormulaType.value == "Hypocaloric"){
                        kcal *= 0.8
                    }else{
                        kcal *= 1.2
                    }
                    calories.value =(kcal.roundToInt().toString())
                }
            } else {
                if (genre.value == "M") {
                    var kcal = 1500.0
                    if(selectedFormulaType.value == "Hypocaloric"){
                        kcal *= 0.8
                    }else{
                        kcal *= 1.2
                    }
                    calories.value =(kcal.roundToInt().toString())

                } else {
                    var kcal = 1000.0
                    if(selectedFormulaType.value == "Hypocaloric"){
                        kcal *= 0.8
                    }else{
                        kcal *= 1.2
                    }
                    calories.value =(kcal.roundToInt().toString())
                }
            }
            proteins.value = ((weights.value!![0].weight * 2.2).roundToInt().toString())
            fats.value = ((weights.value!![0].weight * 0.8).roundToInt().toString())
            carbohydrates.value =((calories.value!!.toDouble() - ((proteins.value!!.toDouble() * 4) + (fats.value!!.toDouble() * 9))).roundToInt().toString())


    }

    fun loadData(customer: Resource.Success<Customer>) {
        birthday.value = (parseDateToFriendlyDate(customer.data.birthday))
        height.value = (customer.data.height)
        initialWeight.value = (customer.data.weights.last().weight)
        genre.value = (customer.data.genre)
        weights.value = (customer.data.weights)
        selectedFormula.value = (customer.data.formula)
        selectedFormulaType.value = (customer.data.formulaType)
        weights.value!!.sortByDescending { weight: Weight ->
            weight.date
        }
        loadFormulas()



    }
}