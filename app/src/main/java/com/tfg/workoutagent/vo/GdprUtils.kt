package com.tfg.workoutagent.vo

import android.content.Context
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Question

fun getAllTerms(context: Context): MutableList<Question> {
    return mutableListOf(
        Question(
            context.resources.getString(R.string.h1),
            context.resources.getString(R.string.p1)
        ), Question(
            context.resources.getString(R.string.h2),
            context.resources.getString(R.string.p2)
        ), Question(
            context.resources.getString(R.string.h3),
            context.resources.getString(R.string.p3)
        ), Question(
            context.resources.getString(R.string.h4),
            context.resources.getString(R.string.p4)
        ), Question(
            context.resources.getString(R.string.h5),
            "${context.resources.getString(R.string.p5)}\n\t${context.resources.getString(
                R.string.p5_1
            )}"
        ), Question(
            context.resources.getString(R.string.h6),
            context.resources.getString(R.string.p6)
        ), Question(
            context.resources.getString(R.string.h7),
            "${context.resources.getString(R.string.p7_1)}\n${context.resources.getString(
                R.string.p7_2
            )}"
        ), Question(
            context.resources.getString(R.string.h8),
            context.resources.getString(R.string.p8)
        ), Question(
            context.resources.getString(R.string.h9),
            context.resources.getString(R.string.p9)
        ), Question(
            context.resources.getString(R.string.h10),
            context.resources.getString(R.string.p10)
        ), Question(
            context.resources.getString(R.string.h11),
            context.resources.getString(R.string.p11)
        ), Question(
            context.resources.getString(R.string.h12),
            context.resources.getString(R.string.p12)
        )
    )
}