package com.tfg.workoutagent.presentation.ui.users.trainer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.userUseCases.ListCustomerUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.trainer.adapters.CustomerListAdapter
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.ListCustomerViewModel
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.ListCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_trainer_user.*

class UserTrainerFragment : Fragment() {

    private lateinit var adapter: CustomerListAdapter
    private val viewModel by lazy {
        ViewModelProvider(
            this,ListCustomerViewModelFactory(
                ListCustomerUseCaseImpl(UserRepositoryImpl())
            )
        ).get(ListCustomerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trainer_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        adapter = CustomerListAdapter(this.context!!)
        recyclerView_customer_trainer.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView_customer_trainer.adapter = adapter
        observeData()
    }

    private fun observeData(){
        viewModel.customerList.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Loading -> {
                    sfl_rv_customer_trainer.startShimmer()
                }
                is Resource.Success -> {
                    sfl_rv_customer_trainer.visibility = View.GONE
                    sfl_rv_customer_trainer.stopShimmer()
                    if(result.data.size == 0){
                        //TODO: Mensaje tipo "You don't have any customers yet"
                    }
                    adapter.setListData(result.data)
                    adapter.notifyDataSetChanged()
                }
                is Resource.Failure -> {
                    sfl_rv_customer_trainer.visibility = View.GONE
                    sfl_rv_customer_trainer.stopShimmer()
                    Log.i("ERROR", "${result.exception.message}");
                    Toast.makeText(this.context!!,"Ocurrió un error ${result.exception.message}",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setupUI(){
        fab_add_customer.setOnClickListener{
            findNavController().navigate(
                UserTrainerFragmentDirections.actionNavigationUsersTrainerToCreateCustomerTrainerFragment2()
            )
        }
    }

}
