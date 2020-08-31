package com.tfg.workoutagent.presentation.ui.users.trainer.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker

import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentEditDeleteCustomerTrainerBinding
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.EditDeleteCustomerTrainerViewModel
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.EditDeleteCustomerTrainerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.createAlertDialog
import com.tfg.workoutagent.vo.utils.sendNotification
import kotlinx.android.synthetic.main.fragment_edit_delete_customer_trainer.*
import kotlinx.android.synthetic.main.fragment_edit_delete_customer_trainer.delete_customer_button
import kotlinx.android.synthetic.main.fragment_edit_delete_customer_trainer.edit_profile_customer_button_select_image_customer
import kotlinx.android.synthetic.main.fragment_edit_profile_customer.*
import java.util.*

class EditDeleteCustomerTrainerFragment : Fragment() {
    companion object{
        private val PICK_IMAGE_CODE = 2000
    }
    private val customerId by lazy { EditDeleteCustomerTrainerFragmentArgs.fromBundle(arguments!!).customerId}
    private val viewModel by lazy { ViewModelProvider(this, EditDeleteCustomerTrainerViewModelFactory(customerId, ManageCustomerTrainerUseCaseImpl(UserRepositoryImpl()))).get(EditDeleteCustomerTrainerViewModel::class.java)}
    private lateinit var binding: FragmentEditDeleteCustomerTrainerBinding
    private var selectedPhotoUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_delete_customer_trainer,
            container,
            false
        )

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return  this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeData()
        observeErrors()
        setupUI()
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun setupUI(){
        image_selected.visibility = View.GONE
        edit_profile_customer_button_select_image_customer.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
        }

        delete_customer_button.setOnClickListener {
            createAlertDialog(
                context = this.context!!,
                title = getString(R.string.alert_title_delete_profile),
                message = getString(R.string.alert_message_delete),
                positiveAction = {
                    sendNotification(this.context!!, "${viewModel.name.value} account has been deleted", "His/her trainer decided to remove this account", "/topics/admin")
                    viewModel.onDelete()
                },
                negativeAction ={},
                positiveText = getString(R.string.answer_yes),
                negativeText = getString(R.string.answer_no)
            )
        }
    }

    private fun setupButtons(it: Date) {
        viewModel.pickerDate.value = it
        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
            .setSelection(viewModel.pickerDate.value!!.time.toLong())
        val picker: MaterialDatePicker<*> = builder.build()
        customer_birthday_input_edit.setOnClickListener {
            picker.show(requireActivity().supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                viewModel.setDate(it as Long)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            viewModel.photo.value = selectedPhotoUri.toString()
            viewModel.dataPhoto = data
            Glide.with(this).asBitmap().load(selectedPhotoUri).into(image_selected)
            edit_profile_customer_button_select_image_customer.visibility = View.GONE
            image_selected.visibility = View.VISIBLE
            image_selected.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Change Picture"), PICK_IMAGE_CODE)
            }
        }
    }

    private fun observeErrors(){
        viewModel.birthdayError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it != "") {
                    binding.birthdayErrorMessage.text = it
                    binding.birthdayErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.birthdayErrorMessage.text = ""
                    binding.birthdayErrorMessage.visibility = View.GONE
                }
            } ?: run {
                binding.birthdayErrorMessage.text = ""
                binding.birthdayErrorMessage.visibility = View.GONE
            }
        })

        viewModel.emailError.observe(viewLifecycleOwner, Observer {
            binding.customerEmailInputEdit.error =
                if (it != "") it else null
        })

        viewModel.dniError.observe(viewLifecycleOwner, Observer {
            binding.customerDniInputEdit.error =
                if (it != "") it else null
        })

        viewModel.phoneError.observe(viewLifecycleOwner, Observer {
            binding.customerPhoneInputEdit.error =
                if (it != "") it else null
        })

        viewModel.surnameError.observe(viewLifecycleOwner, Observer {
            binding.customerSurnameInputEdit.error =
                if (it != "") it else null
        })

        viewModel.nameError.observe(viewLifecycleOwner, Observer {
            binding.customerNameInputEdit.error =
                if (it != "") it else null
        })

    }

    private fun observeData(){
        viewModel.getCustomer.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    //TODO: showProgress()
                }
                is Resource.Success -> {
                    //TODO: hideProgress()
                    setupButtons(it.data.birthday)
                    if(it.data.photo != "" && it.data.photo != "DEFAULT_IMAGE" && it.data.photo != "DEFAULT_PHOTO"){
                        edit_profile_customer_button_select_image_customer.visibility = View.GONE
                        image_selected.visibility = View.VISIBLE
                        Glide.with(this).load(it.data.photo).into(image_selected)
                        image_selected.setOnClickListener {
                            val intent = Intent()
                            intent.type = "image/*"
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
                        }
                    }else{
                        edit_profile_customer_button_select_image_customer.visibility = View.VISIBLE
                        image_selected.visibility = View.GONE
                        edit_profile_customer_button_select_image_customer.setOnClickListener {
                            val intent = Intent()
                            intent.type = "image/*"
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
                        }
                    }
                }
                is Resource.Failure -> {
                    //TODO: hideProgress()
                }
            }
        })
        viewModel.customerDeleted.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    //TODO: hideProgress()
                    findNavController().navigate(EditDeleteCustomerTrainerFragmentDirections.actionEditDeleteCustomerTrainerFragmentToNavigationUsersTrainer())
                }
                false -> {
                    //TODO: hideProgress()
                    Toast.makeText(this.context, "Something went wrong", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }
        })

        viewModel.customerEdited.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    //TODO: hideProgress()
                    findNavController().navigate(EditDeleteCustomerTrainerFragmentDirections.actionEditDeleteCustomerTrainerFragmentToDisplayCustomer(customerId, viewModel.name.value!! + " " + viewModel.surname.value!!))
                }
                false -> {
                    //TODO: hideProgress()
                    Toast.makeText(this.context, "Something went wrong", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }
        })

    }
}
