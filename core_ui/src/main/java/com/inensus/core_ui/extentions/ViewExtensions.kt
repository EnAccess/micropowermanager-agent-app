package com.inensus.core_ui.extentions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.children

fun View.show() {
    alpha = 1f
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.animateGone(duration: Long = 300) {
    if (isVisible()) {
        this
            .animate()
            .alpha(0.0f)
            .setDuration(duration)
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        visibility = View.GONE
                    }
                },
            ).start()
    }
}

fun View.animateShow(duration: Long = 300) {
    if (!this.isVisible()) {
        alpha = 0.0f
        animate()
            .alpha(1.0f)
            .setDuration(duration)
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        visibility = View.VISIBLE
                    }
                },
            ).start()
    }
}

fun View.isVisible() = visibility == View.VISIBLE

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        },
    )
}

fun ViewGroup.setEnabledWithChildren(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    children.forEach {
        it.isEnabled = isEnabled
        if (it is ViewGroup) {
            it.setEnabledWithChildren(isEnabled)
        }
    }
}
