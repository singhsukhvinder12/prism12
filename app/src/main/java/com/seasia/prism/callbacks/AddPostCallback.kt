package com.seasia.prism.callbacks

import com.seasia.prism.newsfeed.AddPostResponse


interface AddPostCallback {
    fun onSuccess(body: AddPostResponse)
    fun onError()
}