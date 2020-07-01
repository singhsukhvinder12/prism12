package com.seasia.prism.model.output

class SearchResponse {
    private val Status: String? = null

    var ResultData: ArrayList<ResultDataList>?=null

    var Message: String? = null

    var ResourceType: String? = null

    var StatusCode: String? = null

    class ResultDataList
    {
        var  Email:String?=null

        var  Address:String?=null

        var  PhoneNo:String?=null

        var  UserId:String?=null

        var  FirstName:String?=null

        var  DOB:String?=null

        var  ImageUrl:String?=null

        var  LastName:String?=null

        var  Gender:String?=null

        var  StrDOB:String?=null
        var  UserName:String?=null
        var  Bio:String?=null
        var  isSelected="false"
    }

}