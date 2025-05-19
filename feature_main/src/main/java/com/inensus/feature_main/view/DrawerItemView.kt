package com.inensus.feature_main.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.use
import com.inensus.feature_main.R
import com.inensus.feature_main.databinding.ItemDrawerBinding

class DrawerItemView(
    context: Context,
    attributeSet: AttributeSet,
) : ConstraintLayout(context, attributeSet) {
    @Suppress("RedundantLateinit")
    public lateinit var binding: ItemDrawerBinding
    private var iconRes: Int? = null
    private var title: String? = null

    inline var isItemSelected: Boolean
        get() = background == ContextCompat.getDrawable(context, R.color.gray_F4F4F4)
        set(value) {
            if (value) {
                background = ContextCompat.getDrawable(context, R.color.gray_F4F4F4)

                val typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
                binding.tvTitle.typeface = typeface
                binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            } else {
                background = ContextCompat.getDrawable(context, R.color.white)

                val typeface = ResourcesCompat.getFont(context, R.font.regular)
                binding.tvTitle.typeface = typeface
                binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.gray_616161))
            }
        }

    init {
        binding = ItemDrawerBinding.inflate(LayoutInflater.from(context), this)
        initializeAttrs(context, attributeSet)
    }

    @SuppressLint("Recycle")
    private fun initializeAttrs(
        context: Context,
        attrs: AttributeSet,
    ) {
        context.obtainStyledAttributes(attrs, R.styleable.DrawerItemView).use {
            iconRes = it.getResourceId(R.styleable.DrawerItemView_drawer_icon, 0)
            title = it.getString(R.styleable.DrawerItemView_drawer_title)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        iconRes?.let {
            binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(context, it))
        }
        title?.let {
            binding.tvTitle.text = title
        }
    }
}
