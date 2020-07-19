package com.tfg.workoutagent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.tfg.workoutagent.base.BaseActivity
import com.tfg.workoutagent.presentation.ui.routines.trainer.fragments.AddDayFragmentDirections
import kotlinx.android.synthetic.main.activity_bottom_navigation_trainer.*


const val FRAGMENT_KEY = "Fragment"
const val PROFILE_TRAINER_FRAGMENT = "MyProfileTrainer"

class TrainerActivity : BaseActivity(), AppBarConfiguration.OnNavigateUpListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation_trainer)

        val navController = findNavController(R.id.nav_host_fragment)

        setupBottombar(navController)
        setupToolbar(navController)

        intent.extras?.let {
            if (it.get(FRAGMENT_KEY) == PROFILE_TRAINER_FRAGMENT) {
                navController.navigate(R.id.navigation_profile_trainer)
            }
        }
    }

    private fun setupToolbar(navController: NavController) {
        setSupportActionBar(findViewById(R.id.main_toolbar))
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.navigation_activity_trainer -> {
                    supportActionBar?.title = "My activity"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.navigation_routine_trainer -> {
                    supportActionBar?.title = "Routines"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.displayRoutine -> {
                    supportActionBar?.title =
                        arguments!!.get("routine_title").toString()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.createRoutine -> {
                    supportActionBar?.title = "Create routine"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.addDayFragment -> {
                    supportActionBar?.title = "Add day"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.addDayEditRoutineFragment -> {
                    supportActionBar?.title = "Add day"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.editDayFragment -> {
                    supportActionBar?.title = "Edit day"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.navigation_exercises_trainer -> {
                    supportActionBar?.title = "Exercises"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.navigation_users_trainer -> {
                    supportActionBar?.title = "My users"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.displayCustomer -> {
                    supportActionBar?.title = arguments!!.get("customerName").toString()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.createCustomerTrainerFragment -> {
                    supportActionBar?.title = "Create a customer"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.editDeleteCustomerTrainerFragment -> {
                    supportActionBar?.title = "Edit " + arguments!!.get("customerName").toString()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.navigation_profile_trainer -> {
                    supportActionBar?.title = "My profile"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.editProfileTrainerFragment -> {
                    supportActionBar?.title = "Edit my account"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.displayExercise -> {
                    supportActionBar?.title =
                        arguments!!.get("exercise_title").toString()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.createExerciseFragment -> {
                    supportActionBar?.title = "Create exercise"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.editDeleteExerciseFragment -> {
                    supportActionBar?.title = "Edit exercise"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.nutritionCustomerTrainerFragment -> {
                    supportActionBar?.title = "Edit nutrition"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)

                }
                R.id.listGoalCustomerTrainerFragment -> {
                    supportActionBar?.title = "Goals of ${arguments!!.get("customerName").toString()}"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
            }
        }
    }

    private fun setupBottombar(navController: NavController) {
        nav_view.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val navController = findNavController(R.id.nav_host_fragment)
                when (navController.currentDestination?.id) {
                    R.id.addDayFragment -> {
                        navController.navigate(
                            AddDayFragmentDirections.actionAddDayFragmentToCreateRoutine(
                                clearData = 2
                            )
                        )
                    }
                    else -> onBackPressed()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun restartActivityWithSelectedFragment(fragment: String) {
        val intent = Intent(this, TrainerActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(FRAGMENT_KEY, fragment)

        startActivity(intent)
        this.overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }

    companion object {
        fun getLaunchIntent(from: Context) =
            Intent(from, TrainerActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
    }
}
