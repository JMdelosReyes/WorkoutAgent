package com.tfg.workoutagent.presentation.ui.login.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.tfg.workoutagent.AdminActivity
import com.tfg.workoutagent.CustomerActivity
import com.tfg.workoutagent.TrainerActivity
import com.tfg.workoutagent.base.BaseActivity
import com.tfg.workoutagent.data.repositoriesImpl.LoginRepositoryImpl
import com.tfg.workoutagent.domain.loginUseCases.LoginUseCaseImpl
import com.tfg.workoutagent.presentation.ui.login.viewmodels.LoginViewModel
import com.tfg.workoutagent.presentation.ui.login.viewmodels.LoginViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.login_activity.*

class GoogleSignInActivity : BaseActivity() {

    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    private lateinit var firebaseAuth: FirebaseAuth
    private val viewModel by lazy { ViewModelProvider(this, LoginViewModelFactory(LoginUseCaseImpl(LoginRepositoryImpl()))).get(LoginViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        configureGoogleSignIn()
        setupUI()

        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }
    private fun setupUI() {
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
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.i("Prueba log in", "Soy el login")
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val email = FirebaseAuth.getInstance().currentUser?.email.toString()
                viewModel.fechtRole.observe(this, Observer { result ->
                    when(result){
                        is Resource.Loading -> {
                           //showProgress()
                        }
                        is Resource.Success -> {
                            val role = result.data
                            //hideProgress()
                            when(role){
                                "TRAINER" -> {startActivity(TrainerActivity.getLaunchIntent(this))}
                                "CUSTOMER" -> {startActivity(CustomerActivity.getLaunchIntent(this))}
                                "ADMIN" -> { startActivity(AdminActivity.getLaunchIntent(this))}
                            }
                        }
                        is Resource.Failure -> {
                            //hideProgress()
                            Toast.makeText(this, "Cannot find this user in Firebase", Toast.LENGTH_LONG).show()
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
        val user = FirebaseAuth.getInstance().currentUser
        Log.i("Prueba inicio", "$user")
        if (user != null) {
            val email = FirebaseAuth.getInstance().currentUser?.email.toString()
            viewModel.fechtRole.observe(this, Observer { result ->
                when(result){
                    is Resource.Loading -> {
                        //showProgress()
                    }
                    is Resource.Success -> {
                        val role = result.data
                        //hideProgress()
                         when(role){
                             "TRAINER" -> {startActivity(TrainerActivity.getLaunchIntent(this))}
                             "CUSTOMER" -> {startActivity(CustomerActivity.getLaunchIntent(this))}
                             "ADMIN" -> { startActivity(AdminActivity.getLaunchIntent(this))}
                         }
                        finish()
                    }
                    is Resource.Failure -> {
                        //hideProgress()
                        Toast.makeText(this, "Cannot find this user in Firebase", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            })

        }
    }
    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, GoogleSignInActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}
