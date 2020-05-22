package com.e.seasianoticeboard.model

class VerifyEmailResponse {
    var Message:String?=null
    var Status:String?=null
    var StatusCode:String?=null
    var ResultData:ResultDataList?=null
    var ResourceType:String?=null

    class ResultDataList {

       var IsDeleted:String?=null

        var  Email:String?=null

        var  CreatedBy:String?=null

        var  Address:String?=null

        var  FirstName:String?=null

        var  IsActive:String?=null

        var  IsRegister:String?=null

        var  ImageUrl:String?=null

        var  Gender:String?=null

        var  City:String?=null

        var  ModifiedDate:String?=null

        var  ModifiedBy:String?=null

        var  ThumbImageUrl:String?=null

        var  CityId:String?=null

        var  PhoneNo:String?=null

        var  UserId:String?=null

        var  DOB:String?=null

        var  CreatedDate:String?=null

        var  LastName:String?=null

        var  Password:String?=null
    }

}