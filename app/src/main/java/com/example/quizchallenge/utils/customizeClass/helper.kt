package com.example.quizchallenge.utils.customizeClass

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView

fun dpToPxx(context: Context, dp: Int): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density).toInt()
}
fun TextView.setTextViewMargin(left: Int, top: Int, right: Int, bottom: Int) {
    val layoutParams = this.layoutParams as LinearLayout.LayoutParams
    layoutParams.setMargins(left, top, right, bottom)
    this.layoutParams = layoutParams
}

fun addPluralOrSingularity(number: Int?, word: String?): String {
    try{
        if (number != null) {
            return if (number > 1) {
                number.toString() + " " + word + "s"
            } else {
                "$number $word"
            }
        }
        return "0 $word"
    }catch (e: Exception){
        e.printStackTrace()
        return "0 $word"
    }
}