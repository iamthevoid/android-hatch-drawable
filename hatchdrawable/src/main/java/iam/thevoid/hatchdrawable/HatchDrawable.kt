package iam.thevoid.hatchdrawable

import android.graphics.*
import android.graphics.drawable.Drawable

class HatchDrawable @JvmOverloads constructor(
    private val lineWidth: Float = 1f,
    private val lineSpacing: Float = 4f,
    private val lineColor: Int = Color.BLACK,
    degree: Float = 225f
) : Drawable() {

    private val direction: Direction = Direction.define(degree)

    private val drawDegree: Float = degree.rem(360)

    /**
     * Set this to true for spacing not scales with angle
     */
    var spacingLinear = false

    /**
     * Set this to true for spacing not scales with angle
     */
    var widthLinear = false

    private val paint = Paint().apply {
        color = lineColor
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.BUTT
        isAntiAlias = true
        strokeWidth = lineWidth
    }

    override fun draw(canvas: Canvas) {

        val start = bounds.left.toFloat() - lineWidth
        val end = bounds.right.toFloat() + lineWidth
        val top = bounds.top.toFloat() - lineWidth
        val bottom = bounds.bottom.toFloat() + lineWidth


        val rawStep = lineWidth + lineSpacing

        when (direction) {
            Direction.DEG_0, Direction.DEG_180 -> drawHorizontal(start, top, end, bottom, canvas, rawStep)
            Direction.DEG_90, Direction.DEG_270 -> drawVertical(start, top, bottom, end, canvas, rawStep)
            Direction.DEG_0_90, Direction.DEG_180_270 -> drawUseAngle(start, bottom, top, end, canvas, step(sin(drawDegree.rem(90))))
            Direction.DEG_90_180 -> drawUseAngleMirrored(180 - drawDegree, start, bottom, top, end, canvas,step(cos(drawDegree.rem(90))))
            else -> drawUseAngleMirrored(360 - drawDegree, start, bottom, top, end, canvas, step(cos(drawDegree.rem(90))))
        }
    }

    fun step(scale : Float) : Float {
        val width = lineWidth / (if (widthLinear) 1f else scale)
        val space = lineSpacing / (if (spacingLinear) 1f else scale)
        return width + space
    }

    // lines for second and fourth quarter of full rotation
    private fun drawUseAngleMirrored(deg: Float, start: Float, bottom: Float, top: Float, end: Float, canvas: Canvas, step: Float) {

        var startX = start - bottom / tan(deg)
        var endX = start

        while (startX <= end || endX <= start) {
            canvas.drawLine(startX, top, endX, bottom, paint)
            startX += step
            endX += step
        }
    }

    private fun drawUseAngle(start: Float, bottom: Float, top: Float, end: Float, canvas: Canvas, step: Float) {
        var startX = start
        var endX = start + bottom / tan(drawDegree)

        if (endX > start) {
            startX -= (endX - start)
            endX = start
        }

        while (startX > endX && endX < end || startX <= endX && startX < end) {
            canvas.drawLine(startX, bottom, endX, top, paint)
            startX += step
            endX += step
        }
    }


    private fun drawVertical(start: Float, top: Float, bottom: Float, end: Float, canvas: Canvas, step: Float) {
        var startX = start
        var endX = start
        while (startX < end) {
            canvas.drawLine(startX, top, endX, bottom, paint)
            startX += step
            endX += step
        }
    }

    private fun drawHorizontal(start: Float, top: Float, end: Float, bottom: Float, canvas: Canvas, step: Float) {
        var startY = top
        var endY = top
        while (startY < bottom) {
            canvas.drawLine(start, startY, end, endY, paint)
            startY += step
            endY += step
        }
    }

    override fun setAlpha(alpha: Int) = Unit

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) = Unit

}