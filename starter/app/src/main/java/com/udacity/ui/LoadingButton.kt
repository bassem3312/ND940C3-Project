package com.udacity.ui

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.udacity.R
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var progress: Float = 0.9f
    private val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(2000)


    //constants.  custom attributes
    private var boxRadius = convertDpToPixel(10f)
    private var boxShadowSize = convertDpToPixel(2f)
    private var boxStrokeWidth = convertDpToPixel(2f)
    private var textColorClick: Int = Color.BLACK
    private var textColor: Int = Color.BLACK
    private var boxProgressColor: Int = Color.GRAY
    private var boxColor = Color.parseColor("#52618e")
    private var boxBackGroundColorClick = Color.parseColor("#52618e")
    private var fontSize = convertDpToPixel(30f)
    private var boxBackgroundColor = Color.WHITE
    private var text = ""
    private var loadingText = ""
    private var buttonText = ""

    private lateinit var boxShadow: RectF
    private lateinit var boxLoading: RectF
    private lateinit var boxBackground: RectF
    private lateinit var boxShadowPaint: Paint
    private lateinit var boxBackgroundPaint: Paint
    private lateinit var progressPaint: Paint
    private var textWidth = 0f
    private var textSmallGlyphHeight = 0f
    private lateinit var textPaint: Paint

    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->

        when (new) {
            ButtonState.Clicked -> {
                textPaint.color = textColorClick
                text = buttonText
                textPaint.textSize = fontSize + convertDpToPixel(8f)
                boxBackgroundPaint.color = boxBackGroundColorClick
                textWidth = textPaint.measureText(text)
            }
            ButtonState.Loading -> {
                Log.e("++Button State ", "is Loading")
                text = loadingText
                textPaint.textSize = fontSize + convertDpToPixel(5f)
                textPaint.color = textColor
                boxBackgroundPaint.color = boxBackgroundColor
                textWidth = textPaint.measureText(text)
                valueAnimator.start()
            }
            else -> {
                Log.e("++Button State ", "is Completed")
                valueAnimator.cancel()
                text = buttonText
                textPaint.color = textColor
                textPaint.textSize = fontSize
                boxBackgroundPaint.color = boxBackgroundColor
                textWidth = textPaint.measureText(text)
                invalidate()
            }
        }
    }

    init {
        initViewCustomAttributes(attrs)
        valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.repeatCount = ValueAnimator.INFINITE

        valueAnimator.addUpdateListener {
            progress = it.animatedValue as Float
            invalidate()
        }
    }

    private fun initViewCustomAttributes(attrs: AttributeSet?) {
        val attributeArray: TypedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton, 0, 0
        )

        if (attributeArray != null) {
            buttonText = attributeArray.getString(R.styleable.LoadingButton_text).toString()
            loadingText = attributeArray.getString(R.styleable.LoadingButton_loading_text).toString()
            textColor = attributeArray.getColor(R.styleable.LoadingButton_text_color, Color.BLACK)
            fontSize = attributeArray.getDimension(R.styleable.LoadingButton_text_size, convertDpToPixel(30f))
            boxStrokeWidth = attributeArray.getDimension(R.styleable.LoadingButton_stroke_width, convertDpToPixel(10f))
            boxShadowSize = attributeArray.getDimension(R.styleable.LoadingButton_stroke_shadow_width, convertDpToPixel(10f))
            boxRadius = attributeArray.getDimension(R.styleable.LoadingButton_stroke_curve, convertDpToPixel(10f))
            boxBackgroundColor = attributeArray.getColor(R.styleable.LoadingButton_background_color, Color.WHITE)
            boxColor = attributeArray.getColor(R.styleable.LoadingButton_stroke_color, Color.BLACK)
            boxProgressColor = attributeArray.getColor(R.styleable.LoadingButton_background_color_loading, Color.GRAY)
            boxBackGroundColorClick = attributeArray.getColor(R.styleable.LoadingButton_background_color_click, Color.BLACK)
            textColorClick = attributeArray.getColor(R.styleable.LoadingButton_text_color_click, Color.BLACK)
            text = buttonText
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minW, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.e("Loading Button", "loading button on size changed")

        boxShadow = RectF(0f, 0f, w.toFloat(), h.toFloat())
         boxLoading = RectF(0f, 0f, w.toFloat(), h.toFloat())
        boxBackground = RectF(
            boxStrokeWidth, boxStrokeWidth,
            w.toFloat() - boxStrokeWidth, h.toFloat() - boxStrokeWidth - boxShadowSize
        )
        boxShadowPaint = Paint().apply { color = boxColor }

        boxBackgroundPaint = Paint().apply { color = boxBackgroundColor }

        progressPaint = Paint().apply {
            color = boxProgressColor
        }


        textPaint = Paint().apply {
            color = textColor
            textSize = fontSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textWidth = measureText(text)
            textSmallGlyphHeight = fontMetrics.run { ascent + descent }
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            //shadow paint.
            it.drawRoundRect(boxShadow, boxRadius, boxRadius, boxShadowPaint)

            //button background.
            it.drawRoundRect(boxBackground, boxRadius, boxRadius, boxBackgroundPaint)

            //draw progress

            if (buttonState == ButtonState.Loading) {
                val dx = width * progress
                boxLoading.left=dx
                it.drawRoundRect(boxLoading, boxRadius, boxRadius, progressPaint)
            }

            //draw text.
            val textStartPadding = (width - textWidth) / 2f
            val textTopPadding = (height - textSmallGlyphHeight) / 2f
            it.drawText(text, textStartPadding, textTopPadding, textPaint)

        }
        //delay 200 ms to display click effect
        if (buttonState == ButtonState.Clicked) {
            Handler(Looper.getMainLooper()).postDelayed({
                buttonState = ButtonState.Completed
                invalidate()
            }, 200)
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            performClick()
        }
        return true
    }

    override fun performClick(): Boolean {

        updateClickStatus()

        //delay 250 ms to display click event effect
        Handler(Looper.getMainLooper()).postDelayed({
        }, 250)
        return super.performClick()

    }

    private fun updateClickStatus() {
        buttonState = ButtonState.Clicked
        invalidate()
    }

    private fun convertDpToPixel(dp: Float) =
        dp * (resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}
