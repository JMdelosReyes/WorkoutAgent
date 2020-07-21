package com.tfg.workoutagent

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.navigation.NavController
import com.tfg.workoutagent.base.BaseActivity

class AdminActivity : BaseActivity() {

    override val viewID = R.layout.activity_bottom_navigation_admin
    override var navHostFragmentID: Int? = R.id.nav_host_fragment
    override var toolbarID: Int? = R.id.main_toolbar
    override var navViewID: Int? = R.id.nav_view_admin

    override fun setupToolbar(navController: NavController) {
        super.setupToolbar(navController)
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
            }
        }
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
