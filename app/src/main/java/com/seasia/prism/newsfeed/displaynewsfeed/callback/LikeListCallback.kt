package com.seasia.prism.newsfeed.displaynewsfeed.callback

import com.seasia.prism.newsfeed.displaynewsfeed.model.GetLikeResponse

interface LikeListCallback {
    fun onSuccess(body: GetLikeResponse)
    fun onError()
}