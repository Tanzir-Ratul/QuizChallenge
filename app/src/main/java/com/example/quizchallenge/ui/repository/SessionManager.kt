package com.example.quizchallenge.ui.repository

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(private val sharedPreferences: SharedPreferences) {
    private val editor = sharedPreferences.edit()

    var highScore: Int?
        get() = sharedPreferences.getInt("highScore", 0)
        set(value) {
            if (value != null) {
                editor.putInt("highScore", value)
                editor.apply()
            }
        }

    companion object {
        const val PREF_FILE_NAME = "SHARED_PREF"

    }
}