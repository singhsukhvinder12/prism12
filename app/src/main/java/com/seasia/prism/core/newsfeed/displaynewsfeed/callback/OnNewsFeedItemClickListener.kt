package com.seasia.prism.core.newsfeed.displaynewsfeed.callback

import com.seasia.prism.core.newsfeed.displaynewsfeed.model.GetFeedResponse


interface OnNewsFeedItemClickListener {
    fun onItemClick(complaintsData:ArrayList<GetFeedResponse.ResultDataList>, type:String, position:Int)

    //to delete the complaint/post
    fun onDeleteItem(complaintsData: ArrayList<GetFeedResponse.ResultDataList>)

    //to change the like status
    fun changeLikeStatus(complaintsData: ArrayList<GetFeedResponse.ResultDataList>)

    fun onStatusClick(statusId: String)
}