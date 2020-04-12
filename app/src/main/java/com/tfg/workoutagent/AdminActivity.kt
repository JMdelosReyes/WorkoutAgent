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

class AdminActivity :  BaseActivity(), AppBarConfiguration.OnNavigateUpListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation_admin)

        val navController = findNavController(R.id.nav_host_fragment)

        setupBottombar(navController)
        setupToolbar(navController)
    }

    private fun setupToolbar(navController: NavController) {
        setSupportActionBar(findViewById(R.id.main_toolbar_admin))
        navController.addOnDestinationChangedListener { _, destination, _ ->
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

    companion object {
        fun getLaunchIntent(from: Context) =
            Intent(from, AdminActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
    }
}
