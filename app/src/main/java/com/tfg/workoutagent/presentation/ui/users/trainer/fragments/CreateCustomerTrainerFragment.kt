package com.tfg.workoutagent.presentation.ui.users.trainer.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseFragment
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentCreateCustomerTrainerBinding
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.CreateCustomerTrainerViewModel
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.CreateCustomerTrainerViewModelFactory
import kotlinx.android.synthetic.main.fragment_create_customer_trainer.*
import kotlinx.android.synthetic.main.fragment_create_customer_trainer.customer_birthday_input_edit
import kotlinx.android.synthetic.main.fragment_create_customer_trainer.edit_profile_customer_button_select_image_customer
import kotlinx.android.synthetic.main.fragment_create_customer_trainer.image_selected
import kotlinx.android.synthetic.main.fragment_edit_delete_customer_trainer.*
import java.util.*


class CreateCustomerTrainerFragment : BaseFragment() {

    companion object{
        private val PICK_IMAGE_CODE = 1000
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this, CreateCustomerTrainerViewModelFactory(
                ManageCustomerTrainerUseCaseImpl(UserRepositoryImpl())
            )
        ).get(CreateCustomerTrainerViewModel::class.java)
    }

    private lateinit var binding : FragmentCreateCustomerTrainerBinding
    private var selectedPhotoUri : Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_customer_trainer,
            container,
            false
        )

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        observeErrors()
        setupButtons()
        image_selected.visibility = View.GONE
        edit_profile_customer_button_select_image_customer.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
        }
        radioGrpSex_customer.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == radioM.id){
               viewModel.genre = "M"
            }else if(checkedId == radioF.id){
                viewModel.genre = "F"
            }
        }
    }

    private fun setupButtons() {
        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
            .setSelection(viewModel.pickerDate.value!!.time)
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
            binding.customerBirthdayInputEdit.error =
                if (it != "") it else null
        })

        viewModel.emailError.observe(viewLifecycleOwner, Observer {
            binding.customerEmailInputEdit.error =
                if (it != "") it else null
        })

        viewModel.heightError.observe(viewLifecycleOwner, Observer {
            binding.customerHeightInputEdit.error =
                if (it != "") it else null
        })

        viewModel.initialWeightError.observe(viewLifecycleOwner, Observer {
            binding.customerWeightInputEdit.error =
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

        viewModel.genreError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it != "") {
                    binding.selectSexErrorMessage.text = it
                    binding.selectSexErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.selectSexErrorMessage.text = ""
                    binding.selectSexErrorMessage.visibility = View.GONE
                }
            } ?: run {
                binding.selectSexErrorMessage.text = ""
                binding.selectSexErrorMessage.visibility = View.GONE
            }
        })
    }
    private fun observeData(){

        viewModel.customerCreated.observe(viewLifecycleOwner, Observer {
            when(it){
                //TODO: Esconder la progressBar
                true -> {
                    findNavController().navigate(
                        CreateCustomerTrainerFragmentDirections.actionCreateCustomerTrainerFragmentToNavigationUsersTrainer2()
                    )
                }
                false -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
