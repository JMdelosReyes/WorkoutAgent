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
import kotlinx.android.synthetic.main.activity_bottom_navigation_customer.*


const val PROFILE_CUSTOMER_FRAGMENT = "MyProfileCustomer"

class CustomerActivity : BaseActivity(), AppBarConfiguration.OnNavigateUpListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation_customer)

        val navController = findNavController(R.id.nav_host_fragment)

        setupBottomBar(navController)
        setupToolbar(navController)

        intent.extras?.let {
            if (it.get(FRAGMENT_KEY) == PROFILE_CUSTOMER_FRAGMENT) {
                navController.navigate(R.id.navigation_profile_customer)
            }
        }
    }

    private fun setupToolbar(navController: NavController) {
        setSupportActionBar(findViewById(R.id.main_toolbar))
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_today_customer -> {
                    supportActionBar?.title = "Today"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.navigation_routine_customer -> {
                    supportActionBar?.title = "Routines"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.navigation_profile_customer -> {
                    supportActionBar?.title = "My profile"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.listGoalCustomerFragment -> {
                    supportActionBar?.title = "My goals"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.createGoalCustomerFragment -> {
                    supportActionBar?.title = "Create a goal"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.editProfileCustomerFragment -> {
                    supportActionBar?.title = "Edit my profile"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.deleteProfileSendEmailCustomerFragment -> {
                    supportActionBar?.title = "Deleting my account"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
            }
        }
    }

    private fun setupBottomBar(navController: NavController) {
        nav_view_customer.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun restartActivityWithSelectedFragmentCustomer(fragment: String) {
        val intent = Intent(this, CustomerActivity::class.java)
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
            Intent(from, CustomerActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
    }
}
