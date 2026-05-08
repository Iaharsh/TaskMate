package com.example.etharaai.ui.components

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView

class GradientTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr) {

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            val startColor = 0xFF0051FF.toInt()
            val endColor = 0xFF9C55FF.toInt()
            
            paint.shader = LinearGradient(
                0f, 0f, width.toFloat(), 0f,
                intArrayOf(startColor, endColor),
                null, Shader.TileMode.CLAMP
            )
        }
    }
}
