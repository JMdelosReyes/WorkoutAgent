package com.tfg.workoutagent.presentation.ui.users.admin.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.userUseCases.ManageTrainerAdminUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.EditDeleteTrainerAdminViewModel
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.EditDeleteTrainerAdminViewModelFactory
import com.tfg.workoutagent.vo.AppExecutors
import com.tfg.workoutagent.vo.Credentials
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.createAlertDialog
import kotlinx.android.synthetic.main.fragment_delete_trainer_send_mail.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class DeleteTrainerSendMailFragment : Fragment() {

    private val trainerId by lazy { DeleteTrainerSendMailFragmentArgs.fromBundle(arguments!!).trainerId }
    lateinit var emailUser : String
    lateinit var nameUser : String
    private val viewModel by lazy {
        ViewModelProvider(this, EditDeleteTrainerAdminViewModelFactory(trainerId, ManageTrainerAdminUseCaseImpl(
            UserRepositoryImpl()
        )
        )).get(EditDeleteTrainerAdminViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delete_trainer_send_mail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeData()
    }

    fun setupUI(){
        delete_trainer_button_admin.setOnClickListener {
            createAlertDialog(
                context = this.context!!,
                title = getString(R.string.alert_title_delete_profile),
                message = getString(R.string.alert_message_delete),
                positiveAction = {
                    sendMail()
                    viewModel.onDelete()
                },
                negativeAction ={},
                positiveText = getString(R.string.answer_yes),
                negativeText = getString(R.string.answer_no)
            )
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
                    //Authenticating the password
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(Credentials.EMAIL, Credentials.PASSWORD)
                    }
                })

            try {
                //Creating MimeMessage object
                val mm = MimeMessage(session)
                val emailId = emailUser
                //Setting sender address
                mm.setFrom(InternetAddress(Credentials.EMAIL))
                //Adding receiver
                mm.addRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(emailId)
                )
                //Adding subject
                mm.subject = "User ${nameUser} was deleted from WorkoutAgent App"
                //Adding message
                mm.setText(body_send_mail_delete_trainer_edit.text.toString())

                //Sending email
                Transport.send(mm)

                AppExecutors().mainThread().execute {
                    //Something that should be executed on main thread.
                    findNavController().navigate(DeleteTrainerSendMailFragmentDirections.actionDeleteTrainerSendMailFragmentToNavigationAdminUsers())
                }

            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }
    }
    private fun observeData(){
        viewModel.getTrainer.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    emailUser = it.data.email
                    nameUser = it.data.name + " "+ it.data.surname
                    body_send_mail_delete_trainer_edit.setText("Hi ${it.data.name} ${it.data.surname},\n your user has been deleted from WorkoutAgent App.\nIf you have not done this action, please contact with administration of WorkoutAgent App using our email workoutagentapp@gmail.com.\nGreetings.\nStaff of WorkoutAgent App.")
                }
                is Resource.Failure -> {

                }
            }
        })
    }
}
