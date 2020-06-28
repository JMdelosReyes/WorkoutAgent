package com.tfg.workoutagent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.tfg.workoutagent.base.BaseActivity
import kotlinx.android.synthetic.main.activity_bottom_navigation_customer.*

class CustomerActivity : BaseActivity(), AppBarConfiguration.OnNavigateUpListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation_customer)

        val navController = findNavController(R.id.nav_host_fragment)

        setupBottomBar(navController)
        setupToolbar(navController)
    }

    private fun setupToolbar(navController: NavController) {
        setSupportActionBar(findViewById(R.id.main_toolbar))
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.navigation_day_customer -> {
                    supportActionBar?.title = "My activity"
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
            }
        }
    }

    private fun setupBottomBar(navController: NavController) {
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
            Intent(from, CustomerActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
    }
}
