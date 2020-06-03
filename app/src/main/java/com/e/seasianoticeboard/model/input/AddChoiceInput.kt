package com.e.seasianoticeboard.model.input

class AddChoiceInput {
    var UserId: String? = null
    var ChoiceQuestionAnswers: ArrayList<ChoiceQuestionAnswersList>? = null

    class ChoiceQuestionAnswersList {
        var ChoiceQuestionId: String? = null
        var Option: String? = null
    }
}