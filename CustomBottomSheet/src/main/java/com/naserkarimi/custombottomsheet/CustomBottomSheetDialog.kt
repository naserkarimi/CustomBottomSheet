package com.naserkarimi.custombottomsheet

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*


class CustomBottomSheetDialog(context: Context, style: Int,
                              private val bottomView: View?,
                              private val topView: View,
                              private val peekHeight: Int?,
                              private val isCancelable: Boolean = true,
                              private val stateChangedCallback: StateChangedCallback? = null) : Dialog(context, style) {

    private constructor(builder: Builder) : this(builder.context,
            builder.style, builder.bottomView,
            builder.topView, builder.peekHeight,
            builder.isCancelable, builder.stateChangedCallback)

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var backgroundView: View

    class Builder(val context: Context, val topView: View) {
        var style: Int = R.style.TransparentBottomSheetDialog
            private set
        var bottomView: View? = null
            private set
        var peekHeight: Int? = null
            private set
        var isCancelable: Boolean = true
            private set
        var stateChangedCallback: StateChangedCallback? = null
            private set

        fun style(style: Int) = apply { this.style = style }
        fun bottomView(bottomView: View) = apply { this.bottomView = bottomView }
        fun peekHeight(peekHeight: Int) = apply { this.peekHeight = peekHeight }
        fun isCancelable(isCancelable: Boolean) = apply { this.isCancelable = isCancelable }
        fun stateChangedCallback(stateChangedCallback: StateChangedCallback) = apply { this.stateChangedCallback = stateChangedCallback }

        fun build() = CustomBottomSheetDialog(this)
    }

    init {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        setCancelable(isCancelable)
        window?.attributes?.windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.statusBarColor = Color.TRANSPARENT
        }
        window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_bottomsheet_dialog)
        initUi()
    }

    private fun initUi() {
        val topViewContainer: LinearLayout = this.findViewById(R.id.top_view_container)
        topViewContainer.addView(topView)
        bottomSheetBehavior = from(topViewContainer)
        backgroundView = this.findViewById(R.id.background_view)
        if (peekHeight != null)
            bottomSheetBehavior.peekHeight = peekHeight
        val coordinatorLayout: CoordinatorLayout = this.findViewById(R.id.custom_dialog_main_container)
        coordinatorLayout.setOnClickListener {
            if (isCancelable) {
                bottomSheetBehavior.state = STATE_HIDDEN
            }
        }
        if (bottomView != null) {
            val bottomViewContainer: LinearLayout = this.findViewById(R.id.bottom_view_container)
            bottomViewContainer.addView(bottomView)
        }
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            var oldSlide: Float = 0.0f
            override fun onStateChanged(view: View, i: Int) {
                if (i == STATE_HIDDEN) {
                    stateChangedCallback?.onHidden()
                    dismiss();
                } else if (i == STATE_EXPANDED) {
                    stateChangedCallback?.onExpanded()
                } else if (i == STATE_COLLAPSED) {
                    stateChangedCallback?.onCollapsed()
                }
            }

            override fun onSlide(view: View, v: Float) {
                if (oldSlide > 0 && v > 0) {
                    return
                }
                if (v <= 0 || (v > 0 && oldSlide <= 0)) {
                    val animation1 = AlphaAnimation(if (oldSlide > 0) 1F else 1F + oldSlide, if (v > 0) 1F else 1F + v)
                    animation1.duration = 100
                    animation1.fillAfter = true
                    bottomView?.startAnimation(animation1)
                    backgroundView.startAnimation(animation1)
                }
                oldSlide = v
            }
        })
        bottomSheetBehavior.state = STATE_HIDDEN
        setOnShowListener {
            bottomSheetBehavior.state = STATE_COLLAPSED
        }
    }

    fun dismissWithAnimation() {
        bottomSheetBehavior.state = STATE_HIDDEN
    }

    override fun onBackPressed() {
        if (isCancelable)
            bottomSheetBehavior.state = STATE_HIDDEN
    }
}