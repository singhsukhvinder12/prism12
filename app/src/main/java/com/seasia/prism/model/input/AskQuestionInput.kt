package com.seasia.prism.model.input

class AskQuestionInput {
    var UserId:String?=null
    var TypeId:String?=null
    var QuestionAnswers:ArrayList<QuestionAnswersList>?=null

    class QuestionAnswersList{
        var QuestionId:String?=null
        var Answer:String?=null
    }
}