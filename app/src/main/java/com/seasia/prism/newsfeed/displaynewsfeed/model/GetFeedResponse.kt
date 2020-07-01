package com.seasia.prism.newsfeed.displaynewsfeed.model

import java.io.Serializable


class GetFeedResponse:Serializable {
    var Status: String? = null

    var ResultData: ArrayList<ResultDataList>? = null

    var Message: String? = null

    var ResourceType: String? = null

    var StatusCode: String? = null


    public class ResultDataList:Serializable {
        var Description: String? = null

        var IsLikedByMe: String? = null
        var Email: String? = null

        var Comments: String? = null
        var PostedByImageUrl : String? = null


        var Title: String? = null

        var lstComments: ArrayList<LstComments>? = null

        var TotalLikes: String? = null

        var lstDocuments: ArrayList<LstDocuments>? = null
        var lstTaggedUsers: ArrayList<lstTaggedUsersList>? = null

        var lstLikes: ArrayList<lstLikesList>? = null

        var TotalComments: String? = null

        var PostedBy: String? = null

        var PostedById: String? = null

        var Documents: String? = null

        var Links: String? = null

        var PostedDate: String? = null
        var strCreatedDate: String? = null

        var NewsLetterId: String? = null

        var LikedByMe: String? = null

        var LikedBy: String? = null
        var Bio: String? = null


        class lstLikesList :Serializable{
            var id:String?=null
            var Name:String?=null
        }


        class LstComments:Serializable {
            var Comment: String? = null

            var CommentedBy: String? = null

            var CommentedDate: String? = null

            var URL: String? = null

            var CommentId: String? = null

        }

        class lstTaggedUsersList{
            var Id:String?=null
            var Name:String?=null
            var Bio:String?=null
            var Email:String?=null
            var ImageUrl:String?=null
        }

        class LstDocuments :Serializable{
            var Type: String? = null

            var DocumentId: String? = null

            var URL: String? = null

        }

    }
}