package com.vikas.mobile.mynotes.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import com.vikas.mobile.mynotes.R

class LinedEditText(context: Context, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatEditText(context, attrs) {
    private val mRect: Rect = Rect()
    private val mPaint: Paint = Paint()

    override fun onDraw(canvas: Canvas) {
//        var count = lineCount;
        val height = height
        val lineHeight = lineHeight
        var count = height / lineHeight
        if (lineCount > count) count = lineCount //for long text with scrolling
        val r = mRect
        val paint = mPaint
        var baseline = getLineBounds(0, r) //first line
        for (i in 0 until count) {
            canvas.drawLine(
                    r.left.toFloat(),
                    (baseline + 1).toFloat(),
                    r.right.toFloat(),
                    (baseline + 1).toFloat(), paint)
            baseline += lineHeight //next line
        }
        super.onDraw(canvas)
    }

    // we need this constructor for LayoutInflater
    init {
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.color = context.getColor(R.color.note_content)
//        mPaint.color = Color.parseColor("#C0C0C0") //SET YOUR OWN COLOR HERE
    }
}