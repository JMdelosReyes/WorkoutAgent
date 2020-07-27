package com.tfg.workoutagent.base

import android.content.Context
import android.content.res.Configuration

interface BaseAdapterInterface {
    fun isDarkMode(context: Context): Boolean {
        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
    }
}