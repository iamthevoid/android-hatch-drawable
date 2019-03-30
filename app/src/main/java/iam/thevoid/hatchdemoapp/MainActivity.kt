package iam.thevoid.hatchdemoapp

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import iam.thevoid.hatchdrawable.HatchDrawable
import java.util.*

class MainActivity : Activity(), Notifier {

    val redWatcher = SeekListener(this)
    val greenWatcher = SeekListener(this)
    val blueWatcher = SeekListener(this)

    val angleWatcher = SeekListener(this)
    val lineWidthWatcher = HalfFloatSeekListener(this)
    val spacingWatcher = HalfFloatSeekListener(this)

    private lateinit var redSeek: SeekBar
    private lateinit var redImage: ImageView
    private lateinit var greenSeek: SeekBar
    private lateinit var greenImage: ImageView
    private lateinit var blueSeek: SeekBar
    private lateinit var blueImage: ImageView

    private lateinit var hatch: View
    private lateinit var colorPreview: View
    private lateinit var colorPreviewText: TextView

    private lateinit var angleSeek: SeekBar
    private lateinit var angleText: TextView
    private lateinit var spacingSeek: SeekBar
    private lateinit var spacingText: TextView
    private lateinit var lineWidthSeek: SeekBar
    private lateinit var lineWidthText: TextView

    private lateinit var checkboxWidth : CheckBox
    private lateinit var checkboxSpace : CheckBox

    private var dp = 0f

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        redSeek = findViewById(R.id.seek_red)
        greenSeek = findViewById(R.id.seek_green)
        blueSeek = findViewById(R.id.seek_blue)
        redImage = findViewById(R.id.red_col)
        greenImage = findViewById(R.id.green_col)
        blueImage = findViewById(R.id.blue_col)
        angleSeek = findViewById(R.id.seek_angle)
        angleText = findViewById(R.id.angle_tv)
        spacingSeek = findViewById(R.id.seek_spacing)
        spacingText = findViewById(R.id.spacing_tv)
        lineWidthSeek = findViewById(R.id.seek_width)
        lineWidthText = findViewById(R.id.width_tv)
        colorPreview = findViewById(R.id.color_preview)
        colorPreviewText = findViewById(R.id.color_preview_text)
        checkboxSpace = findViewById(R.id.checkboxSpacing)
        checkboxWidth = findViewById(R.id.checkboxWidth)
        hatch = findViewById(R.id.hatch)
        dp = resources.getDimensionPixelSize(R.dimen.dp).toFloat()

        val rand = Random()

        redSeek.setOnSeekBarChangeListener(redWatcher)
        redSeek.max = 255
        redSeek.progress = rand.nextInt(255)

        greenSeek.setOnSeekBarChangeListener(greenWatcher)
        greenSeek.max = 255
        greenSeek.progress = rand.nextInt(255)

        blueSeek.setOnSeekBarChangeListener(blueWatcher)
        blueSeek.max = 255
        blueSeek.progress = rand.nextInt(255)

        angleSeek.setOnSeekBarChangeListener(angleWatcher)
        angleSeek.max = 360
        angleSeek.progress = rand.nextInt(360)

        spacingSeek.setOnSeekBarChangeListener(spacingWatcher)
        spacingSeek.max = 128
        spacingSeek.progress = rand.nextInt(128)


        lineWidthSeek.setOnSeekBarChangeListener(lineWidthWatcher)
        lineWidthSeek.max = 128
        lineWidthSeek.progress = rand.nextInt(6)

        redImage.setImageDrawable(ColorDrawable(Color.RED))
        greenImage.setImageDrawable(ColorDrawable(Color.GREEN))
        blueImage.setImageDrawable(ColorDrawable(Color.BLUE))

        checkboxSpace.isChecked = rand.nextBoolean()
        checkboxSpace.setOnCheckedChangeListener { _, _ -> notifyChanged() }
        checkboxWidth.isChecked = rand.nextBoolean()
        checkboxWidth.setOnCheckedChangeListener { _, _ -> notifyChanged() }
    }

    override fun notifyChanged() {
        val redColor = redWatcher.value
        val greenColor = greenWatcher.value
        val blueColor = blueWatcher.value
        val angle = angleWatcher.value.toFloat()
        val lineWidth = lineWidthWatcher.value
        val spacing = spacingWatcher.value

        val red = complete(redColor)
        val green = complete(greenColor)
        val blue = complete(blueColor)

        val colorString = "$red$green$blue"
        Log.i("MainActivity", "Color = $colorString, angle = $angle, line width = $lineWidth, spacing = $spacing")

        redImage.alpha = redColor.toFloat() / 255f
        greenImage.alpha = greenColor.toFloat() / 255f
        blueImage.alpha = blueColor.toFloat() / 255f

        angleText.text = "${angle.toInt()}"
        spacingText.text = "${spacing.toInt()}"
        lineWidthText.text = "${lineWidth.toInt()}"

        val color = Color.parseColor("#$colorString")
        colorPreview.setBackgroundColor(color)

        val redInverted = complete(0xff - redColor)
        val greenInverted = complete(0xff - greenColor)
        val blueInverted = complete(0xff - blueColor)

        val invertedColor = Color.parseColor("#$redInverted$greenInverted$blueInverted")
        colorPreviewText.setTextColor(invertedColor)

        val drawable = HatchDrawable(
                lineWidth * dp,
                spacing * dp,
                Color.parseColor("#$colorString"),
                angle
        ).apply {
            widthLinear = checkboxWidth.isChecked
            spacingLinear = checkboxSpace.isChecked
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            hatch.background = drawable
        else {
            @Suppress("DEPRECATION")
            hatch.setBackgroundDrawable(drawable)
        }
    }

    private fun complete(color: Int): String {
        val strColor = color.toString(16)
        return if (strColor.length == 2) strColor else "0$strColor"
    }

    class SeekListener(private val notifier: Notifier) : SeekChangeAdapter() {

        var value: Int = 0xff
            private set

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            value = progress
            notifier.notifyChanged()
        }
    }


    class HalfFloatSeekListener(private val notifier: Notifier) : SeekChangeAdapter() {

        var value: Float = 0F
            private set

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            value = progress.toFloat() / 2f
            notifier.notifyChanged()
        }
    }
}
