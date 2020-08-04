package com.tfg.workoutagent.presentation.ui.profile.customer.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tfg.workoutagent.CustomerActivity
import com.tfg.workoutagent.PROFILE_CUSTOMER_FRAGMENT
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCaseImpl
import com.tfg.workoutagent.presentation.ui.login.activities.GoogleSignInActivity
import com.tfg.workoutagent.presentation.ui.login.activities.PREFERENCE_FILE_KEY
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.ProfileCustomerViewModel
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.ProfileCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import kotlinx.android.synthetic.main.dialog_settings_profile.view.*
import kotlinx.android.synthetic.main.fragment_customer_profile.*

class ProfileCustomerFragment : Fragment() {

    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private var darkMode: Boolean = false

    private val viewModel by lazy {
        ViewModelProvider(
            this, ProfileCustomerViewModelFactory(
                DisplayProfileUserUseCaseImpl(
                    UserRepositoryImpl()
                )
            )
        ).get(ProfileCustomerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        darkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
        return inflater.inflate(R.layout.fragment_customer_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeData()
    }

    private fun setupUI() {
        sign_out_button.setOnClickListener { signOut2() }
        display_customer_button_edit.setOnClickListener {
            findNavController().navigate(
                ProfileCustomerFragmentDirections.actionNavigationProfileCustomerToEditProfileCustomerFragment()
            )
        }
        display_customer_button_nutrition.setOnClickListener {
            findNavController().navigate(
                ProfileCustomerFragmentDirections.actionNavigationProfileCustomerToDisplayNutritionCustomerFragment()
            )
        }
        display_customer_button_evolution.setOnClickListener {
            findNavController().navigate(
                ProfileCustomerFragmentDirections.actionNavigationProfileCustomerToListGoalCustomerFragment()
            )
        }
        settings_image_profile_customer.setOnClickListener {
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
                findNavController().navigate(ProfileCustomerFragmentDirections.actionNavigationProfileCustomerToTermsConditionsFragment())
            }
            dialogView.button_qa.setOnClickListener {
                alertDialog.dismiss()
                findNavController().navigate(ProfileCustomerFragmentDirections.actionNavigationProfileCustomerToFaqsFragment3())
            }
            alertDialog.show()
        }
    }

    private fun observeData() {
        viewModel.getProfileCustomer.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Glide.with(this).load(it.data.photo)
                        .into(circleImageViewCustomer_displayProfile)
                    display_customer_name_displayProfile.text = it.data.name + " " + it.data.surname
                    display_customer_email_displayProfile.text = it.data.email
                    display_customer_phone_displayProfile.text = it.data.phone
                    display_customer_height_displayProfile.text = it.data.height.toString() + " cm"
                    display_customer_weight_displayProfile.text =
                        it.data.weights[0].weight.toString() + " kg"
                    display_customer_birthday_displayProfile.text =
                        parseDateToFriendlyDate(it.data.birthday)
                    display_customer_dni_displayProfile.text = it.data.dni
                }
                is Resource.Failure -> {
                }
            }
        })
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
        (activity!! as CustomerActivity).restartActivityWithSelectedFragmentCustomer(
            PROFILE_CUSTOMER_FRAGMENT
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
