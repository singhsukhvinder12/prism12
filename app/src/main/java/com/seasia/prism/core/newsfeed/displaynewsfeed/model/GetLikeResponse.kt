package com.seasia.prism.core.newsfeed.displaynewsfeed.model


class GetLikeResponse {
    var Status: String? = null

    var ResultData: ResultDataModel? = null

    var Message: String? = null

    var ResourceType: String? = null

    var StatusCode: String? = null

    public class ResultDataModel {
        var TotalCount: String? = null

        var lstgetLikesListViewModels: ArrayList<LstgetLikesListViewModelsList>? = null

        public class LstgetLikesListViewModelsList {
            var TotalCount: String? = null

            var LikeId: String? = null
            var ImageUrl: String? = null

            var CreatedDate: String? = null

            var LikedBy: String? = null

        }
    }

}