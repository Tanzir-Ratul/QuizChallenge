package com.example.quizchallenge.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.quizchallenge.ui.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor (private val repository: QuizRepository) : ViewModel(){
}