package com.tfg.workoutagent.presentation.ui.users.admin.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
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
import com.tfg.workoutagent.domain.userUseCases.ManageTrainerAdminUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.DisplayTrainerAdminViewModel
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.DisplayTrainerAdminViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import kotlinx.android.synthetic.main.fragment_display_trainer_admin.*

class DisplayTrainerAdminFragment : Fragment() {

    private val trainerId by lazy { DisplayTrainerAdminFragmentArgs.fromBundle(arguments!!).trainerId }
    private val trainerName by lazy { DisplayTrainerAdminFragmentArgs.fromBundle(arguments!!).nameTrainer }

    private val viewModel by lazy { ViewModelProvider(this, DisplayTrainerAdminViewModelFactory(trainerId, ManageTrainerAdminUseCaseImpl(UserRepositoryImpl()))).get(DisplayTrainerAdminViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_display_trainer_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupUI()
    }
    private fun observeData(){
        viewModel.getTrainer.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Loading -> {
                    //TODO: showProgress()
                }
                is Resource.Success -> {
                    //TODO: hideProgress()
                    display_trainer_birthday_admin.text = parseDateToFriendlyDate(it.data.birthday)
                    display_trainer_name_admin.text = it.data.name + " " + it.data.surname
                    display_trainer_email_admin.text = it.data.email
                    display_trainer_dni_admin.text = it.data.dni
                    display_trainer_phone_admin.text = it.data.phone
                    if(it.data.photo == "" || it.data.photo == "DEFAULT_IMAGE"){
                        Glide.with(this).load(R.drawable.ic_person_black_60dp).into(circleImageViewTrainer_admin)
                    }else{
                        Glide.with(this).load(it.data.photo).into(circleImageViewTrainer_admin)
                    }
                    if(it.data.academicTitle =="" || it.data.academicTitle =="DEFAULT_ACADEMIC_TITLE"){
                        display_trainer_button_curriculum_admin.setOnClickListener{
                            Toast.makeText(this.context, "This trainer hasn't uploaded a CV. Try again later", Toast.LENGTH_LONG).show()
                        }

                    }else{
                        display_trainer_button_curriculum_admin.setOnClickListener {_ ->
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(Uri.parse(it.data.academicTitle), "application/pdf")
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            val newIntent = Intent.createChooser(intent, "Open File")
                            try {
                                startActivity(newIntent)
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(this.context, "Please, install a PDF reader to visualize the CV", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                is Resource.Failure -> {
                    //TODO: hideProgress()
                    Toast.makeText(context, "${it}", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        })

    }

    private fun setupUI(){
        display_trainer_button_edit_admin.setOnClickListener {
            findNavController().navigate(DisplayTrainerAdminFragmentDirections.actionDisplayTrainerAdminFragmentToEditDeleteTrainerAdminFragment(trainerName, trainerId))
        }
    }
}
