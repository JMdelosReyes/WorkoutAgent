package com.tfg.workoutagent.presentation.ui.users.admin.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.tfg.workoutagent.AdminActivity
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.userUseCases.ListUserAdminUseCaseImpl
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.presentation.ui.users.admin.adapters.CustomerAdminListAdapter
import com.tfg.workoutagent.presentation.ui.users.admin.adapters.TrainerAdminListAdapter
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.UserListViewModel
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.UserListViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_admin_users.*

class UserListFragment : Fragment() {

    private lateinit var adapterCustomer : CustomerAdminListAdapter
    private lateinit var adapterTrainer : TrainerAdminListAdapter
    private lateinit var completeCustomerList: MutableList<Customer>
    private lateinit var completeTrainerList: MutableList<Trainer>
    private val viewModel by lazy {
        ViewModelProvider(
            this, UserListViewModelFactory(
                ListUserAdminUseCaseImpl(UserRepositoryImpl())
            )
        ).get(UserListViewModel::class.java)
    }

    private val tabActions = object {
        var currentMode = 0 // 0: Trainer list, 1: Customer list

        fun changeMode(mode: Int) {
            this.currentMode = mode
            loadUsers()
        }

        fun loadUsers() {
            if (this.currentMode == 0) {
                showTrainers()
            } else {
                showCustomers()
            }
        }

        private fun showTrainers() {
            recyclerView_customer_admin.visibility = View.GONE
            recyclerView_trainer_admin.visibility = View.VISIBLE
            fab_add_trainer.visibility = View.VISIBLE
        }

        private fun showCustomers() {
            recyclerView_trainer_admin.visibility = View.GONE
            recyclerView_customer_admin.visibility = View.VISIBLE
            fab_add_trainer.visibility = View.GONE
        }
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
        setupTabs()

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
                    viewModel.filteredCustomerList = it.data
                    completeCustomerList = it.data
                    adapterCustomer.setListData(viewModel.filteredCustomerList)
                    adapterCustomer.notifyDataSetChanged()

                    tabActions.loadUsers()
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

                    viewModel.filteredTrainerList = it.data
                    completeTrainerList = it.data
                    adapterTrainer.setListData(viewModel.filteredTrainerList)
                    adapterTrainer.notifyDataSetChanged()

                    tabActions.loadUsers()
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

    //Código necesario para usar el filtro
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater?.inflate(R.menu.search_menu, menu)
        val searchView = SearchView((context as AdminActivity).supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val lowerText = query.toLowerCase()
                if(lowerText == ""){
                    viewModel.filteredCustomerList = completeCustomerList
                    adapterCustomer.setListData(viewModel.filteredCustomerList)
                    adapterCustomer.notifyDataSetChanged()

                    viewModel.filteredTrainerList = completeTrainerList
                    adapterTrainer.setListData(viewModel.filteredTrainerList)
                    adapterTrainer.notifyDataSetChanged()
                }else{
                    var list = mutableListOf<Customer>()
                    for(customer in completeCustomerList){
                        if(customer.email.toLowerCase().contains(lowerText) || customer.name.toLowerCase().contains(lowerText) || customer.surname.toLowerCase().contains(lowerText)){
                            list.add(customer)
                        }
                    }
                    viewModel.filteredCustomerList = list
                    adapterCustomer.setListData(viewModel.filteredCustomerList)
                    adapterCustomer.notifyDataSetChanged()

                    val listTrainer = mutableListOf<Trainer>()
                    for(trainer in completeTrainerList){
                        if(trainer.email.toLowerCase().contains(lowerText) || trainer.name.toLowerCase().contains(lowerText) || trainer.surname.toLowerCase().contains(lowerText)){
                            listTrainer.add(trainer)
                        }
                    }
                    viewModel.filteredTrainerList = completeTrainerList
                    adapterTrainer.setListData(viewModel.filteredTrainerList)
                    adapterTrainer.notifyDataSetChanged()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val lowerText = newText.toLowerCase()
                if(lowerText == ""){
                    viewModel.filteredCustomerList = completeCustomerList
                    adapterCustomer.setListData(viewModel.filteredCustomerList)
                    adapterCustomer.notifyDataSetChanged()
                    viewModel.filteredTrainerList = completeTrainerList
                    adapterTrainer.setListData(viewModel.filteredTrainerList)
                    adapterTrainer.notifyDataSetChanged()
                }else{
                    var list = mutableListOf<Customer>()
                    for(customer in completeCustomerList){
                        if(customer.email.toLowerCase().contains(lowerText) || customer.name.toLowerCase().contains(lowerText) || customer.surname.toLowerCase().contains(lowerText)){
                            list.add(customer)
                        }
                    }
                    viewModel.filteredCustomerList = list
                    adapterCustomer.setListData(viewModel.filteredCustomerList)
                    adapterCustomer.notifyDataSetChanged()

                    val listTrainer = mutableListOf<Trainer>()
                    for(trainer in completeTrainerList){
                        if(trainer.email.toLowerCase().contains(lowerText) || trainer.name.toLowerCase().contains(lowerText) || trainer.surname.toLowerCase().contains(lowerText)){
                            listTrainer.add(trainer)
                        }
                    }
                    viewModel.filteredTrainerList = listTrainer
                    adapterTrainer.setListData(viewModel.filteredTrainerList)
                    adapterTrainer.notifyDataSetChanged()
                }
                return true
            }
        })
    }

    private fun setupTabs() {
        tab_layout.getTabAt(this.tabActions.currentMode)?.select()
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let {
                    if (it.position == 0) {
                        tabActions.changeMode(0)
                    } else {
                        tabActions.changeMode(1)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // nothing
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    if (it.position == 0) {
                        tabActions.changeMode(0)
                    } else {
                        tabActions.changeMode(1)
                    }
                }
            }
        })
    }
}
