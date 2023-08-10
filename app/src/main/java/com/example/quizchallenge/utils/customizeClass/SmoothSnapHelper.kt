package com.example.quizchallenge.utils.customizeClass

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class SmoothSnapHelper(val context: Context) : SnapHelper() {
    override fun createScroller(layoutManager: RecyclerView.LayoutManager?): RecyclerView.SmoothScroller? {
        return object:LinearSmoothScroller(context){
            override fun getHorizontalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        return null//calculateDistanceToFinalSnap(layoutManager, targetView)
    }


    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        return null//findSnapView(layoutManager)
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager?,
        velocityX: Int,
        velocityY: Int
    ): Int {
        return RecyclerView.NO_POSITION
    }
}