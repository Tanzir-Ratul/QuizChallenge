package com.example.quizchallenge.ui.models



data class QuizModel(
    var questions: List<Question?>?
) {
    data class Question(
        var answers: Answers?,
        var correctAnswer: String?, // B
        var question: String?, // Wann wurde CHECK24 gegründet?
        var questionImageUrl: String?, // https://app.check24.de/vg2-quiz/images/vg2_building_001.jpg
        var score: Int? // 200
    ) {
        data class Answers(
            var A: String? = null , // 1997
            var B: String?= null, // 1999
            var C: String? = null,// 2001
            var D: String? = null// 2004
        )
    }
}