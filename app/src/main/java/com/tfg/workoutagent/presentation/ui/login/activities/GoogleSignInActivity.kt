package com.tfg.workoutagent.presentation.ui.login.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton.COLOR_DARK
import com.google.android.gms.common.SignInButton.COLOR_LIGHT
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.tfg.workoutagent.AdminActivity
import com.tfg.workoutagent.CustomerActivity
import com.tfg.workoutagent.R
import com.tfg.workoutagent.TrainerActivity
import com.tfg.workoutagent.base.BaseActivity
import com.tfg.workoutagent.data.repositoriesImpl.LoginRepositoryImpl
import com.tfg.workoutagent.domain.loginUseCases.LoginUseCaseImpl
import com.tfg.workoutagent.presentation.ui.login.viewmodels.LoginViewModel
import com.tfg.workoutagent.presentation.ui.login.viewmodels.LoginViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.login_activity.*

const val PREFERENCE_FILE_KEY = "SharedPreferenceKey"

class GoogleSignInActivity : BaseActivity() {

    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    private lateinit var firebaseAuth: FirebaseAuth
    private val viewModel by lazy { ViewModelProvider(this, LoginViewModelFactory(LoginUseCaseImpl(LoginRepositoryImpl()))).get(LoginViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        val sharedPref: SharedPreferences = this.getSharedPreferences(
            PREFERENCE_FILE_KEY,
            Context.MODE_PRIVATE
        )
        val darkModeBool = sharedPref.getBoolean("darkMode_sp", false)
        if(darkModeBool){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        configureGoogleSignIn()
        setupUI(darkModeBool)

        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }
    private fun setupUI(darkMode: Boolean) {
        if(darkMode){
            logo_image.setImageResource(R.drawable.darklogo)
            google_button.setColorScheme(COLOR_DARK)
        }else{
            logo_image.setImageResource(R.drawable.logo)
            google_button.setColorScheme(COLOR_LIGHT)
        }
        google_button.setOnClickListener {
            signIn()
        }
    }
    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            try {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                this.viewModel.reloadRole()
                viewModel.fetchRole.observe(this, Observer { result ->
                    when(result){
                        is Resource.Loading -> {
                            //showProgress()
                        }
                        is Resource.Success -> {
                            //hideProgress()
                            if(result.data != "NO_ACCOUNT") {
                                viewModel.fetchToken.observe(this, Observer { r ->
                                    when (r) {
                                        is Resource.Loading -> {
                                            //showProgress()
                                        }
                                        is Resource.Success -> {
                                            //hideProgress()
                                        }
                                        is Resource.Failure -> {
                                            //hideProgress()
                                            //Toast.makeText(this, "Cannot update this token in Firebase", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                })
                            }

                            when(result.data){
                                "TRAINER" -> {
                                    viewModel.loggedTrainer.observe(this, Observer { r1 ->
                                        when(r1){
                                            is Resource.Loading -> {
                                                //showProgress()
                                            }
                                            is Resource.Success -> {
                                                val myId = r1.data.id
                                                FirebaseMessaging.getInstance().subscribeToTopic("/topics/trainer_$myId")
                                                startActivity(TrainerActivity.getLaunchIntent(this))
                                                finish()
                                            }
                                            is Resource.Failure -> {
                                                startActivity(TrainerActivity.getLaunchIntent(this))
                                                finish()
                                            }
                                        }
                                    })
                                }
                                "CUSTOMER" -> {
                                    viewModel.trainer.observe(this, Observer { r1 ->
                                        when(r1){
                                            is Resource.Loading -> {
                                                //showProgress()
                                            }
                                            is Resource.Success -> {
                                                if(r1.data.id == "DEFAULT_ID"){
                                                    startActivity(CustomerActivity.getLaunchIntent(this))
                                                    finish()
                                                }else{
                                                    val trainerId = r1.data.id
                                                    FirebaseMessaging.getInstance().subscribeToTopic("/topics/customers_$trainerId")
                                                    startActivity(CustomerActivity.getLaunchIntent(this))
                                                    finish()
                                                }
                                            }
                                            is Resource.Failure -> {
                                                startActivity(CustomerActivity.getLaunchIntent(this))
                                                finish()
                                            }
                                        }
                                    })
                                }
                                "ADMIN" -> {
                                    FirebaseMessaging.getInstance().subscribeToTopic("/topics/admin")
                                    startActivity(AdminActivity.getLaunchIntent(this))
                                    finish()
                                }
                                "NO_ACCOUNT" -> {
                                    mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build()
                                    mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
                                    firebaseAuth.signOut()
                                    mGoogleSignInClient.revokeAccess()
                                    this.viewModel.setNullRole()
                                    Toast.makeText(this, "Cannot find this user in Firebase", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        is Resource.Failure -> {
                            //hideProgress()
                            mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build()
                            mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
                            firebaseAuth.signOut()
                            mGoogleSignInClient.revokeAccess()
                            this.viewModel.setNullRole()
                            //Toast.makeText(this, "Cannot find this user in Firebase 2", Toast.LENGTH_LONG).show()
                        }
                    }
                })

            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        try {
            this.viewModel.reloadRole()
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                viewModel.fetchRole.observe(this, Observer { result ->
                    when(result){
                        is Resource.Loading -> {
                            //showProgress()
                        }
                        is Resource.Success -> {
                            val role = result.data
                            //hideProgress()
                            viewModel.fetchToken.observe(this, Observer { r ->
                                when(r){
                                    is Resource.Loading -> {
                                        //showProgress()
                                    }
                                    is Resource.Success -> {
                                        //hideProgress()
                                    }
                                    is Resource.Failure -> {
                                        //hideProgress()
                                        //Toast.makeText(this, "Cannot update this token in Firebase", Toast.LENGTH_LONG).show()
                                    }
                                }
                            })
                            when(role){
                                "TRAINER" -> {
                                    viewModel.loggedTrainer.observe(this, Observer { r1 ->
                                        when(r1){
                                            is Resource.Loading -> {
                                                //showProgress()
                                            }
                                            is Resource.Success -> {
                                                val myId = r1.data.id
                                                FirebaseMessaging.getInstance().subscribeToTopic("/topics/trainer_$myId")
                                                startActivity(TrainerActivity.getLaunchIntent(this))
                                                finish()
                                            }
                                            is Resource.Failure -> {
                                                startActivity(TrainerActivity.getLaunchIntent(this))
                                                finish()
                                            }
                                        }
                                    })
                                }
                                "CUSTOMER" -> {
                                    viewModel.trainer.observe(this, Observer { r1 ->
                                        when(r1){
                                            is Resource.Loading -> {
                                                //showProgress()
                                            }
                                            is Resource.Success -> {
                                                val trainerId = r1.data.id
                                                FirebaseMessaging.getInstance().subscribeToTopic("/topics/customers_$trainerId")
                                                startActivity(CustomerActivity.getLaunchIntent(this))
                                                finish()
                                            }
                                            is Resource.Failure -> {
                                                startActivity(CustomerActivity.getLaunchIntent(this))
                                                finish()
                                            }
                                        }
                                    })
                                }
                                "ADMIN" -> {
                                    FirebaseMessaging.getInstance().subscribeToTopic("/topics/admin")
                                    startActivity(AdminActivity.getLaunchIntent(this))
                                    finish()
                                }
                                "NO_ACCOUNT" -> {
                                    mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build()
                                    mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
                                    firebaseAuth.signOut()
                                    mGoogleSignInClient.revokeAccess()
                                    this.viewModel.setNullRole()
                                    Toast.makeText(this, "Cannot find this user in Firebase", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        is Resource.Failure -> {
                            //hideProgress()
                            mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build()
                            mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
                            firebaseAuth.signOut()
                            mGoogleSignInClient.revokeAccess()
                            this.viewModel.setNullRole()
                            //Toast.makeText(this, "Cannot find this user in Firebase", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        } catch (e: Exception) {
            //Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }
    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, GoogleSignInActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}
