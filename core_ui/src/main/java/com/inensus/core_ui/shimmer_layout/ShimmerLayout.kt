package com.inensus.core_ui.shimmer_layout

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Shader
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.inensus.core_ui.R
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class ShimmerLayout(
    context: Context,
    attrs: AttributeSet,
) : FrameLayout(context, attrs) {
    private var maskOffsetX = 0
    private lateinit var maskRect: Rect
    private var gradientTexturePaint: Paint? = null
    private var maskAnimator: ValueAnimator? = null

    private var maskBitmap: Bitmap? = null
    private var canvasForShimmerMask: Canvas? = null

    private var isAnimationReversed = false
    private var isAnimationStarted = false
    private var autoStart = false
    private var shimmerAnimationDuration = 0
    private var shimmerColor = 0
    private var shimmerAngle = 0
    private var maskWidth = 0f
    private var gradientCenterColorWidth = 0f

    private var startAnimationPreDrawListener: ViewTreeObserver.OnPreDrawListener? = null

    init {
        setWillNotDraw(false)
        val a: TypedArray =
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ShimmerLayout,
                0,
                0,
            )
        try {
            shimmerAngle = a.getInteger(R.styleable.ShimmerLayout_shimmer_angle, DEFAULT_ANGLE.toInt())
            shimmerAnimationDuration = a.getInteger(R.styleable.ShimmerLayout_shimmer_animation_duration, DEFAULT_ANIMATION_DURATION)
            shimmerColor = a.getColor(R.styleable.ShimmerLayout_shimmer_color, 0)
            autoStart = a.getBoolean(R.styleable.ShimmerLayout_shimmer_auto_start, false)
            maskWidth = a.getFloat(R.styleable.ShimmerLayout_shimmer_mask_width, 0.5f)
            gradientCenterColorWidth = a.getFloat(R.styleable.ShimmerLayout_shimmer_gradient_center_color_width, 0.1f)
            isAnimationReversed = a.getBoolean(R.styleable.ShimmerLayout_shimmer_reverse_animation, false)
        } finally {
            a.recycle()
        }
        setMaskWidth(maskWidth)
        setGradientCenterColorWidth(gradientCenterColorWidth)
        setShimmerAngle(shimmerAngle)
        if (autoStart && visibility == VISIBLE) {
            startShimmerAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        resetShimmering()
        super.onDetachedFromWindow()
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (!isAnimationStarted || width <= 0 || height <= 0) {
            super.dispatchDraw(canvas)
        } else {
            dispatchDrawShimmer(canvas)
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == VISIBLE) {
            if (autoStart) {
                startShimmerAnimation()
            }
        } else {
            stopShimmerAnimation()
        }
    }

    fun startShimmerAnimation() {
        if (isAnimationStarted) {
            return
        }
        if (width == 0) {
            startAnimationPreDrawListener =
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        viewTreeObserver.removeOnPreDrawListener(this)
                        startShimmerAnimation()
                        return true
                    }
                }
            viewTreeObserver.addOnPreDrawListener(startAnimationPreDrawListener)
            return
        }
        maskRect = calculateBitmapMaskRect()
        getShimmerAnimation().start()
        isAnimationStarted = true
    }

    fun stopShimmerAnimation() {
        if (startAnimationPreDrawListener != null) {
            viewTreeObserver.removeOnPreDrawListener(startAnimationPreDrawListener)
        }
        resetShimmering()
    }

    fun isAnimationStarted(): Boolean = isAnimationStarted

    fun setShimmerColor(shimmerColor: Int) {
        this.shimmerColor = shimmerColor
        resetIfStarted()
    }

    fun setShimmerAnimationDuration(durationMillis: Int) {
        shimmerAnimationDuration = durationMillis
        resetIfStarted()
    }

    fun setAnimationReversed(animationReversed: Boolean) {
        isAnimationReversed = animationReversed
        resetIfStarted()
    }

    /**
     * Set the angle of the shimmer effect in clockwise direction in degrees.
     * The angle must be between {@value #MIN_ANGLE_VALUE} and {@value #MAX_ANGLE_VALUE}.
     *
     * @param angle The angle to be set
     */
    private fun setShimmerAngle(angle: Int) {
        require(!(angle < MIN_ANGLE_VALUE || MAX_ANGLE_VALUE < angle)) {
            String.format(
                "shimmerAngle value must be between %d and %d",
                MIN_ANGLE_VALUE,
                MAX_ANGLE_VALUE,
            )
        }
        shimmerAngle = angle
        resetIfStarted()
    }

    /**
     * Sets the width of the shimmer line to a value higher than 0 to less or equal to 1.
     * 1 means the width of the shimmer line is equal to half of the width of the ShimmerLayout.
     * The default value is 0.5.
     *
     * @param maskWidth The width of the shimmer line.
     */
    private fun setMaskWidth(maskWidth: Float) {
        require(!(maskWidth <= MIN_MASK_WIDTH_VALUE || MAX_MASK_WIDTH_VALUE < maskWidth)) {
            String.format(
                "maskWidth value must be higher than %d and less or equal to %d",
                MIN_MASK_WIDTH_VALUE,
                MAX_MASK_WIDTH_VALUE,
            )
        }
        this.maskWidth = maskWidth
        resetIfStarted()
    }

    /**
     * Sets the width of the center gradient color to a value higher than 0 to less than 1.
     * 0.99 means that the whole shimmer line will have this color with a little transparent edges.
     * The default value is 0.1.
     *
     * @param gradientCenterColorWidth The width of the center gradient color.
     */
    private fun setGradientCenterColorWidth(gradientCenterColorWidth: Float) {
        require(
            !(
                gradientCenterColorWidth <= MIN_GRADIENT_CENTER_COLOR_WIDTH_VALUE ||
                    MAX_GRADIENT_CENTER_COLOR_WIDTH_VALUE <= gradientCenterColorWidth
            ),
        ) {
            String.format(
                "gradientCenterColorWidth value must be higher than %d and less than %d",
                MIN_GRADIENT_CENTER_COLOR_WIDTH_VALUE,
                MAX_GRADIENT_CENTER_COLOR_WIDTH_VALUE,
            )
        }
        this.gradientCenterColorWidth = gradientCenterColorWidth
        resetIfStarted()
    }

    private fun resetIfStarted() {
        if (isAnimationStarted) {
            resetShimmering()
            startShimmerAnimation()
        }
    }

    private fun dispatchDrawShimmer(canvas: Canvas) {
        super.dispatchDraw(canvas)
        getMaskBitmap()?.let { localMaskBitmap ->

            if (canvasForShimmerMask == null) {
                canvasForShimmerMask = Canvas(localMaskBitmap)
            }

            canvasForShimmerMask?.apply {
                drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                save()
                translate(-maskOffsetX.toFloat(), 0f)
                super.dispatchDraw(canvasForShimmerMask)
                restore()
            }
            drawShimmer(canvas, localMaskBitmap)
        }
    }

    private fun drawShimmer(
        destinationCanvas: Canvas,
        localMaskBitmap: Bitmap,
    ) {
        createShimmerPaint(localMaskBitmap)
        destinationCanvas.apply {
            save()
            translate(maskOffsetX.toFloat(), 0f)
            gradientTexturePaint?.let { paint ->
                drawRect(maskRect.left.toFloat(), 0f, maskRect.width().toFloat(), maskRect.height().toFloat(), paint)
            }
            restore()
        }
    }

    private fun resetShimmering() {
        maskAnimator?.apply {
            end()
            removeAllUpdateListeners()
        }
        maskAnimator = null
        gradientTexturePaint = null
        isAnimationStarted = false
        releaseBitMaps()
    }

    private fun releaseBitMaps() {
        canvasForShimmerMask = null
        maskBitmap?.recycle()
        maskBitmap = null
    }

    private fun getMaskBitmap(): Bitmap? {
        if (maskBitmap == null) {
            maskBitmap = createBitmap(maskRect.width(), height)
        }
        return maskBitmap
    }

    private fun createShimmerPaint(localMaskBitmap: Bitmap) {
        if (gradientTexturePaint != null) {
            return
        }
        val edgeColor = reduceColorAlphaValueToZero(shimmerColor)
        val shimmerLineWidth = width / 2 * maskWidth
        val yPosition = if (0 <= shimmerAngle) height.toFloat() else 0.toFloat()
        val gradient =
            LinearGradient(
                0f,
                yPosition,
                cos(Math.toRadians(shimmerAngle.toDouble())).toFloat() * shimmerLineWidth,
                yPosition + sin(Math.toRadians(shimmerAngle.toDouble())).toFloat() * shimmerLineWidth,
                intArrayOf(edgeColor, shimmerColor, shimmerColor, edgeColor),
                getGradientColorDistribution(),
                Shader.TileMode.CLAMP,
            )
        val maskBitmapShader = BitmapShader(localMaskBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val composeShader = ComposeShader(gradient, maskBitmapShader, PorterDuff.Mode.DST_IN)

        gradientTexturePaint =
            Paint().apply {
                isAntiAlias = true
                isDither = true
                isFilterBitmap = true
                shader = composeShader
            }
    }

    private fun getShimmerAnimation(): Animator =
        maskAnimator ?: let {
            val animationToX = width
            val animationFromX = -if (width > maskRect.width()) animationToX else maskRect.width()

            val shimmerBitmapWidth: Int = maskRect.width()
            val shimmerAnimationFullLength = animationToX - animationFromX
            val animator =
                if (isAnimationReversed) {
                    ValueAnimator.ofInt(shimmerAnimationFullLength, 0)
                } else {
                    ValueAnimator.ofInt(0, shimmerAnimationFullLength)
                }.apply {
                    duration = shimmerAnimationDuration.toLong()
                    repeatCount = ObjectAnimator.INFINITE
                    addUpdateListener { animation ->
                        maskOffsetX = animationFromX + animation.animatedValue as Int
                        if (maskOffsetX + shimmerBitmapWidth >= 0) {
                            invalidate()
                        }
                    }
                }
            maskAnimator = animator
            animator
        }

    private fun createBitmap(
        width: Int,
        height: Int,
    ): Bitmap? =
        try {
            Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
        } catch (e: OutOfMemoryError) {
            System.gc()
            null
        }

    private fun reduceColorAlphaValueToZero(actualColor: Int): Int =
        Color.argb(0, Color.red(actualColor), Color.green(actualColor), Color.blue(actualColor))

    private fun calculateBitmapMaskRect() = Rect(0, 0, calculateMaskWidth(), height)

    private fun calculateMaskWidth(): Int {
        val shimmerLineBottomWidth =
            width / 2 * maskWidth / cos(Math.toRadians(abs(shimmerAngle).toDouble()))
        val shimmerLineRemainingTopWidth =
            height * tan(Math.toRadians(abs(shimmerAngle).toDouble()))
        return (shimmerLineBottomWidth + shimmerLineRemainingTopWidth).toInt()
    }

    private fun getGradientColorDistribution(): FloatArray? {
        val colorDistribution = FloatArray(4)
        colorDistribution[0] = 0f
        colorDistribution[3] = 1f
        colorDistribution[1] = 0.5f - gradientCenterColorWidth / 2f
        colorDistribution[2] = 0.5f + gradientCenterColorWidth / 2f
        return colorDistribution
    }

    companion object {
        private const val DEFAULT_ANIMATION_DURATION = 1500
        private const val DEFAULT_ANGLE: Byte = 20
        private const val MIN_ANGLE_VALUE: Byte = -45
        private const val MAX_ANGLE_VALUE: Byte = 45
        private const val MIN_MASK_WIDTH_VALUE: Byte = 0
        private const val MAX_MASK_WIDTH_VALUE: Byte = 1
        private const val MIN_GRADIENT_CENTER_COLOR_WIDTH_VALUE: Byte = 0
        private const val MAX_GRADIENT_CENTER_COLOR_WIDTH_VALUE: Byte = 1
    }
}
