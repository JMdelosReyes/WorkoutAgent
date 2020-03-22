package com.tfg.workoutagent.presentation.ui.exercises.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repoimpl.ExerciseRepoImpl
import com.tfg.workoutagent.domain.exercises.ListExercisesImpl
import com.tfg.workoutagent.login.GoogleSignInActivity
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.presentation.ui.exercises.adapters.ExerciseListAdapter
import com.tfg.workoutagent.presentation.ui.exercises.viewmodels.ListExerciseViewModel
import com.tfg.workoutagent.presentation.ui.exercises.viewmodels.ListExerciselViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var adapter: ExerciseListAdapter

    private val viewModel by lazy { ViewModelProvider(this, ListExerciselViewModelFactory(ListExercisesImpl(ExerciseRepoImpl()))).get(ListExerciseViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupUI()

        adapter = ExerciseListAdapter(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        observeData()

    }


    private fun observeData(){
        //si es en un fragmento ponemos viewLifecycleowner como primer parametro

        viewModel.exerciseList.observe(this, Observer {result ->
            when(result){
                is Resource.Loading -> {
                    shimmer_view_container.startShimmer()


                }
                is Resource.Success ->{
                    shimmer_view_container.visibility= View.GONE
                    shimmer_view_container.stopShimmer()
                    adapter.setListData(result.data)
                    adapter.notifyDataSetChanged()
                }
                is Resource.Failure ->{
                    shimmer_view_container.visibility= View.GONE
                    shimmer_view_container.stopShimmer()
                    Toast.makeText(this, "Ocurrió un error ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun setupUI() {
        sign_out_button.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
        startActivity(GoogleSignInActivity.getLaunchIntent(this))
        // Google revoke access

        FirebaseAuth.getInstance().signOut();

        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this) {
            Log.i("HomeActivity", "Se ha revocado el acceso")
        }
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}
