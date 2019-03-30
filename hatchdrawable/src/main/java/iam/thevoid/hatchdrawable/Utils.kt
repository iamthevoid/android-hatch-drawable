package iam.thevoid.hatchdrawable


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

fun sin(deg : Float) : Float = Math.sin(Math.toRadians(abs(deg))).toFloat()
fun cos(deg : Float) : Float = Math.cos(Math.toRadians(abs(deg))).toFloat()
fun tan(deg : Float) : Float = Math.tan(Math.toRadians(abs(deg))).toFloat()

fun abs(deg : Float) : Double = (if (deg > 0) deg else 360 - deg).toDouble()