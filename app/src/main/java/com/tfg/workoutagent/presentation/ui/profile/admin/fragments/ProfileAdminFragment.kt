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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.tfg.workoutagent.AdminActivity
import com.tfg.workoutagent.PROFILE_ADMIN_FRAGMENT
import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.login.activities.GoogleSignInActivity
import com.tfg.workoutagent.presentation.ui.login.activities.PREFERENCE_FILE_KEY
import com.tfg.workoutagent.presentation.ui.profile.admin.viewModels.ProfileAdminViewModel
import com.tfg.workoutagent.presentation.ui.profile.customer.fragments.ProfileCustomerFragmentDirections
import kotlinx.android.synthetic.main.dialog_settings_profile.view.*
import kotlinx.android.synthetic.main.fragment_admin_profile.*

class ProfileAdminFragment : Fragment() {

    private lateinit var profileAdminViewModel: ProfileAdminViewModel
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private var darkMode : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileAdminViewModel =
            ViewModelProvider(this@ProfileAdminFragment).get(ProfileAdminViewModel::class.java)
        darkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
        return inflater.inflate(R.layout.fragment_admin_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI(){
        sign_out_button_admin.setOnClickListener { signOut2() }
        settings_image_profile_admin.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context!!)
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
            alertDialog.show()
        }
    }

    private fun changeMode(){
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        val sharedPreferences = activity?.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
        with(sharedPreferences?.edit()){
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
}
