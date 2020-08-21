package com.tfg.workoutagent.presentation.ui.profile.customer.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentEditProfileCustomerBinding
import com.tfg.workoutagent.domain.profileUseCases.ManageProfileUseCaseImpl
import com.tfg.workoutagent.presentation.ui.login.activities.GoogleSignInActivity
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.EditProfileCustomerViewModel
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.EditProfileCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_edit_profile_customer.*
import java.util.*

class EditProfileCustomerFragment : Fragment() {
    companion object{
        private const val PICK_IMAGE_CODE = 2000
    }
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val viewModel by lazy { ViewModelProvider(this, EditProfileCustomerViewModelFactory(ManageProfileUseCaseImpl(UserRepositoryImpl()))).get(EditProfileCustomerViewModel::class.java)}
    private lateinit var binding: FragmentEditProfileCustomerBinding
    private var selectedPhotoUri : Uri? = null
    private lateinit var customerId : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_profile_customer,
            container,
            false
        )
        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeData()
        observeErrors()
    }
    private fun setupUI(){
        delete_customer_button.setOnClickListener {  val builder = AlertDialog.Builder(this.context)
            builder.setTitle(getString(R.string.alert_title_delete_profile))
            builder.setMessage(getString(R.string.alert_message_delete))

            builder.setPositiveButton(getString(R.string.answer_yes)) { dialog, _ ->
                findNavController().navigate(EditProfileCustomerFragmentDirections.actionEditProfileCustomerFragmentToDeleteProfileSendEmailCustomerFragment(customerId))
                dialog.dismiss()
            }

            builder.setNeutralButton(getString(R.string.answer_no)) { dialog, _ ->
                dialog.dismiss()
            }
            builder.create()
            builder.show()
        }
    }

    private fun setupButtons(it: Date) {
        viewModel.pickerDate.value = it
        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
            .setSelection(viewModel.pickerDate.value!!.time.toLong())
        val picker: MaterialDatePicker<*> = builder.build()
        edit_profile_customer_birthday_input_edit.setOnClickListener {
            picker.show(requireActivity().supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                viewModel.setDate(it as Long)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            viewModel.dataPhoto = data
            viewModel.photo.value = selectedPhotoUri.toString()
            Glide.with(this).asBitmap().load(selectedPhotoUri).into(edit_profile_customer_image_selected)
            edit_profile_customer_button_select_image_customer.visibility = View.GONE
            edit_profile_customer_image_selected.visibility = View.VISIBLE
            edit_profile_customer_image_selected.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Change Picture"), PICK_IMAGE_CODE)
            }
        }
    }

    private fun observeData(){
        viewModel.getCustomer.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    //TODO: showProgress()
                }
                is Resource.Success -> {
                    //TODO: hideProgress()
                    setupButtons(it.data.birthday)
                    customerId = it.data.id
                    if(it.data.photo != "" || it.data.photo != "DEFAULT_IMAGE" || it.data.photo != "DEFAULT_PHOTO"){
                        edit_profile_customer_button_select_image_customer.visibility = View.GONE
                        edit_profile_customer_image_selected.visibility = View.VISIBLE
                        Glide.with(this).load(it.data.photo).into(edit_profile_customer_image_selected)
                        edit_profile_customer_image_selected.setOnClickListener {
                            val intent = Intent()
                            intent.type = "image/*"
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
                        }
                    }else{
                        edit_profile_customer_button_select_image_customer.setOnClickListener {
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

        viewModel.customerEdited.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    findNavController().navigate(EditProfileCustomerFragmentDirections.actionEditProfileCustomerFragmentToNavigationProfileCustomer())
                    Toast.makeText(this.context, "Customer edited successfully!", Toast.LENGTH_LONG).show()
                }
                false -> {
                    Toast.makeText(this.context, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.customerDeleted.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> singOutAfterDelete()
                false -> {
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
        viewModel.birthdayError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it != "") {
                    binding.birthdayErrorMessage.text = it
                    binding.birthdayErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.birthdayErrorMessage.text = ""
                    binding.birthdayErrorMessage.visibility = View.GONE
                }
            } ?: run {
                binding.birthdayErrorMessage.text = ""
                binding.birthdayErrorMessage.visibility = View.GONE
            }
        })

        viewModel.emailError.observe(viewLifecycleOwner, Observer {
            binding.editProfileCustomerEmailInputEdit.error =
                if (it != "") it else null
        })

        viewModel.dniError.observe(viewLifecycleOwner, Observer {
            binding.editProfileCustomerDniInputEdit.error =
                if (it != "") it else null
        })

        viewModel.phoneError.observe(viewLifecycleOwner, Observer {
            binding.editProfileCustomerPhoneInputEdit.error =
                if (it != "") it else null
        })

        viewModel.surnameError.observe(viewLifecycleOwner, Observer {
            binding.editProfileCustomerSurnameInputEdit.error =
                if (it != "") it else null
        })

        viewModel.nameError.observe(viewLifecycleOwner, Observer {
            binding.editProfileCustomerNameInputEdit.error =
                if (it != "") it else null
        })
        viewModel.heightError.observe(viewLifecycleOwner, Observer {
            binding.customerHeightInputEdit.error =
                if (it != "") it else null
        })
    }
}
