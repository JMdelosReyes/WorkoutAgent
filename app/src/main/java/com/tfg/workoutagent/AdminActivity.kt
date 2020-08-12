package com.tfg.workoutagent

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import android.content.Context
import android.content.Intent
import androidx.navigation.ui.setupWithNavController
import com.tfg.workoutagent.base.BaseActivity
import kotlinx.android.synthetic.main.activity_bottom_navigation_admin.*


const val PROFILE_ADMIN_FRAGMENT = "MyProfileAdmin"

class AdminActivity : BaseActivity(), AppBarConfiguration.OnNavigateUpListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation_admin)

        val navController = findNavController(R.id.nav_host_fragment)

        setupBottombar(navController)
        setupToolbar(navController)

        intent.extras?.let {
            if (it.get(FRAGMENT_KEY) == PROFILE_ADMIN_FRAGMENT) {
                navController.navigate(R.id.navigation_admin_profile)
            }
        }
    }

    private fun setupToolbar(navController: NavController) {
        setSupportActionBar(findViewById(R.id.main_toolbar_admin))
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.navigation_admin_profile -> {
                    supportActionBar?.title = "My profile"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.navigation_admin_users -> {
                    supportActionBar?.title = "Users"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.createTrainerAdminFragment -> {
                    supportActionBar?.title = "Create a trainer"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.displayCustomerAdminFragment -> {
                    supportActionBar?.title = arguments!!.get("customerName").toString()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.displayTrainerAdminFragment -> {
                    supportActionBar?.title = arguments!!.get("nameTrainer").toString()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.editDeleteTrainerAdminFragment -> {
                    supportActionBar?.title = arguments!!.get("nameTrainer").toString()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.termsConditionsFragment2 -> {
                    supportActionBar?.title = "Terms and Conditions"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                R.id.faqsFragment2 -> {
                    supportActionBar?.title = "FAQS"
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
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun restartActivityWithSelectedFragmentAdmin(fragment: String) {
        val intent = Intent(this, AdminActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(FRAGMENT_KEY, fragment)

        startActivity(intent)
        this.overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment)
        when (navController.currentDestination?.id) {
            R.id.navigation_admin_users -> finish()
            R.id.navigation_admin_profile -> finish()
            else -> super.onBackPressed()
        }
    }

    companion object {
        fun getLaunchIntent(from: Context) =
            Intent(from, AdminActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
    }
}
