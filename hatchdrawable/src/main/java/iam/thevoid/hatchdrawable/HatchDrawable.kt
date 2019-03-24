package iam.thevoid.hatchdrawable

import android.graphics.*
import android.graphics.drawable.Drawable

class HatchDrawable(
    private val lineWidth: Float = 1f,
    private val lineSpacing: Float = 4f,
    private val lineColor: Int = Color.BLACK,
    degree: Float = 225f
) : Drawable() {

    private val direction: Direction = Direction.define(degree)

    private val drawDegree: Double = degree.rem(360).toDouble()

    private val paint = Paint().apply {
        color = lineColor
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.BUTT
        isAntiAlias = true
        strokeWidth = lineWidth
    }


    override fun draw(canvas: Canvas) {
        val start = bounds.left.toFloat()
        val end = bounds.right.toFloat()
        val top = bounds.top.toFloat()
        val bottom = bounds.bottom.toFloat()

        val step = lineWidth + lineSpacing

        when (direction) {
            Direction.DEG_0, Direction.DEG_180 -> drawHorizontal(start, top, end, bottom, canvas, step)
            Direction.DEG_90, Direction.DEG_270 -> drawVertical(start, top, bottom, end, canvas, step)
            Direction.DEG_0_90, Direction.DEG_180_270 -> drawUseAngle(start, bottom, top, end, canvas, step)
            Direction.DEG_90_180 -> drawUseAngleMirrored(180 - drawDegree, start, bottom, top, end, canvas, step)
            else -> drawUseAngleMirrored(360 - drawDegree, start, bottom, top, end, canvas, step)
        }
    }

    private fun drawUseAngleMirrored(deg : Double, start: Float, bottom: Float, top: Float, end: Float, canvas: Canvas, step: Float) {
        val tan = Math.tan(Math.toRadians(Math.abs(deg))).toFloat()

        var startX = start - (bottom - top) / tan
        var endX = start

        while (startX <= end || endX <= start) {
            val drawStartX = if (startX < start) start else if (startX > end) end else startX
            val drawEndX = if (endX < start) start else if (endX > end) end else endX

            val startXOffset =  start - startX
            var drawStartY = (if (startXOffset > 0) startXOffset * tan else 0f)

            val endXOffset = endX - end
            var drawEndY = bottom - (if (endXOffset > 0) endXOffset * tan else 0f)


            canvas.drawLine(drawStartX, drawStartY, drawEndX, drawEndY, paint)
            startX += step
            endX += step
        }
    }


    private fun drawUseAngle(start: Float, bottom: Float, top: Float, end: Float, canvas: Canvas, step: Float) {
        val tan = Math.tan(Math.toRadians(drawDegree)).toFloat()

        var startX = start
        var endX = start + (bottom - top) / tan

        if (endX > start) {
            startX -= (endX - start)
            endX = start
        }

        while (startX > endX && endX < end || startX <= endX && startX < end) {
            val drawStartX = if (startX < start) start else if (startX > end) end else startX
            val drawEndX = if (endX < start) start else if (endX > end) end else endX

            val startXOffset = start - startX
            val drawStartY = bottom - (if (startXOffset > 0) startXOffset else 0f) * tan

            val endOffset = endX - end
            val drawEndY = top + (if (endOffset > 0) endOffset else 0f) * tan

            canvas.drawLine(drawStartX, drawStartY, drawEndX, drawEndY, paint)
            startX += step
            endX += step
        }
    }


    private fun drawVertical(start: Float, top: Float, bottom: Float, end: Float, canvas: Canvas, step: Float) {
        var startX = start
        val startY = top
        var endX = start
        val endY = bottom
        while (startX < end) {
            canvas.drawLine(startX, startY, endX, endY, paint)
            startX += step
            endX += step
        }
    }

    private fun drawHorizontal(start: Float, top: Float, end: Float, bottom: Float, canvas: Canvas, step: Float) {
        val startX = start
        var startY = top
        val endX = end
        var endY = top
        while (startY < bottom) {
            canvas.drawLine(startX, startY, endX, endY, paint)
            startY += step
            endY += step
        }
    }

    override fun setAlpha(alpha: Int) = Unit

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) = Unit

    enum class Direction {
        DEG_0, DEG_0_90, DEG_90, DEG_90_180, DEG_180, DEG_180_270, DEG_270, DEG_270_360;

        companion object {
            fun define(degree: Float) =
                degree.rem(360).let { rem ->
                    when {
                        rem == 0f -> Direction.DEG_0
                        rem < 90f -> Direction.DEG_0_90
                        rem == 90f -> Direction.DEG_90
                        rem < 180f -> Direction.DEG_90_180
                        rem == 180f -> Direction.DEG_180
                        rem < 270f -> Direction.DEG_180_270
                        rem == 270f -> Direction.DEG_270
                        else -> Direction.DEG_270_360
                    }
                }
        }
    }
}