package com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.callback

import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.RepostPostResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.DeletePostResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedResponse

interface NewsFeedCallback {
    fun onSuccess(resultData: ArrayList<GetFeedResponse.ResultDataList>?)
    fun onError()
    fun onDeletePost(body: DeletePostResponse)
    fun onRepostPost(body: RepostPostResponse)
}