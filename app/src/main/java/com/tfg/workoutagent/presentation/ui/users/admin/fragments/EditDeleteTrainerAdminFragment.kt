package com.tfg.workoutagent.presentation.ui.users.admin.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentEditDeleteTrainerAdminBinding
import com.tfg.workoutagent.domain.userUseCases.ManageTrainerAdminUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.EditDeleteTrainerAdminViewModel
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.EditDeleteTrainerAdminViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.createAlertDialog
import com.tfg.workoutagent.vo.utils.parseStringToDateBar
import com.tfg.workoutagent.vo.utils.sendNotification
import kotlinx.android.synthetic.main.fragment_create_trainer_admin.*
import kotlinx.android.synthetic.main.fragment_edit_delete_trainer_admin.*
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EditDeleteTrainerAdminFragment : Fragment() {

    companion object{
        private val PICK_IMAGE_CODE = 4000
    }

    private val trainerId by lazy { EditDeleteTrainerAdminFragmentArgs.fromBundle(arguments!!).trainerId }
    private val trainerName by lazy { EditDeleteTrainerAdminFragmentArgs.fromBundle(arguments!!).nameTrainer }
    private val viewModel by lazy { ViewModelProvider(this, EditDeleteTrainerAdminViewModelFactory(trainerId, ManageTrainerAdminUseCaseImpl(UserRepositoryImpl()))).get(EditDeleteTrainerAdminViewModel::class.java) }
    private lateinit var binding : FragmentEditDeleteTrainerAdminBinding
    private var selectedPhotoUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_delete_trainer_admin,
            container,
            false)
        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return  this.binding.root
    }

       override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            viewModel.dataPhoto = data
            viewModel.photo.value = selectedPhotoUri.toString()
            Glide.with(this).asBitmap().load(selectedPhotoUri).into(image_selected_edit_trainer_admin)
            edit_profile_trainer_select_image_button.visibility = View.GONE
            image_selected_edit_trainer_admin.visibility = View.VISIBLE
            image_selected_edit_trainer_admin.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Change Picture"), PICK_IMAGE_CODE)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeData()
        observeErrors()



    }
    private fun setupPicker(it: Date) {
        viewModel.pickerDate.value = it
        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
            .setSelection(viewModel.pickerDate.value!!.time)
        val picker: MaterialDatePicker<*> = builder.build()
        trainer_birthday_input_edit_admin.setOnClickListener {
            picker.show(requireActivity().supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                viewModel.setDate(it as Long)
            }
        }
    }

    private fun setupUI(){
        image_selected_edit_trainer_admin.visibility = View.GONE
        edit_profile_trainer_select_image_button.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
        }

        delete_trainer_button_admin.setOnClickListener {
            createAlertDialog(
                context = this.context!!,
                title = getString(R.string.alert_title_delete_profile),
                message = getString(R.string.alert_message_delete),
                positiveAction = {
                    sendNotification(this.context!!, "$trainerName account has been deleted", "You may have to look for a new trainer", "/topics/customers_$trainerId")
                    findNavController().navigate(EditDeleteTrainerAdminFragmentDirections.actionEditDeleteTrainerAdminFragmentToDeleteTrainerSendMailFragment(trainerId))
                },
                negativeAction ={},
                positiveText = getString(R.string.answer_yes),
                negativeText = getString(R.string.answer_no)
            )
        }
    }

    private fun observeData(){
        viewModel.getTrainer.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    //TODO: showProgress()
                }
                is Resource.Success -> {
                    //TODO: hideProgress()
                    setupPicker(it.data.birthday)
                    if(it.data.photo != "" || it.data.photo != "DEFAULT_IMAGE" || it.data.photo != "DEFAULT_PHOTO"){
                        edit_profile_trainer_select_image_button.visibility = View.GONE
                        image_selected_edit_trainer_admin.visibility = View.VISIBLE
                        Glide.with(this).load(it.data.photo).into(image_selected_edit_trainer_admin)
                        image_selected_edit_trainer_admin.setOnClickListener {
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

        viewModel.trainerUpdated.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    //TODO: hideProgress()
                    findNavController().navigate(EditDeleteTrainerAdminFragmentDirections.actionEditDeleteTrainerAdminFragmentToNavigationAdminUsers())
                }
                false -> {
                    //TODO: hideProgress()
                    Toast.makeText(this.context, "Something went wrong", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }
        })

        viewModel.trainerDeleted.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    findNavController().navigate(EditDeleteTrainerAdminFragmentDirections.actionEditDeleteTrainerAdminFragmentToNavigationAdminUsers())
                }
                false ->  Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
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
            binding.trainerEmailInputEditAdmin.error =
                if (it != "") it else null
        })

        viewModel.dniError.observe(viewLifecycleOwner, Observer {
            binding.trainerDniInputEditAdmin.error =
                if (it != "") it else null
        })

        viewModel.phoneError.observe(viewLifecycleOwner, Observer {
            binding.trainerPhoneInputEditAdmin.error =
                if (it != "") it else null
        })

        viewModel.surnameError.observe(viewLifecycleOwner, Observer {
            binding.trainerSurnameInputEditAdmin.error =
                if (it != "") it else null
        })

        viewModel.nameError.observe(viewLifecycleOwner, Observer {
            binding.trainerNameInputEditAdmin.error =
                if (it != "") it else null
        })
    }
}
