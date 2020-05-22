package com.e.seasianoticeboard.callbacks

import com.e.seasianoticeboard.views.institute.newsfeed.AddPostResponse

interface AddPostCallback {
    fun onSuccess(body: AddPostResponse)
    fun onError()
}