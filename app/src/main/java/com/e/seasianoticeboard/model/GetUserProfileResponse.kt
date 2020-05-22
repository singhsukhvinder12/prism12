package com.e.seasianoticeboard.model

class GetUserProfileResponse {
    var Status: String? = null

    var ResultData: ResultDataList? = null

    var Message: String? = null

    var ResourceType: String? = null

    var StatusCode: String? = null

    public class ResultDataList {
        var Email: String? = null

        var Address: String? = null

        var PhoneNo: String? = null

        var UserId: String? = null

        var FirstName: String? = null

        var DOB: String? = null

        var ImageUrl: String? = null

        var LastName: String? = null

        var Gender: String? = null

        var StrDOB: String? = null
    }

}