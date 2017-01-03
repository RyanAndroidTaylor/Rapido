@file:Suppress("unused")

package com.izeni.rapidocommon.view.widgets


import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.ImageView
import com.izeni.rapidocommon.R
import com.izeni.rapidocommon.e
import kotlin.properties.Delegates

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Izeni, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/
class CircleImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : ImageView(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null, -1)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    private val DEFAULT_TEXT_SIZE: Float = 25f
    private val DEFAULT_TEXT_COLOR: Int = Color.WHITE
    private val DEFAULT_TEXT: String = " "
    private val SCALE_TYPE = ScaleType.CENTER_CROP

    private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
    private val COLORDRAWABLE_DIMENSION = 2

    private val DEFAULT_BORDER_WIDTH = 0
    private val DEFAULT_BORDER_COLOR = Color.BLACK
    private val DEFAULT_FILL_COLOR = Color.TRANSPARENT
    private val DEFAULT_BORDER_OVERLAY = false

    var viewTextColor = DEFAULT_TEXT_COLOR
        set(value) {
            field = value
            invalidateTextPaints()
        }

    var viewText = DEFAULT_TEXT
        set(value) {
            field = value
            invalidate()
        }

    var viewTextSize = DEFAULT_TEXT_SIZE
        set(value) {
            field = value
            invalidateTextPaints()
        }

    var viewFont: Typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        set(value) {
            field = value
            invalidateTextPaints()
        }


    private var mTextPaint: TextPaint = TextPaint()
    private var mViewSize: Float by Delegates.notNull()

    private val mDrawableRect = RectF()
    private val mBorderRect = RectF()

    private val mShaderMatrix = Matrix()
    private val mBitmapPaint = Paint()
    private val mBackgroundPaint = Paint()
    private val mWhitePaint = Paint()
    private val mBorderPaint = Paint()

    private var borderColor = DEFAULT_BORDER_COLOR
        set(@ColorInt value) {
            if (field == value) return

            field = value
            mBorderPaint.color = field
            invalidate()
        }

    private var borderWidth = DEFAULT_BORDER_WIDTH
        set(value) {
            if (field == value) return
            field = value
            setup()
        }

    private var mBitmap: Bitmap? = null
    private var mBitmapShader: BitmapShader by Delegates.notNull()
    private var mBitmapWidth: Int by Delegates.notNull()
    private var mBitmapHeight: Int by Delegates.notNull()

    private var mDrawableRadius: Float by Delegates.notNull()
    private var mBorderRadius: Float by Delegates.notNull()

    private var ready: Boolean = false
    private var setupPending: Boolean = true

    var borderOverlay: Boolean = DEFAULT_BORDER_OVERLAY
        set(value) {
            if (field == value) return
            field = value
            setup()
        }

    var fillColor: Int = DEFAULT_FILL_COLOR
        set(value) {
            if (field == value) return
            field = value
            setup()
        }

    var disableCircularTransformation: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                initializeBitmap()
            }
        }

    init {
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0).let {
                borderWidth = it.getDimensionPixelSize(R.styleable.CircleImageView_panko_border_width, DEFAULT_BORDER_WIDTH)
                borderColor = it.getColor(R.styleable.CircleImageView_panko_border_color, DEFAULT_BORDER_COLOR)
                borderOverlay = it.getBoolean(R.styleable.CircleImageView_panko_border_overlay, DEFAULT_BORDER_OVERLAY)
                if (it.hasValue(R.styleable.CircleImageView_panko_letterText)) {
                    viewText = it.getString(R.styleable.CircleImageView_panko_letterText)
                }

                if (it.hasValue(R.styleable.CircleImageView_panko_fill_color)) {
                    fillColor = it.getColor(R.styleable.CircleImageView_panko_fill_color, DEFAULT_FILL_COLOR)
                }

                viewTextColor = it.getColor(R.styleable.CircleImageView_panko_textColor, DEFAULT_TEXT_COLOR)
                viewTextSize = it.getDimension(R.styleable.CircleImageView_panko_textSize, DEFAULT_TEXT_SIZE)

                it.recycle()
            }
        }

        super.setScaleType(SCALE_TYPE)
        ready = true

        if (setupPending) {
            setup()
            setupPending = false
        }
    }

    private fun invalidateTextPaints() {
        mTextPaint.typeface = viewFont
        mTextPaint.color = viewTextColor
        mTextPaint.textSize = viewTextSize
    }

    override fun getScaleType() = SCALE_TYPE
    override fun setScaleType(scaleType: ScaleType?) {
        if (scaleType != SCALE_TYPE) {
            throw IllegalArgumentException("ScaleType $scaleType not supported.")
        }
    }

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        if (adjustViewBounds) {
            throw IllegalArgumentException("adjustViewBounds not supported.")
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (disableCircularTransformation) {
            super.onDraw(canvas)
            return
        }

        val xPos: Float = mDrawableRect.centerX() - (mTextPaint.measureText(viewText) / 2)
        val yPos: Float = (mDrawableRect.centerY() - (mTextPaint.descent() + mTextPaint.ascent()) / 2f)

        canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mBackgroundPaint)

        canvas.drawText(viewText, xPos, yPos, mTextPaint)

        if (mBitmap != null) {
            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mWhitePaint)
            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mBitmapPaint)
        }

        if (borderWidth > 0) {
            canvas.drawCircle(mBorderRect.centerX(), mBorderRect.centerY(), mBorderRadius, mBorderPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewSize = Math.min(width, height).toFloat()
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    fun clearBitmap() {
        mBitmap = null
        invalidate()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        initializeBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        initializeBitmap()
    }

    private fun applyColorFilter() {
        mBitmapPaint.colorFilter = colorFilter
        invalidate()
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        try {
            val bitmap: Bitmap

            if (drawable is ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG)
            } else {
                bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } catch (ex: Exception) {
            e("Cannot Decode Bitmap", t = ex)
            return null
        }
    }

    private fun initializeBitmap() {
        mBitmap = if (disableCircularTransformation) null else getBitmapFromDrawable(drawable)
        setup()
    }

    private fun setup() {
        if (!ready) {
            setupPending = true; return
        }
        if (width == 0 && height == 0) return

        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = borderColor
        mBorderPaint.strokeWidth = borderWidth.toFloat()


        mBackgroundPaint.style = Paint.Style.FILL
        mBackgroundPaint.isAntiAlias = true
        mBackgroundPaint.color = fillColor

        mWhitePaint.style = Paint.Style.FILL
        mWhitePaint.isAntiAlias = true
        mWhitePaint.color = Color.WHITE

        if (mBitmap != null) {
            mBitmapShader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            mBitmapPaint.isAntiAlias = true
            mBitmapPaint.shader = mBitmapShader

            mBitmapHeight = mBitmap!!.height
            mBitmapWidth = mBitmap!!.width
        }

        mBorderRect.set(calculateBounds())
        mBorderRadius = Math.min((mBorderRect.height() - borderWidth) / 2.0f, (mBorderRect.width() - borderWidth) / 2.0f)

        mDrawableRect.set(mBorderRect)
        if (!borderOverlay && borderWidth > 0) {
            mDrawableRect.inset(borderWidth - 1.0f, borderWidth - 1.0f)
        }
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f)

        invalidateTextPaints()
        applyColorFilter()
        if (mBitmap != null) updateShaderMatrix()
        invalidate()
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = Math.min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        mShaderMatrix.set(null)

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / mBitmapHeight
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f
        } else {
            scale = mDrawableRect.width() / mBitmapWidth
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f
        }

        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate((dx + 0.5f) + mDrawableRect.left, (dy + 0.5f) + mDrawableRect.top)

        mBitmapShader.setLocalMatrix(mShaderMatrix)
    }

}
