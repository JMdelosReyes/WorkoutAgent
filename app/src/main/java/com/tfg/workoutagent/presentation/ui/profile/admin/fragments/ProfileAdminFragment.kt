package com.tfg.workoutagent.presentation.ui.profile.admin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.login.activities.GoogleSignInActivity
import com.tfg.workoutagent.presentation.ui.profile.admin.viewModels.ProfileAdminViewModel
import kotlinx.android.synthetic.main.fragment_admin_profile.*

class ProfileAdminFragment : Fragment() {

    private lateinit var profileAdminViewModel: ProfileAdminViewModel
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileAdminViewModel =
            ViewModelProvider(this@ProfileAdminFragment).get(ProfileAdminViewModel::class.java)
        return inflater.inflate(R.layout.fragment_admin_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI(){
        sign_out_button_admin.setOnClickListener { signOut2() }
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
