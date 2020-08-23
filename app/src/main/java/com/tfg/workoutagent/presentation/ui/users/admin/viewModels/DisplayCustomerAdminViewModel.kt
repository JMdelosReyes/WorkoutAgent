package com.tfg.workoutagent.presentation.ui.users.admin.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerAdminUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class DisplayCustomerAdminViewModel(private val customerId : String, private val manageCustomerAdminUseCase: ManageCustomerAdminUseCase) : ViewModel() {
   val getCustomer = liveData(Dispatchers.IO) {
       emit(Resource.Loading())
       try {
           val customer = manageCustomerAdminUseCase.getCustomer(customerId)
           emit(customer)
       }catch (e : Exception){
       }
   }

    private val _customerDeleted = MutableLiveData<Boolean?>(null)
    val customerDeleted: LiveData<Boolean?>
        get() = _customerDeleted

    fun onDelete(){
        viewModelScope.launch {
            try {
                manageCustomerAdminUseCase.deleteCustomerAdmin(customerId)
                _customerDeleted.value = true
            }catch (e:Exception){
                _customerDeleted.value = false
            }
        }
    }
}
