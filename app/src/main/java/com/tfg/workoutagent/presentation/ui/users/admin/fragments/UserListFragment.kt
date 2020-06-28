package com.tfg.workoutagent.presentation.ui.users.admin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.userUseCases.ListUserAdminUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.admin.adapters.CustomerAdminListAdapter
import com.tfg.workoutagent.presentation.ui.users.admin.adapters.TrainerAdminListAdapter
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.UserListViewModel
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.UserListViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_admin_users.*

class UserListFragment : Fragment() {

    private lateinit var adapterCustomer : CustomerAdminListAdapter
    private lateinit var adapterTrainer : TrainerAdminListAdapter
    private val viewModel by lazy {
        ViewModelProvider(
            this, UserListViewModelFactory(
                ListUserAdminUseCaseImpl(UserRepositoryImpl())
            )
        ).get(UserListViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        adapterCustomer = CustomerAdminListAdapter(this.context!!)
        adapterTrainer = TrainerAdminListAdapter(this.context!!)

        recyclerView_customer_admin.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView_customer_admin.adapter = adapterCustomer
        recyclerView_trainer_admin.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView_trainer_admin.adapter = adapterTrainer
        observeData()
    }

    private fun observeData(){

        viewModel.customerList.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> { sfl_rv_customer_admin.startShimmer() }
                is Resource.Success -> {
                    sfl_rv_customer_admin.visibility = View.GONE
                    sfl_rv_customer_admin.stopShimmer()
                    if(it.data.size==0){
                        //TODO: Mensaje tipo "You don't have any customers yet"
                    }
                    adapterCustomer.setListData(it.data)
                    adapterCustomer.notifyDataSetChanged()
                }
                is Resource.Failure -> {
                    sfl_rv_customer_admin.visibility = View.GONE
                    sfl_rv_customer_admin.stopShimmer()
                    Toast.makeText(this.context!!,"Ocurrió un error ${it.exception.message}",Toast.LENGTH_LONG).show()
                }
            }
        })


        viewModel.trainerList.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> { sfl_rv_customer_admin.startShimmer() }
                is Resource.Success -> {
                    sfl_rv_customer_admin.visibility = View.GONE
                    sfl_rv_customer_admin.stopShimmer()
                    if(it.data.size==0){
                        //TODO: Mensaje tipo "You don't have any customers yet"
                    }
                    adapterTrainer.setListData(it.data)
                    adapterTrainer.notifyDataSetChanged()
                }
                is Resource.Failure -> {
                    sfl_rv_customer_admin.visibility = View.GONE
                    sfl_rv_customer_admin.stopShimmer()
                    Toast.makeText(this.context!!,"Ocurrió un error ${it.exception.message}",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setupUI(){
        fab_add_trainer.setOnClickListener{
            findNavController().navigate(UserListFragmentDirections.actionNavigationAdminUsersToCreateTrainerAdminFragment())
        }
    }
}
