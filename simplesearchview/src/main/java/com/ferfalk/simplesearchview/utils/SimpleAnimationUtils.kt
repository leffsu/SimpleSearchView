@file:Suppress("unused")

package com.ferfalk.simplesearchview.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Interpolator
import androidx.annotation.RequiresApi
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import java.util.*
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author Fernando A. H. Falkiewicz
 */
object SimpleAnimationUtils {
    const val ANIMATION_DURATION_DEFAULT = 250

    val defaultInterpolator: Interpolator
        get() = FastOutSlowInInterpolator()

    @JvmStatic
    fun revealOrFadeIn(view: View, duration: Int, center: Point?): Animator {
        return revealOrFadeIn(view, duration, null, center)
    }

    @JvmStatic
    fun revealOrFadeIn(view: View, listener: AnimationListener?): Animator {
        return revealOrFadeIn(view, ANIMATION_DURATION_DEFAULT, listener, null)
    }

    @JvmStatic
    fun revealOrFadeIn(view: View, center: Point?): Animator {
        return revealOrFadeIn(view, ANIMATION_DURATION_DEFAULT, null, center)
    }

    @JvmStatic
    fun revealOrFadeIn(view: View, listener: AnimationListener?, center: Point?): Animator {
        return revealOrFadeIn(view, ANIMATION_DURATION_DEFAULT, listener, center)
    }

    @JvmStatic
    @JvmOverloads
    fun revealOrFadeIn(view: View, duration: Int = ANIMATION_DURATION_DEFAULT, listener: AnimationListener? = null, center: Point? = null): Animator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            reveal(view, duration, listener, center)
        } else {
            fadeIn(view, duration, listener)
        }
    }

    @JvmStatic
    fun hideOrFadeOut(view: View, duration: Int, center: Point?): Animator? {
        return hideOrFadeOut(view, duration, null, center)
    }

    @JvmStatic
    fun hideOrFadeOut(view: View, listener: AnimationListener?): Animator? {
        return hideOrFadeOut(view, ANIMATION_DURATION_DEFAULT, listener, null)
    }

    @JvmStatic
    fun hideOrFadeOut(view: View, center: Point?): Animator? {
        return hideOrFadeOut(view, ANIMATION_DURATION_DEFAULT, null, center)
    }

    @JvmStatic
    fun hideOrFadeOut(view: View, listener: AnimationListener?, center: Point?): Animator? {
        return hideOrFadeOut(view, ANIMATION_DURATION_DEFAULT, listener, center)
    }

    @JvmStatic
    @JvmOverloads
    fun hideOrFadeOut(view: View, duration: Int = ANIMATION_DURATION_DEFAULT, listener: AnimationListener? = null, center: Point? = null): Animator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hide(view, duration, listener, center)
        } else {
            fadeOut(view, duration, listener)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun reveal(view: View, duration: Int, center: Point?): Animator {
        return reveal(view, duration, null, center)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun reveal(view: View, listener: AnimationListener?): Animator {
        return reveal(view, ANIMATION_DURATION_DEFAULT, listener, null)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun reveal(view: View, center: Point?): Animator {
        return reveal(view, ANIMATION_DURATION_DEFAULT, null, center)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun reveal(view: View, listener: AnimationListener?, center: Point?): Animator {
        return reveal(view, ANIMATION_DURATION_DEFAULT, listener, center)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @JvmOverloads
    fun reveal(view: View, duration: Int = ANIMATION_DURATION_DEFAULT, listener: AnimationListener? = null, center: Point? = null): Animator {
        var center = center
        if (center == null) {
            center = getDefaultCenter(view)
        }

        val anim = ViewAnimationUtils.createCircularReveal(view, center.x, center.y, 0f, getRevealRadius(center, view).toFloat())
        anim.addListener(object : DefaultActionAnimationListener(view, listener) {
            override fun defaultOnAnimationStart(view: View) {
                view.visibility = View.VISIBLE
            }
        })

        anim.duration = duration.toLong()
        anim.interpolator = defaultInterpolator
        return anim
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun hide(view: View, duration: Int, center: Point?): Animator? {
        return hide(view, duration, null, center)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun hide(view: View, listener: AnimationListener?): Animator? {
        return hide(view, ANIMATION_DURATION_DEFAULT, listener, null)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun hide(view: View, center: Point?): Animator? {
        return hide(view, ANIMATION_DURATION_DEFAULT, null, center)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun hide(view: View, listener: AnimationListener?, center: Point?): Animator? {
        return hide(view, ANIMATION_DURATION_DEFAULT, listener, center)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @JvmOverloads
    fun hide(view: View?, duration: Int = ANIMATION_DURATION_DEFAULT, listener: AnimationListener? = null, center: Point? = null): Animator? {

        if (view?.isAttachedToWindow != true) {
            return null
        }

        val center = center ?: getDefaultCenter(view)

        val anim = ViewAnimationUtils.createCircularReveal(view, center.x, center.y, getRevealRadius(center, view).toFloat(), 0f)
        anim.addListener(object : DefaultActionAnimationListener(view, listener) {
            override fun defaultOnAnimationEnd(view: View) {
                view.visibility = View.GONE
            }
        })

        anim.duration = duration.toLong()
        anim.interpolator = defaultInterpolator
        return anim
    }


    internal fun getDefaultCenter(view: View): Point {
        return Point(view.width / 2, view.height / 2)
    }

    internal fun getRevealRadius(center: Point, view: View): Int {
        var radius = 0f
        val points = ArrayList<Point>()
        points.add(Point(view.left, view.top))
        points.add(Point(view.right, view.top))
        points.add(Point(view.left, view.bottom))
        points.add(Point(view.right, view.bottom))

        for (point in points) {
            val distance = distance(center, point)
            if (distance > radius) {
                radius = distance
            }
        }

        return ceil(radius.toDouble()).toInt()
    }


    fun distance(first: Point, second: Point): Float {
        return sqrt((first.x - second.x).toDouble().pow(2.0) + (first.y - second.y).toDouble().pow(2.0)).toFloat()
    }

    fun fadeIn(view: View, listener: AnimationListener?): Animator {
        return fadeIn(view, ANIMATION_DURATION_DEFAULT, listener)
    }

    @JvmOverloads
    fun fadeIn(view: View, duration: Int = ANIMATION_DURATION_DEFAULT, listener: AnimationListener? = null): Animator {
        if (view.alpha == 1f) {
            view.alpha = 0f
        }

        val anim = ObjectAnimator.ofFloat(view, "alpha", 1f)
        anim.addListener(object : DefaultActionAnimationListener(view, listener) {
            override fun defaultOnAnimationStart(view: View) {
                view.visibility = View.VISIBLE
            }
        })

        anim.duration = duration.toLong()
        anim.interpolator = defaultInterpolator
        return anim
    }

    fun fadeOut(view: View, listener: AnimationListener?): Animator {
        return fadeOut(view, ANIMATION_DURATION_DEFAULT, listener)
    }

    @JvmOverloads
    fun fadeOut(view: View, duration: Int = ANIMATION_DURATION_DEFAULT, listener: AnimationListener? = null): Animator {
        val anim = ObjectAnimator.ofFloat(view, "alpha", 0f)
        anim.addListener(object : DefaultActionAnimationListener(view, listener) {
            override fun defaultOnAnimationEnd(view: View) {
                view.visibility = View.GONE
            }
        })

        anim.duration = duration.toLong()
        anim.interpolator = defaultInterpolator
        return anim
    }

    @JvmOverloads
    fun verticalSlideView(view: View, fromHeight: Int, toHeight: Int, listener: AnimationListener? = null): Animator {
        return verticalSlideView(view, fromHeight, toHeight, ANIMATION_DURATION_DEFAULT, listener)
    }

    @JvmStatic
    @JvmOverloads
    fun verticalSlideView(view: View, fromHeight: Int, toHeight: Int, duration: Int, listener: AnimationListener? = null): Animator {
        val anim = ValueAnimator
                .ofInt(fromHeight, toHeight)

        anim.addUpdateListener({ animation ->
            view.layoutParams.height = animation.animatedValue as Int
            view.requestLayout()
        })

        anim.addListener(DefaultActionAnimationListener(view, listener))

        anim.duration = duration.toLong()
        anim.interpolator = defaultInterpolator
        return anim
    }


    interface AnimationListener {
        /**
         * @return return true to override the default behaviour
         */
        fun onAnimationStart(view: View): Boolean

        /**
         * @return return true to override the default behaviour
         */
        fun onAnimationEnd(view: View): Boolean

        /**
         * @return return true to override the default behaviour
         */
        fun onAnimationCancel(view: View): Boolean
    }

    private open class DefaultActionAnimationListener internal constructor(private val view: View, private val listener: AnimationListener?) : AnimatorListenerAdapter() {

        override fun onAnimationStart(animation: Animator) {
            if (listener == null || !listener.onAnimationStart(view)) {
                defaultOnAnimationStart(view)
            }
        }

        override fun onAnimationEnd(animation: Animator) {
            if (listener == null || !listener.onAnimationEnd(view)) {
                defaultOnAnimationEnd(view)
            }
        }

        override fun onAnimationCancel(animation: Animator) {
            if (listener == null || !listener.onAnimationCancel(view)) {
                defaultOnAnimationCancel(view)
            }
        }

        internal open fun defaultOnAnimationStart(view: View) {
            // No default action
        }

        internal open fun defaultOnAnimationEnd(view: View) {
            // No default action
        }

        internal fun defaultOnAnimationCancel(view: View) {
            // No default action
        }
    }
}
