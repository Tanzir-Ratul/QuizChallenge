package com.example.quizchallenge.ui.repository

import android.content.Context
import com.example.quizchallenge.ui.models.QuizModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

class QuizRepository @Inject constructor (private val ctx: Context) {

    @Inject
    lateinit var sessionManager: SessionManager
    suspend fun getQuizData():QuizModel{
        return withContext(Dispatchers.IO){
            val jsonString = loadJsonFromAsset("quiz.json")
            val gson = Gson()
            gson.fromJson(jsonString, QuizModel::class.java)
        }

    }
    private suspend fun loadJsonFromAsset(filename: String): String {
        return try {
            val inputStream = ctx.assets.open(filename)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (withContext(Dispatchers.IO) {
                    bufferedReader.readLine()
                }.also { line = it } != null) {
                stringBuilder.append(line)
            }
            stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}