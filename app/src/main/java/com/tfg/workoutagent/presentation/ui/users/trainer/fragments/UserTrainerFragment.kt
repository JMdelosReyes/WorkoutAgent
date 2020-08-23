package com.tfg.workoutagent.presentation.ui.users.trainer.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.tfg.workoutagent.R
import com.tfg.workoutagent.TrainerActivity
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.userUseCases.ListCustomerUseCaseImpl
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.presentation.ui.users.trainer.adapters.CustomerListAdapter
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.ListCustomerViewModel
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.ListCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_trainer_user.*

class UserTrainerFragment : Fragment() {

    private lateinit var adapter: CustomerListAdapter
    private lateinit var completeCustomerList: MutableList<Customer>
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
        recyclerView_customer_trainer.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

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
                    viewModel.filteredCustomerList = result.data
                    completeCustomerList = result.data
                    adapter.setListData(viewModel.filteredCustomerList)
                    adapter.notifyDataSetChanged()
                }
                is Resource.Failure -> {
                    sfl_rv_customer_trainer.visibility = View.GONE
                    sfl_rv_customer_trainer.stopShimmer()
                    //Toast.makeText(this.context!!,"Ocurrió un error ${result.exception.message}",Toast.LENGTH_LONG).show()
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

    //Código necesario para usar el filtro
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater?.inflate(R.menu.search_menu, menu)
        val searchView = SearchView((context as TrainerActivity).supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val lowerText = query.toLowerCase()
                if(lowerText == ""){
                    viewModel.filteredCustomerList = completeCustomerList
                    adapter.setListData(viewModel.filteredCustomerList)
                    adapter.notifyDataSetChanged()
                }else{
                    val list = mutableListOf<Customer>()
                    for(customer in completeCustomerList){
                        if(customer.email.toLowerCase().contains(lowerText) || customer.name.toLowerCase().contains(lowerText) || customer.surname.toLowerCase().contains(lowerText)){
                            list.add(customer)
                        }
                    }
                    viewModel.filteredCustomerList = list
                    adapter.setListData(viewModel.filteredCustomerList)
                    adapter.notifyDataSetChanged()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val lowerText = newText.toLowerCase()
                if(lowerText == ""){
                    viewModel.filteredCustomerList = completeCustomerList
                    adapter.setListData(viewModel.filteredCustomerList)
                    adapter.notifyDataSetChanged()
                }else{
                    val list = mutableListOf<Customer>()
                    for(customer in completeCustomerList){
                        if(customer.email.toLowerCase().contains(lowerText) || customer.name.toLowerCase().contains(lowerText) || customer.surname.toLowerCase().contains(lowerText)){
                            list.add(customer)
                        }
                    }
                    viewModel.filteredCustomerList = list
                    adapter.setListData(viewModel.filteredCustomerList)
                    adapter.notifyDataSetChanged()
                }
                return true
            }
        })
    }

}
