package com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.callback

import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetLikeResponse

interface LikeListCallback {
    fun onSuccess(body: GetLikeResponse)
    fun onError()
}