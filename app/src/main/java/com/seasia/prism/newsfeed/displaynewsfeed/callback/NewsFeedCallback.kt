package com.seasia.prism.newsfeed.displaynewsfeed.callback

import com.seasia.prism.newsfeed.displaynewsfeed.model.DeletePostResponse
import com.seasia.prism.newsfeed.displaynewsfeed.model.GetFeedResponse
import com.seasia.prism.newsfeed.displaynewsfeed.model.RepostPostResponse

interface NewsFeedCallback {
    fun onSuccess(resultData: ArrayList<GetFeedResponse.ResultDataList>?)
    fun onError()
    fun onDeletePost(body: DeletePostResponse)
    fun onRepostPost(body: RepostPostResponse)
}