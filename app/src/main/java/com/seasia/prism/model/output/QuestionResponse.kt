package com.seasia.prism.model.output

import java.io.Serializable

class QuestionResponse:Serializable {

    var Message: String? = null
    var Status: String? = null
    var StatusCode: String? = null
    var ResultData: ArrayList<ResultDataList>? = null
    var ResourceType: String? = null
    public class ResultDataList:Serializable{
        var QuestionId:String?=null
        var Question:String?=null
        var QuestionAnswerId:String?=null
        var Answer:String?=null
        var AddYourAns=""
    }
}