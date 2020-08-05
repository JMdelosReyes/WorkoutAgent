package com.tfg.workoutagent.vo

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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