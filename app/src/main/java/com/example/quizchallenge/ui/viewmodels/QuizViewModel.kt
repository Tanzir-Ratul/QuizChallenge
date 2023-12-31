package com.example.quizchallenge.ui.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.quizchallenge.ui.models.QuizModel
import com.example.quizchallenge.ui.repository.QuizRepository
import com.example.quizchallenge.ui.repository.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(private val repository: QuizRepository) : ViewModel() {

    @Inject lateinit var sharedPreferencesManager: SessionManager
    private var _quizList = MutableLiveData<QuizModel?>()
    val quizList: LiveData<QuizModel?> = _quizList

    private var _answerList = MutableLiveData<QuizModel.Question.Answers?>()
    val answerList: LiveData<QuizModel.Question.Answers?> = _answerList

    private var _question = MutableLiveData<List<QuizModel.Question?>?>()
    val question: LiveData<List<QuizModel.Question?>?> = _question
    var currentPos = MutableLiveData<Int>().apply { value = 0 }
    var highScore = MutableLiveData<Int>().apply { value = 0 }
    init{
        getQuizList()
    }
    private fun getQuizList() {
        viewModelScope.launch {
            val response = repository.getQuizData()
            if (response != null ) {
                _quizList.postValue(response)
                _question.value = response.questions
                //_answerList.value = response.questions?.get(0)?.answers

                Log.d("QuizViewModel", "getQuizList: ${response}")
            }


        }
    }
   fun checkHighScoreBit(newScore:Int?){
       highScore.value = newScore?.let { highScore.value?.plus(it) }
       if((sharedPreferencesManager.highScore ?: 0) < (highScore.value ?: 0)){
           sharedPreferencesManager.highScore = highScore.value

       }

   }
    fun getCurrentPosition(): Int {
        currentPos.value =  currentPos.value?.plus(1) ?: 0
        return currentPos.value?:0
    }


}