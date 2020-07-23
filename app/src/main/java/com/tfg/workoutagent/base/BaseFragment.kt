package com.tfg.workoutagent.base

import android.content.res.Configuration
import androidx.fragment.app.Fragment

/**
 * A simple [Fragment] subclass.
 */
abstract class BaseFragment : Fragment() {

    fun getDarkMode() : Boolean{
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
    }
}
