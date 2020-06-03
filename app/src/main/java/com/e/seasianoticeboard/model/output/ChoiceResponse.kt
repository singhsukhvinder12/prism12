package com.e.seasianoticeboard.model.output

import java.io.Serializable

class ChoiceResponse:Serializable {

    var Status: String? = null

    var ResultData: ArrayList<ResultDataList>?=null

    var Message: String? = null

    var ResourceType: String? = null

    var StatusCode: String? = null
    public class ResultDataList:Serializable{
        var Option2: String? = null
        var Option1: String? = null
        var ChoiceQuestionId: String? = null
        var ChoiceQuestionAnswerId: String? = null
        var Selected: String? = null
        var SelectedOption: String? = null
    }
}