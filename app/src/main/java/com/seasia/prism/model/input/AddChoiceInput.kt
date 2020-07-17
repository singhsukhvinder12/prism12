package com.seasia.prism.model.input

class AddChoiceInput {
    var UserId: String? = null
    var TypeId: String? = null
    var ChoiceQuestionAnswers: ArrayList<ChoiceQuestionAnswersList>? = null

    class ChoiceQuestionAnswersList {
        var ChoiceQuestionId: String? = ""
        var Option: String? = ""

    }
}