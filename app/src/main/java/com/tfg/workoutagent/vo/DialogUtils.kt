package com.tfg.workoutagent.vo

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tfg.workoutagent.R

fun createAlertDialog(
    context: Context,
    title: String,
    message: String,
    positiveAction: () -> Unit,
    negativeAction: () -> Unit,
    positiveText: String = "Yes",
    negativeText: String = "No"
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText) { dialog, _ ->
            positiveAction()
            dialog.dismiss()
        }
        .setNegativeButton(negativeText) { dialog, _ ->
            negativeAction()
            dialog.dismiss()
        }
        .show()
}

fun createAlertDialog(
    context: Context,
    title: String,
    message: String,
    positiveAction: () -> Unit,
    negativeAction: () -> Unit,
    positiveText: Int,
    negativeText: Int
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(context.resources.getString(positiveText)) { dialog, _ ->
            positiveAction()
            dialog.dismiss()
        }
        .setNegativeButton(context.resources.getString(negativeText)) { dialog, _ ->
            negativeAction()
            dialog.dismiss()
        }
        .show()
}

fun createAlertDialog(
    context: Context,
    title: String,
    message: String
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(context.getString(R.string.close)) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

class LoadingDialog(val activity: Activity) {

    lateinit var dialog: AlertDialog

    fun loadDialog() {
        val builder = AlertDialog.Builder(activity, R.style.WeightDialog)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(true)
        this.dialog = builder.create()
        this.dialog.show()
    }

    fun dismissDialog() {
        if (this::dialog.isInitialized) {
            this.dialog.dismiss()
        }
    }
}