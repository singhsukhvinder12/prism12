package com.seasia.prism.core.newsfeed.displaynewsfeed.model


class GetCommentResponse {
    var Status: String? = null

    var ResultData: ResultDataModel? = null

    var Message: String? = null

    var ResourceType: String? = null

    var StatusCode: String? = null
    public class ResultDataModel{
        var Count: String? = null

        var lstgetCommentViewModel: ArrayList<LstgetCommentViewModelList>?=null
        public class LstgetCommentViewModelList{
            var Comment: String? = null

            var AttachmentUrl: String? = null
            var ImageUrl: String? = null
            var CommentedById: String? = null

            var CommentedBy: String? = null

            var CreatedDate: String? = null

            var dummyDate: Boolean = false

            var strCreatedDate: String?=null

            var Count: String? = null

            var CommentId: String? = null

            var PostedBy: String? = null
        }

    }
}