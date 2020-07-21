package com.tfg.workoutagent.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BaseActivity : AppCompatActivity(), AppBarConfiguration.OnNavigateUpListener {

    abstract val viewID: Int

    open var navHostFragmentID: Int? = null

    open var toolbarID: Int? = null

    open var navViewID: Int? = null

    var navController: NavController? = null

    var preferencesDarkMode = "dark_mode"

    var preferencesDarkModeDefault = "light"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewID)
        this.navHostFragmentID?.let {
            this.navController = findNavController(it)
            this.toolbarID?.let {
                this.setupToolbar(this.navController!!)
            }
            this.navViewID?.let {
                this.setupBottomBar(this.navController!!)
            }
        }
        this.checkPreferences()
        this.checkDarkMode()
    }

    open fun setupToolbar(navController: NavController) {
        setSupportActionBar(findViewById(toolbarID!!))
    }

    open fun setupBottomBar(navController: NavController) {
        findViewById<BottomNavigationView>(this.navViewID!!).setupWithNavController(navController)
    }

    private fun checkPreferences() {
        val sharedPref = getDefaultSharedPreferences(this) ?: return
        if (!sharedPref.contains(this.preferencesDarkMode)) {
            this.saveSharedPreference(this.preferencesDarkMode, this.preferencesDarkModeDefault)
        }
    }

    fun getSharedPreferences() = getDefaultSharedPreferences(this).all

    fun getSharedPreferenceValue(preference: String, defaultPreference: String) =
        getDefaultSharedPreferences(this).getString(
            preference,
            defaultPreference
        )

    fun saveSharedPreference(preference: String, preferenceValue: String) {
        val sharedPref =
            getDefaultSharedPreferences(this) ?: return
        with(sharedPref.edit()) {
            putString(preference, preferenceValue)
            commit()
        }
    }

    private fun checkDarkMode() {

    }

    /*protected abstract fun getViewID():Int

    fun showProgress(){
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgress(){
        progressBar.visibility = View.GONE
    }*/
}