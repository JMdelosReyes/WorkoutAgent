package com.tfg.workoutagent.presentation.ui.profile.trainer.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCaseImpl
import com.tfg.workoutagent.presentation.ui.login.activities.GoogleSignInActivity
import com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels.ProfileTrainerViewModel
import com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels.ProfileTrainerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import kotlinx.android.synthetic.main.fragment_trainer_profile.*

class ProfileTrainerFragment : Fragment() {

    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var urlAT : String
    private val viewModel by lazy { ViewModelProvider(
        this, ProfileTrainerViewModelFactory(
            DisplayProfileUserUseCaseImpl(
                UserRepositoryImpl()
            )
        )
    ).get(ProfileTrainerViewModel::class.java)}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trainer_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeData()
    }

    private fun setupUI() {
        sign_out_button.setOnClickListener { signOut2() }
        display_trainer_button_edit.setOnClickListener { findNavController().navigate(ProfileTrainerFragmentDirections.actionNavigationProfileTrainerToEditProfileTrainerFragment()) }
        display_trainer_button_evolution.setOnClickListener {  }
    }

    private fun observeData(){
        viewModel.getProfileTrainer.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    Glide.with(this).load(it.data.photo).into(circleImageViewTrainer_displayProfile)
                    display_trainer_name_displayProfile.text = it.data.name + " " + it.data.surname
                    display_trainer_email_displayProfile.text = it.data.email
                    display_trainer_phone_displayProfile.text = it.data.phone
                    display_trainer_birthday_displayProfile.text = parseDateToFriendlyDate(it.data.birthday)
                    display_trainer_dni_displayProfile.text = it.data.dni
                    urlAT = it.data.academicTitle
                    display_trainer_button_curriculum.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(Uri.parse(urlAT), "application/pdf")
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        val newIntent = Intent.createChooser(intent, "Open File")
                        try {
                            startActivity(newIntent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(this.context, "Please, install a PDF reader to visualize the CV", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                is Resource.Failure -> {
                    Log.i("ERROR", it.exception.toString())
                }
            }
        })
    }

    private fun signOut2() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this.activity!!, mGoogleSignInOptions)
        FirebaseAuth.getInstance().signOut()
        mGoogleSignInClient.revokeAccess()
        startActivity(GoogleSignInActivity.getLaunchIntent(this.context!!))
    }
}
