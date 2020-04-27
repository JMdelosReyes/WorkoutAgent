package com.tfg.workoutagent.presentation.ui.users.admin.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseFragment
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentCreateTrainerAdminBinding
import com.tfg.workoutagent.domain.userUseCases.ManageTrainerAdminUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.CreateTrainerAdminViewModel
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.CreateTrainerAdminViewModelFactory
import kotlinx.android.synthetic.main.fragment_create_trainer_admin.*

class CreateTrainerAdminFragment : BaseFragment() {

    companion object{
        private val PICK_IMAGE_CODE = 3000
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this, CreateTrainerAdminViewModelFactory(
                ManageTrainerAdminUseCaseImpl(
                    UserRepositoryImpl())))
            .get(CreateTrainerAdminViewModel::class.java) }

    private lateinit var binding : FragmentCreateTrainerAdminBinding
    private var selectedPhotoUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_trainer_admin, container, false)

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return this.binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            Glide.with(this).asBitmap().load(selectedPhotoUri).into(image_selected_create_trainer_admin)
            create_trainer_button_select_image_admin.visibility = View.GONE
            image_selected_create_trainer_admin.visibility = View.VISIBLE
            image_selected_create_trainer_admin.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Change Picture"), PICK_IMAGE_CODE)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        observeErrors()
        setupUI()
    }

    private fun setupUI(){
        image_selected_create_trainer_admin.visibility = View.GONE
        create_trainer_button_select_image_admin.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
        }
    }

    private fun observeData(){
        viewModel.createdTrainer.observe(viewLifecycleOwner, Observer {
            when(it){
                //TODO: Esconder la progressBar
                true -> {
                    findNavController().navigate(CreateTrainerAdminFragmentDirections.actionCreateTrainerAdminFragmentToNavigationAdminUsers())
                }
                false -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeErrors(){
        viewModel.surnameError.observe(viewLifecycleOwner, Observer {
            binding.trainerSurnameInputEdit.error =
                if (it != "") it else null
        })

        viewModel.nameError.observe(viewLifecycleOwner, Observer {
            binding.trainerNameInputEdit.error =
                if (it != "") it else null
        })

        viewModel.birthdayError.observe(viewLifecycleOwner, Observer {
            binding.trainerBirthdayInputEdit.error =
                if (it != "") it else null
        })

        viewModel.emailError.observe(viewLifecycleOwner, Observer {
            binding.trainerEmailInputEdit.error =
                if (it != "") it else null
        })

        viewModel.dniError.observe(viewLifecycleOwner, Observer {
            binding.trainerDniInputEdit.error =
                if (it != "") it else null
        })

        viewModel.phoneError.observe(viewLifecycleOwner, Observer {
            binding.trainerPhoneInputEdit.error =
                if (it != "") it else null
        })
    }

}
