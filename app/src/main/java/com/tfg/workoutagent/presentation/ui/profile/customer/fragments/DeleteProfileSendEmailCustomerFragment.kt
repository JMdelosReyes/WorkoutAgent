package com.tfg.workoutagent.presentation.ui.profile.customer.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.profileUseCases.ManageProfileUseCaseImpl
import com.tfg.workoutagent.presentation.ui.login.activities.GoogleSignInActivity
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.DeleteProfileSendEmailCustomerViewModel
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.DeleteProfileSendEmailCustomerViewModelFactory
import com.tfg.workoutagent.vo.AppExecutors
import com.tfg.workoutagent.vo.Credentials
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_delete_profile_send_email_customer.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class DeleteProfileSendEmailCustomerFragment : Fragment() {
    private lateinit var emailUser : String
    private lateinit var nameUser : String
    private lateinit var emailAdmin : String
    private lateinit var emailTrainer : String
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val customerId by lazy { DeleteProfileSendEmailCustomerFragmentArgs.fromBundle(arguments!!).customerId }
    private val viewModel by lazy { ViewModelProvider(this, DeleteProfileSendEmailCustomerViewModelFactory(
        customerId, ManageProfileUseCaseImpl(
            UserRepositoryImpl()
        )))
        .get(DeleteProfileSendEmailCustomerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_delete_profile_send_email_customer,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupUI()
    }

    fun setupUI(){
        delete_profile_customer_button.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle(getString(R.string.alert_title_delete_profile))
            builder.setMessage(getString(R.string.alert_message_delete))

            builder.setPositiveButton(getString(R.string.answer_yes)) { dialog, _ ->
                viewModel.onDelete()
                sendMail()
                dialog.dismiss()
            }

            builder.setNeutralButton(getString(R.string.answer_no)) { dialog, _ ->
                dialog.dismiss()
            }
            builder.create()
            builder.show()
        }
    }

    private fun sendMail(){
        AppExecutors().diskIO().execute {
            val props = System.getProperties()
            props.put("mail.smtp.host", "smtp.gmail.com")
            props.put("mail.smtp.socketFactory.port", "465")
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            props.put("mail.smtp.auth", "true")
            props.put("mail.smtp.port", "465")

            val session =  Session.getInstance(props,
                object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(Credentials.EMAIL, Credentials.PASSWORD)
                    }
                })

            try {
                val mm = MimeMessage(session)
                mm.setFrom(InternetAddress(Credentials.EMAIL))
                mm.addRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(emailAdmin)
                )
                mm.addRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(emailTrainer)
                )
                mm.subject = "User ${nameUser} has deleted his profile"
                mm.setText("Reason:\n" +body_send_mail_delete_profile_customer_edit.text.toString())
                Transport.send(mm)
                AppExecutors().mainThread().execute {
                    sendSorryMessage()
                }

            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }
    }

    private fun sendSorryMessage(){
        AppExecutors().diskIO().execute {
            val props = System.getProperties()
            props.put("mail.smtp.host", "smtp.gmail.com")
            props.put("mail.smtp.socketFactory.port", "465")
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            props.put("mail.smtp.auth", "true")
            props.put("mail.smtp.port", "465")

            val session =  Session.getInstance(props,
                object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(Credentials.EMAIL, Credentials.PASSWORD)
                    }
                })

            try {
                val mm = MimeMessage(session)
                val emailId = emailUser
                mm.setFrom(InternetAddress(Credentials.EMAIL))
                //Adding receiver
                mm.addRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(emailId)
                )
                mm.subject = "Your account ${emailUser} has been deleted from WorkoutAgent App"
                mm.setText("Hi ${nameUser},\nWe're sorry to see you leave the workout agent. Please note that all information about you has been successfully deleted from our databases.\nWe hope you will return in the near future.\nGreetings.\n")
                Transport.send(mm)
                AppExecutors().mainThread().execute {
                    signOut2()
                }

            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }
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

    private fun observeData(){
        viewModel.getCustomer.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    emailUser = it.data.email
                    nameUser = it.data.name + " "+ it.data.surname
                    body_send_mail_delete_profile_customer_edit.setText("Hi, my name is ${it.data.name} ${it.data.surname},\nI would like to delete all the information about the user associated with this email as I have decided to delete the user profile that was in the WorkoutAgent app.\nGreetings.\n")
                }
                is Resource.Failure -> { }
            }
        })
        viewModel.getAdminEmail.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> emailAdmin = it.data
                is Resource.Failure -> {  }
            }
        })
        viewModel.getTrainerEmail.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> emailTrainer = it.data.email
                is Resource.Failure -> {  }
            }
        })
    }
}
