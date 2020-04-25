package com.tfg.workoutagent.presentation.ui.users.admin.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentEditDeleteTrainerAdminBinding
import com.tfg.workoutagent.domain.userUseCases.ManageTrainerAdminUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.EditDeleteTrainerAdminViewModel
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.EditDeleteTrainerAdminViewModelFactory
import kotlinx.android.synthetic.main.fragment_create_trainer_admin.*
import kotlinx.android.synthetic.main.fragment_edit_delete_trainer_admin.*

class EditDeleteTrainerAdminFragment : Fragment() {

    companion object{
        private val PICK_IMAGE_CODE = 4000
    }

    private val trainerId by lazy { EditDeleteTrainerAdminFragmentArgs.fromBundle(arguments!!).trainerId }
    private val trainerName by lazy { EditDeleteTrainerAdminFragmentArgs.fromBundle(arguments!!).nameTrainer }
    private val viewModel by lazy { ViewModelProvider(this, EditDeleteTrainerAdminViewModelFactory("", ManageTrainerAdminUseCaseImpl(UserRepositoryImpl()))).get(EditDeleteTrainerAdminViewModel::class.java) }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeData()
        observeErrors()
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

    private fun observeData(){
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
            binding.trainerBirthdayInputEditAdmin.error =
                if (it != "") it else null
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
