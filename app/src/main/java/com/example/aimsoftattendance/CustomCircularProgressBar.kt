package com.example.aimsoftattendance

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.aimsoftattendance.R

class CustomCircularProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    private val markerPaint = Paint()
    private val rectF = RectF()
    private var progress = 0f
    private val maxProgress = 12f // 12 hours
    private var showMarker = false

    private var progressColor = Color.GREEN
    private var backgroundColor = Color.LTGRAY
    private var markerColor = Color.RED

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f // Adjust as needed
        paint.isAntiAlias = true

        markerPaint.style = Paint.Style.FILL
        markerPaint.color = markerColor
        markerPaint.isAntiAlias = true

        // Load custom attributes if any
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomCircularProgressBar,
            0, 0
        )

        try {
            progressColor = a.getColor(R.styleable.CustomCircularProgressBar_progressColor, Color.GREEN)
            backgroundColor = a.getColor(R.styleable.CustomCircularProgressBar_backgroundColor, Color.LTGRAY)
            markerColor = a.getColor(R.styleable.CustomCircularProgressBar_markerColor, Color.GREEN)
            markerPaint.color = markerColor
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val padding = paint.strokeWidth / 2
        rectF.set(padding, padding, width - padding, height - padding)

        // Draw the background circle
        paint.color = backgroundColor
        canvas.drawArc(rectF, -90f, 360f, false, paint)

        // Draw the progress arc
        paint.color = progressColor
        val sweepAngle = (progress / maxProgress) * 360f
        canvas.drawArc(rectF, -90f, sweepAngle, false, paint)

        // Draw the marker if needed
        if (showMarker) {
            val markerRadius = paint.strokeWidth * 2 // Adjust as needed
            val markerAngle = -90f + sweepAngle
            val centerX = width / 2
            val centerY = height / 2
            val radius = (width / 2 - padding)
            val markerX = (centerX + radius * Math.cos(Math.toRadians(markerAngle.toDouble()))).toFloat()
            val markerY = (centerY + radius * Math.sin(Math.toRadians(markerAngle.toDouble()))).toFloat()
            canvas.drawCircle(markerX, markerY, markerRadius, markerPaint)
        }
    }

    fun setProgress(hours: Float) {
        progress = hours.coerceIn(0f, maxProgress) // Ensure progress is within bounds
        invalidate() // Redraw the view
    }

    fun setProgressColor(color: Int) {
        progressColor = color
        invalidate() // Redraw the view
    }

    fun showMarker(show: Boolean) {
        showMarker = show
        invalidate() // Redraw the view
    }
}


