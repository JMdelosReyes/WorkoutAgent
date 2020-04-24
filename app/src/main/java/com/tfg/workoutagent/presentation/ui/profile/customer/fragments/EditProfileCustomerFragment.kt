package com.tfg.workoutagent.presentation.ui.profile.customer.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentEditProfileCustomerBinding
import com.tfg.workoutagent.domain.profileUseCases.ManageProfileUseCaseImpl
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.EditProfileCustomerViewModel
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.EditProfileCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_edit_profile_customer.*

class EditProfileCustomerFragment : Fragment() {
    companion object{
        private val PICK_IMAGE_CODE = 2000
    }
    private val viewModel by lazy { ViewModelProvider(this, EditProfileCustomerViewModelFactory(ManageProfileUseCaseImpl(UserRepositoryImpl()))).get(EditProfileCustomerViewModel::class.java)}
    private lateinit var binding: FragmentEditProfileCustomerBinding
    private var selectedPhotoUri : Uri? = null

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
        observeData()
        setupUI()
    }

    private fun setupUI(){
        edit_profile_customer_button_select_image_customer.visibility = View.GONE
        delete_customer_button.setOnClickListener {  val builder = AlertDialog.Builder(this.context)
            builder.setTitle(getString(R.string.alert_title_delete_user))
            builder.setMessage(getString(R.string.alert_message_delete))

            builder.setPositiveButton(getString(R.string.answer_yes)) { dialog, _ ->
                dialog.dismiss()
                //viewModel.onDelete()
            }

            builder.setNeutralButton(getString(R.string.answer_no)) { dialog, _ ->
                dialog.dismiss()
            }
            builder.create()
            builder.show() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
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
                    Glide.with(this).load(it.data.photo).into(edit_profile_customer_image_selected)
                }
                is Resource.Failure -> {
                    //TODO: hideProgress()
                }
            }
        })
        viewModel.birthdayError.observe(viewLifecycleOwner, Observer {
            binding.editProfileCustomerBirthdayInputEdit.error =
                if (it != "") it else null
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
    }
}
