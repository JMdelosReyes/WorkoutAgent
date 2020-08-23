package com.tfg.workoutagent.presentation.ui.profile.admin.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.tfg.workoutagent.AdminActivity
import com.tfg.workoutagent.PROFILE_ADMIN_FRAGMENT
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCaseImpl
import com.tfg.workoutagent.presentation.ui.login.activities.GoogleSignInActivity
import com.tfg.workoutagent.presentation.ui.login.activities.PREFERENCE_FILE_KEY
import com.tfg.workoutagent.presentation.ui.profile.admin.viewModels.ProfileAdminViewModel
import com.tfg.workoutagent.presentation.ui.profile.admin.viewModels.ProfileAdminViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.createAlertDialog
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import kotlinx.android.synthetic.main.dialog_settings_profile.view.*
import kotlinx.android.synthetic.main.fragment_admin_profile.*

class ProfileAdminFragment : Fragment() {

    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var viewModel: ProfileAdminViewModel
    private var darkMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_admin_profile, container, false)
        darkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
        viewModel = ViewModelProvider(
            this.requireActivity(), ProfileAdminViewModelFactory(
                DisplayProfileUserUseCaseImpl(
                    UserRepositoryImpl()
                )
            )
        ).get(ProfileAdminViewModel::class.java)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeData()
    }

    private fun setupUI() {
        sign_out_button_admin.setOnClickListener { signOut2() }
        settings_image_profile_admin.setOnClickListener {
            val dialogBuilder = MaterialAlertDialogBuilder(context!!)
            dialogBuilder.setTitle("Settings")
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_settings_profile, null)
            dialogBuilder.setView(dialogView)
            dialogView.dark_mode_switch.isChecked = darkMode
            dialogView.dark_mode_switch.setOnCheckedChangeListener { _, _ -> changeMode() }
            val alertDialog = dialogBuilder.create()
            dialogView.button_terms.setOnClickListener {
                alertDialog.dismiss()
                findNavController().navigate(ProfileAdminFragmentDirections.actionNavigationAdminProfileToTermsConditionsFragment2())
            }
            dialogView.button_qa.setOnClickListener {
                alertDialog.dismiss()
                findNavController().navigate(ProfileAdminFragmentDirections.actionNavigationAdminProfileToFaqsFragment2())
            }
            alertDialog.show()
        }
    }

    private fun changeMode() {
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        val sharedPreferences =
            activity?.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
        with(sharedPreferences?.edit()) {
            this!!.putBoolean("darkMode_sp", !darkMode)
            this.commit()
        }
        activity!!.finish()
        (activity!! as AdminActivity).restartActivityWithSelectedFragmentAdmin(
            PROFILE_ADMIN_FRAGMENT
        )
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

    private fun observeData() {
        viewModel.getProfileAdmin.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Glide.with(this).load(it.data.photo).into(circleImageViewAdmin_displayProfile)
                    display_admin_email_displayProfile.text = it.data.email
                    display_admin_phone_displayProfile.text = it.data.phone
                    display_admin_birthday_displayProfile.text =
                        parseDateToFriendlyDate(it.data.birthday)
                    display_admin_dni_displayProfile.text = it.data.dni
                    display_admin_name_displayProfile.text = it.data.name
                }
                is Resource.Failure -> {
                }
            }
        })
    }
}
