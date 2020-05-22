package com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.presenter

import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.LikesResponse

interface LikeInterface {
    fun onSuccess(body: LikesResponse)
    fun onError()
}