package com.tfg.workoutagent.vo

import android.app.AlertDialog
import android.content.Context

fun createAlertDialog(
    context: Context,
    title: String,
    message: String,
    positiveAction: () -> Unit,
    negativeAction: () -> Unit
) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)

    builder.setPositiveButton("Yes") { dialog, _ ->
        positiveAction()
        dialog.dismiss()
    }

    builder.setNeutralButton("No") { dialog, _ ->
        negativeAction()
        dialog.dismiss()
    }
    builder.create()
    builder.show()
}