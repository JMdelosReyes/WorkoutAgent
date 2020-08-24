package com.tfg.workoutagent.vo

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class CustomDivider : ItemDecoration {

    private var divider: Drawable?

    private var marginLeft = 0

    private var marginRight = 0

    constructor(context: Context, marginLeft: Int, marginRight: Int) {
        val styledAttributes: TypedArray =
            context.obtainStyledAttributes(ATTRS)
        this.divider = styledAttributes.getDrawable(0)
        this.marginLeft = marginLeft
        this.marginRight = marginRight
        styledAttributes.recycle()
    }

    constructor(context: Context, resId: Int) {
        divider = ContextCompat.getDrawable(context, resId)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + marginLeft
        val right = parent.width - parent.paddingRight - marginRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params =
                child.layoutParams as RecyclerView.LayoutParams
            val top: Int = child.bottom + params.bottomMargin
            val bottom = top + divider!!.intrinsicHeight
            divider!!.setBounds(left, top, right, bottom)
            divider!!.draw(c)
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}