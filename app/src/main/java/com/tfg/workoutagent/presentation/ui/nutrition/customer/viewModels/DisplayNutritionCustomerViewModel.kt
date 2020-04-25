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
            if (customer is Resource.Success) {
                loadData(customer)
            }
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    var birthday = MutableLiveData("")
    var height = MutableLiveData(0)
    var genre = MutableLiveData("")
    var initialWeight = MutableLiveData(0.0)
    var calories = MutableLiveData<String>("")
    var loadedData = MutableLiveData<Boolean?>(false)
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
            proteins.postValue((weights.value!![0].weight * 2.2).roundToInt().toString())
            fats.postValue((weights.value!![0].weight * 0.8).roundToInt().toString())
            carbohydrates.postValue((calories.value!!.toDouble() - ((proteins.value!!.toDouble() * 4) + (fats.value!!.toDouble() * 9))).roundToInt().toString())


            loadedData.postValue(true)
    }

    fun loadData(customer: Resource.Success<Customer>) {
        birthday.postValue(parseDateToFriendlyDate(customer.data.birthday))
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



    }
}