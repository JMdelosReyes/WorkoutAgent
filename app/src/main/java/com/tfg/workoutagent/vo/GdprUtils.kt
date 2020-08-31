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
            context.resources.getString(R.string.h10),
            context.resources.getString(R.string.p10)
        ), Question(
            context.resources.getString(R.string.h11),
            context.resources.getString(R.string.p11)
        ), Question(
            context.resources.getString(R.string.h12),
            context.resources.getString(R.string.p12)
        ), Question(
            context.resources.getString(R.string.h13),
            "\t${context.resources.getString(R.string.p13_1)}\n" +
                    "\t${context.resources.getString(R.string.p13_2)}\n" +
                    "\t\t${context.resources.getString(R.string.p13_2_1)}\n" +
                    "\t\t${context.resources.getString(R.string.p13_2_2)}\n" +
                    "\t\t${context.resources.getString(R.string.p13_2_3)}\n" +
                    "\t\t${context.resources.getString(R.string.p13_2_4)}\n" +
                    "\t\t${context.resources.getString(R.string.p13_2_5)}\n" +
                    "\t${context.resources.getString(R.string.p13_3)}\n" +
                    "\t\t${context.resources.getString(R.string.p13_3_1)}\n" +
                    "\t\t${context.resources.getString(R.string.p13_3_2)}\n" +
                    "\t\t${context.resources.getString(R.string.p13_3_3)}\n" +
                    "\t\t${context.resources.getString(R.string.p13_3_4)}\n" +
                    "\t\t${context.resources.getString(R.string.p13_3_5)}\n"
        ), Question(
            context.resources.getString(R.string.h14),
            "\t${context.resources.getString(R.string.p14_1)}\n" +
                    "\t${context.resources.getString(R.string.p14_2)}\n" +
                    "\t${context.resources.getString(R.string.p14_3)}\n" +
                    "\t${context.resources.getString(R.string.p14_4)}\n"
        ), Question(
            context.resources.getString(R.string.h15),
            "${context.resources.getString(R.string.p15)}\n"
        ), Question(
            context.resources.getString(R.string.h16),
            "${context.resources.getString(R.string.p16)}\n"
        ), Question(
            context.resources.getString(R.string.h17),
            "${context.resources.getString(R.string.p17)}\n" +
                    "${context.resources.getString(R.string.p18)}\n"
        )
    )
}

fun getAllFaqs(context: Context): MutableList<Question> {
    return mutableListOf(
        Question(
            context.resources.getString(R.string.faq_h1),
            context.resources.getString(R.string.faq_p1)
        ), Question(
            context.resources.getString(R.string.faq_h2),
            context.resources.getString(R.string.faq_p2)
        ), Question(
            context.resources.getString(R.string.faq_h3),
            context.resources.getString(R.string.faq_p3)
        ), Question(
            context.resources.getString(R.string.faq_h4),
            context.resources.getString(R.string.faq_p4)
        ), Question(
            context.resources.getString(R.string.faq_h5),
            context.resources.getString(R.string.faq_p5)
        ), Question(
            context.resources.getString(R.string.faq_h6),
            context.resources.getString(R.string.faq_p6)
        ), Question(
            context.resources.getString(R.string.faq_h7),
            context.resources.getString(R.string.faq_p7)
        )
    )
}