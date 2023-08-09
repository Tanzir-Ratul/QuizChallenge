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
            var a: String?, // 1997
            var b: String?, // 1999
            var c: String?, // 2001
            var d: String? // 2004
        )
    }
}