package com.example.drinkup.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.drinkup.R

class WaterProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 0f
    private var maxProgress = 100f

    private val waterPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.blue_primary)
        style = Paint.Style.FILL
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.white_alpha_20)
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 48f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // ✅ هنا التعديل
    }

    private val path = Path()
    private var waveOffset = 0f

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.WaterProgressView,
            0, 0
        ).apply {
            try {
                progress = getFloat(R.styleable.WaterProgressView_progress, 0f)
                maxProgress = getFloat(R.styleable.WaterProgressView_maxProgress, 100f)
            } finally {
                recycle()
            }
        }
    }

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, maxProgress)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // Draw background
        canvas.drawRoundRect(0f, 0f, width, height, 30f, 30f, backgroundPaint)

        // Calculate water level
        val waterLevel = height - (height * (progress / maxProgress))

        // Draw wave
        path.reset()
        path.moveTo(0f, waterLevel)

        val waveLength = width / 2
        val amplitude = 20f

        var x = 0f
        while (x < width) {
            path.quadTo(
                x + waveLength / 4 + waveOffset, waterLevel - amplitude,
                x + waveLength / 2 + waveOffset, waterLevel
            )
            path.quadTo(
                x + waveLength * 3 / 4 + waveOffset, waterLevel + amplitude,
                x + waveLength + waveOffset, waterLevel
            )
            x += waveLength
        }

        path.lineTo(width, height)
        path.lineTo(0f, height)
        path.close()

        canvas.drawPath(path, waterPaint)

        // Draw percentage text
        val percentage = "${(progress / maxProgress * 100).toInt()}%"
        canvas.drawText(
            percentage,
            width / 2,
            height / 2 + textPaint.textSize / 3,
            textPaint
        )

        // Animate wave
        waveOffset += 2f
        if (waveOffset > waveLength) {
            waveOffset = 0f
        }

        postInvalidateOnAnimation()
    }
}
