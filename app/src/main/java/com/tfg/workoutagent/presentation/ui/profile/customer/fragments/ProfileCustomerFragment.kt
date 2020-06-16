package com.tfg.workoutagent.presentation.ui.profile.customer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.ProfileCustomerViewModel
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.ProfileCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import kotlinx.android.synthetic.main.fragment_customer_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile_customer.*

class ProfileCustomerFragment : Fragment() {

    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val viewModel by lazy {
        ViewModelProvider(this, ProfileCustomerViewModelFactory(
            DisplayProfileUserUseCaseImpl(
                UserRepositoryImpl()
            )
        )).get(ProfileCustomerViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_customer_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeData()
    }

    private fun setupUI() {
        sign_out_button.setOnClickListener { signOut2() }
        display_customer_button_edit.setOnClickListener { findNavController().navigate(ProfileCustomerFragmentDirections.actionNavigationProfileCustomerToEditProfileCustomerFragment()) }
        display_customer_button_nutrition.setOnClickListener { findNavController().navigate(ProfileCustomerFragmentDirections.actionNavigationProfileCustomerToDisplayNutritionCustomerFragment()) }
    }

    private fun observeData(){
        viewModel.getProfileCustomer.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    Glide.with(this).load(it.data.photo).into(circleImageViewCustomer_displayProfile)
                    display_customer_name_displayProfile.text = it.data.name + " " + it.data.surname
                    display_customer_email_displayProfile.text = it.data.email
                    display_customer_phone_displayProfile.text = it.data.phone
                    display_customer_height_displayProfile.text = it.data.height.toString() + " cm"
                    display_customer_weight_displayProfile.text = it.data.weights[0].weight.toString() + " kg"
                    display_customer_birthday_displayProfile.text = parseDateToFriendlyDate(it.data.birthday)
                    display_customer_dni_displayProfile.text = it.data.dni
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
