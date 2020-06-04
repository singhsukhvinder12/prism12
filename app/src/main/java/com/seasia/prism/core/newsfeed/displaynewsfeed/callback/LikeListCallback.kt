package com.seasia.prism.core.newsfeed.displaynewsfeed.callback

import com.seasia.prism.core.newsfeed.displaynewsfeed.model.GetLikeResponse

interface LikeListCallback {
    fun onSuccess(body: GetLikeResponse)
    fun onError()
}