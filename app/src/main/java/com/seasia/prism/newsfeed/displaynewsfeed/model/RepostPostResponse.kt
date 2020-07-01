package com.seasia.prism.newsfeed.displaynewsfeed.model

class RepostPostResponse {

    var Message: String? = null
    var Status: String? = null
    var StatusCode: String? = null
    var ResourceType: String? = null
    var ResultData:ResultDataList? = null

    class ResultDataList {
        var IsIssueReported: String? = null
        var IssueReportedId: String? = null
    }

}