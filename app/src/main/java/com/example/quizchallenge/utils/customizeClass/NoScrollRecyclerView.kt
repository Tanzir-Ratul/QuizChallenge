package com.example.quizchallenge.utils.customizeClass

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class NoScrollRecyclerView(context:Context, attrs: AttributeSet): RecyclerView(context, attrs) {
    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return false
    }
}