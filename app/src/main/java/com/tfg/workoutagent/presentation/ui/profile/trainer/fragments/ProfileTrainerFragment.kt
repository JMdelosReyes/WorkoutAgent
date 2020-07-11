package com.tfg.workoutagent.presentation.ui.profile.trainer.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.tfg.workoutagent.PREFERENCE_FILE_KEY_TRAINER
import com.tfg.workoutagent.PROFILE_TRAINER_FRAGMENT
import com.tfg.workoutagent.R
import com.tfg.workoutagent.TrainerActivity
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCaseImpl
import com.tfg.workoutagent.presentation.ui.login.activities.GoogleSignInActivity
import com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels.ProfileTrainerViewModel
import com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels.ProfileTrainerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import kotlinx.android.synthetic.main.dialog_settings_trainer.view.dark_mode_switch
import kotlinx.android.synthetic.main.fragment_trainer_profile.*


class ProfileTrainerFragment : Fragment() {

    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var viewModel : ProfileTrainerViewModel
    private var darkMode : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_trainer_profile, container, false)
        darkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
        viewModel = ViewModelProvider(
            this, ProfileTrainerViewModelFactory(darkMode,
                DisplayProfileUserUseCaseImpl(
                    UserRepositoryImpl()
                )
            )
        ).get(ProfileTrainerViewModel::class.java)
        return root
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
        settings_image_profile_trainer.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context!!)
            dialogBuilder.setTitle("Settings")
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_settings_trainer, null)
            dialogBuilder.setView(dialogView)
            dialogView.dark_mode_switch.isChecked = darkMode
            dialogView.dark_mode_switch.setOnCheckedChangeListener { _, _ -> changeMode() }
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun changeMode(){
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        val sharedPreferences = activity?.getSharedPreferences(PREFERENCE_FILE_KEY_TRAINER, Context.MODE_PRIVATE)
        with(sharedPreferences?.edit()){
            this!!.putBoolean("darkMode_sp", !darkMode)
            this.commit()
        }
        val written = sharedPreferences?.getBoolean("darkMode_sp", false)
        activity!!.finish()
        (activity!! as TrainerActivity).restartActivityWithSelectedFragment(
            PROFILE_TRAINER_FRAGMENT
        )
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
                    display_trainer_button_curriculum.setOnClickListener {_ ->
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
                is Resource.Failure -> {
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
