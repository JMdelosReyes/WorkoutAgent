package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.workoutagent.domain.routineUseCases.AssignRoutinesUseCase
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AssignRoutineViewModel(private val assignRoutinesUseCase: AssignRoutinesUseCase) :
    ViewModel() {

    val customers: MutableLiveData<Resource<MutableList<Customer>>> = MutableLiveData()

    val routines: MutableLiveData<Resource<MutableList<Routine>>> = MutableLiveData()

    fun loadCustomers() = viewModelScope.launch(Dispatchers.IO) {
        try {
            customers.postValue(Resource.Loading())
            val customerList = assignRoutinesUseCase.getCustomers()
            if (customerList is Resource.Success) {
                customers.postValue(customerList)
                allCustomers = mutableListOf()
                allCustomers.addAll(customerList.data)
                filteredCustomers = mutableListOf()
                filteredCustomers.addAll(customerList.data)
            }
        } catch (e: Exception) {
            customers.postValue(Resource.Failure(e))
        }
    }

    fun loadRoutines() = viewModelScope.launch(Dispatchers.IO) {
        try {
            routines.postValue(Resource.Loading())
            val routineList = assignRoutinesUseCase.getTemplateRoutines()
            if (routineList is Resource.Success) {
                routines.postValue(routineList)
                allRoutines = mutableListOf()
                allRoutines.addAll(routineList.data)
                filteredRoutines = mutableListOf()
                filteredRoutines.addAll(routineList.data)
            }
        } catch (e: Exception) {
            routines.postValue(Resource.Failure(e))
        }
    }

    private var allCustomers = mutableListOf<Customer>()

    private var filteredCustomers = mutableListOf<Customer>()

    private var selectedCustomer: Customer? = null

    fun getFilteredCustomers() = this.filteredCustomers

    fun filterCustomers(filterText: String?) {
        if (filterText == null || filterText == "") {
            filteredCustomers = mutableListOf()
            filteredCustomers.addAll(this.allCustomers)
        } else {
            val customerList = mutableListOf<Customer>()
            this.allCustomers.forEach {
                if (it.name.toLowerCase(Locale.ROOT)
                        .contains(filterText) || it.surname.toLowerCase(Locale.ROOT)
                        .contains(filterText)
                ) {
                    customerList.add(it)
                }
            }
            filteredCustomers = mutableListOf()
            filteredCustomers.addAll(customerList)
        }
    }

    fun getSelectedCustomer() = this.selectedCustomer

    fun updateSelectedCustomer(customer: Customer?) {
        this.selectedCustomer = customer
    }

    private var allRoutines = mutableListOf<Routine>()

    private var filteredRoutines = mutableListOf<Routine>()

    private var selectedRoutine: Routine? = null

    fun getFilteredRoutines() = this.filteredRoutines

    fun filterRoutines(filterText: String?) {
        if (filterText == null || filterText == "") {
            filteredRoutines = mutableListOf()
            filteredRoutines.addAll(this.allRoutines)
        } else {
            val routineList = mutableListOf<Routine>()
            this.allRoutines.forEach {
                if (it.title.toLowerCase(Locale.ROOT)
                        .contains(filterText)
                ) {
                    routineList.add(it)
                }
            }
            filteredRoutines = mutableListOf()
            filteredRoutines.addAll(routineList)
        }
    }

    fun getSelectedRoutine() = this.selectedRoutine

    fun updateSelectedRoutine(routine: Routine?) {
        this.selectedRoutine = routine
    }

    fun onAssignRoutine() {
        if (checkAssignData()) {
            assignRoutine()
        }
    }

    private fun assignRoutine() {
        viewModelScope.launch {
            try {
                assignRoutinesUseCase.assignRoutine(
                    selectedCustomer!!,
                    selectedRoutine!!.id,
                    pickerDate.value!!
                )
                _closeAssignDialog.value = true
            } catch (e: Exception) {
                Log.i("FAIL", e.message)
                _closeAssignDialog.value = false
            }
        }
    }

    private fun checkAssignData(): Boolean {
        checkCustomer()
        checkRoutine()
        checkStartDate()
        return _customerError.value == "" && _routineError.value == "" && _startDateError.value == ""
    }

    private val _customerError = MutableLiveData("")
    val customerError: LiveData<String>
        get() = _customerError

    private fun checkCustomer() {
        if (this.selectedCustomer == null) {
            _customerError.value = "You have to select a customer"
            return
        }
        _customerError.value = ""
    }

    private val _routineError = MutableLiveData("")
    val routineError: LiveData<String>
        get() = _routineError

    private fun checkRoutine() {
        if (this.selectedRoutine == null) {
            _routineError.value = "You have to select a routine"
            return
        }
        _routineError.value = ""
    }

    val startDate = MutableLiveData("")
    private val _startDateError = MutableLiveData("")
    val startDateError: LiveData<String>
        get() = _startDateError

    val pickerDate = MutableLiveData(Date())

    fun setDate(time: Long) {
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        pickerDate.value = Date(time)
        val date = simpleDateFormat.format(pickerDate.value)
        startDate.value = date
    }

    private fun checkStartDate() {
        startDate.value?.let {
            if (it.isEmpty()) {
                _startDateError.value = "The start date cannot be empty"
                return
            } // TODO Mike calienta (la fecha no puede ser anterior, crack)
        }
        _startDateError.value = ""
    }

    // Para cerrar el dialog
    private val _closeAssignDialog = MutableLiveData<Boolean>(null)
    val closeAssignDialog: LiveData<Boolean>
        get() = _closeAssignDialog

    fun assignDialogClosed() {
        _closeAssignDialog.value = null
    }
}