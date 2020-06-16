package com.tfg.workoutagent.presentation.ui.users.admin.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerAdminUseCaseImpl
import com.tfg.workoutagent.domain.userUseCases.ManageTrainerAdminUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.DisplayCustomerAdminViewModel
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.DisplayCustomerAdminViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import kotlinx.android.synthetic.main.fragment_display_customer_admin.*
import kotlinx.android.synthetic.main.fragment_display_customer_trainer.*

class DisplayCustomerAdminFragment : Fragment() {

    private val customerId by lazy { DisplayCustomerAdminFragmentArgs.fromBundle(arguments!!).customerId }
    private val customerName by lazy { DisplayCustomerAdminFragmentArgs.fromBundle(arguments!!).customerName }

    private val viewModel by lazy { ViewModelProvider(this, DisplayCustomerAdminViewModelFactory(customerId, ManageCustomerAdminUseCaseImpl(UserRepositoryImpl()))).get(DisplayCustomerAdminViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_display_customer_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupUI()
    }

    private fun observeData(){
        viewModel.getCustomer.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    //TODO: showProgress()
                }
                is Resource.Success -> {
                    //TODO: hideProgress()
                    display_customer_birthday_admin.text = parseDateToFriendlyDate(it.data.birthday)
                    display_customer_name_admin.text = it.data.name + " " + it.data.surname
                    display_customer_email_admin.text = it.data.email
                    display_customer_phone_admin.text = it.data.phone
                    if(it.data.genre == "M"){
                        display_customer_gender_admin.text = getString(R.string.genre_male)
                    }else{
                        display_customer_gender_admin.text = getString(R.string.genre_female)
                    }
                    Log.i("RESOURCE SUCCESS CM", it.data.height.toString() + " cm")
                    Log.i("RESOURCE SUCCESS KG", it.data.weights[it.data.weights.lastIndex].weight.toString() + " kg")
                    display_customer_height_admin.text = it.data.height.toString() + " cm"
                    display_customer_weight_admin.text = it.data.weights[it.data.weights.lastIndex].weight.toString() + " kg"
                    if(it.data.photo == "" || it.data.photo == "DEFAULT_IMAGE"){
                        Glide.with(this).load(R.drawable.ic_person_black_60dp).into(circleImageViewCustomer_admin)
                    }else{
                        Glide.with(this).load(it.data.photo).into(circleImageViewCustomer_admin)
                    }
                }
                is Resource.Failure -> {
                    //TODO: hideProgress()
                    Toast.makeText(context, "${it}", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        })

        viewModel.customerDeleted.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(DisplayCustomerAdminFragmentDirections.actionDisplayCustomerAdminFragmentToNavigationAdminUsers())
                }
                false -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun setupUI(){
        display_customer_button_delete_admin.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle(getString(R.string.alert_title_delete_user))
            builder.setMessage(getString(R.string.alert_message_delete))

            builder.setPositiveButton(getString(R.string.answer_yes)) { dialog, _ ->
                dialog.dismiss()
                viewModel.onDelete()
            }

            builder.setNeutralButton(getString(R.string.answer_no)) { dialog, _ ->
                dialog.dismiss()
            }
            builder.create()
            builder.show()
        }
    }
}
