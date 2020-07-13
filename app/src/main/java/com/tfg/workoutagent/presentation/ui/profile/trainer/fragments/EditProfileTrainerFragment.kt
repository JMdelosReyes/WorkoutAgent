package com.tfg.workoutagent.presentation.ui.profile.trainer.fragments

import android.app.Activity
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
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.profileUseCases.ManageProfileUseCaseImpl
import com.tfg.workoutagent.databinding.FragmentEditProfileTrainerBinding
import com.tfg.workoutagent.presentation.ui.login.activities.GoogleSignInActivity
import com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels.EditProfileTrainerViewModel
import com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels.EditProfileTrainerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_edit_delete_trainer_admin.*
import kotlinx.android.synthetic.main.fragment_edit_profile_trainer.*
import kotlinx.android.synthetic.main.fragment_edit_profile_trainer.delete_trainer_button_admin

class EditProfileTrainerFragment : Fragment() {

    companion object{
        private val PICK_IMAGE_CODE = 4000
        private val PICK_PDF_CODE = 4001
    }

    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val viewModel by lazy { ViewModelProvider(this, EditProfileTrainerViewModelFactory(ManageProfileUseCaseImpl(UserRepositoryImpl()))).get(EditProfileTrainerViewModel::class.java) }
    private lateinit var binding : FragmentEditProfileTrainerBinding
    private var selectedPhotoUri : Uri? = null
    private var selectedPDFUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_profile_trainer,
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            viewModel.dataPhoto = data
            viewModel.photo.value = selectedPhotoUri.toString()
            Glide.with(this).asBitmap().load(selectedPhotoUri).into(image_selected_edit_profile_trainer)
            edit_profile_trainer_button_select_image.visibility = View.GONE
            image_selected_edit_profile_trainer.visibility = View.VISIBLE
            image_selected_edit_profile_trainer.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Change Picture"), PICK_IMAGE_CODE)
            }
        }
        if(requestCode == PICK_PDF_CODE && resultCode == Activity.RESULT_OK && data != null){
            selectedPDFUri = data.data
            viewModel.dataPDF = data
            viewModel.academicTitle.value = selectedPDFUri.toString()
        }
    }

    private fun setupUI(){
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
        upload_cv_trainer_button_admin.setOnClickListener {
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Change CV"), PICK_PDF_CODE)
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
                    if(it.data.photo != "" || it.data.photo != "DEFAULT_IMAGE"){
                        edit_profile_trainer_button_select_image.visibility = View.GONE
                        image_selected_edit_profile_trainer.visibility = View.VISIBLE
                        Glide.with(this).load(it.data.photo).into(image_selected_edit_profile_trainer)
                        image_selected_edit_profile_trainer.setOnClickListener {
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
        viewModel.trainerDeleted.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    //TODO: hideProgress()
                    singOutAfterDelete()
                }
                false -> {
                    //TODO: hideProgress()
                    Toast.makeText(this.context, "Something went wrong", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }
        })
        viewModel.trainerUpdated.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    //TODO: hideProgress()
                    findNavController().navigate(EditProfileTrainerFragmentDirections.actionEditProfileTrainerFragmentToNavigationProfileTrainer())
                }
                false -> {
                    //TODO: hideProgress()
                    Toast.makeText(this.context, "Something went wrong", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }
        })
    }

    private fun singOutAfterDelete(){
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this.activity!!, mGoogleSignInOptions)
        FirebaseAuth.getInstance().signOut()
        mGoogleSignInClient.revokeAccess()
        startActivity(GoogleSignInActivity.getLaunchIntent(this.context!!))
    }
    private fun observeErrors(){
        viewModel.surnameError.observe(viewLifecycleOwner, Observer {
            binding.trainerSurnameInputProfile.error =
                if (it != "") it else null
        })

        viewModel.nameError.observe(viewLifecycleOwner, Observer {
            binding.trainerNameInputProfile.error =
                if (it != "") it else null
        })

        viewModel.birthdayError.observe(viewLifecycleOwner, Observer {
            binding.trainerBirthdayInputProfile.error =
                if (it != "") it else null
        })

        viewModel.emailError.observe(viewLifecycleOwner, Observer {
            binding.trainerEmailInputProfile.error =
                if (it != "") it else null
        })

        viewModel.dniError.observe(viewLifecycleOwner, Observer {
            binding.trainerDniInputProfile.error =
                if (it != "") it else null
        })

        viewModel.phoneError.observe(viewLifecycleOwner, Observer {
            binding.trainerPhoneInputProfile.error =
                if (it != "") it else null
        })
    }
}
