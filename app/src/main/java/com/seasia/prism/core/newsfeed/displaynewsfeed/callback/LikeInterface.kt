package com.seasia.prism.core.newsfeed.displaynewsfeed.callback

import com.seasia.prism.core.newsfeed.displaynewsfeed.model.LikesResponse


interface LikeInterface {
    fun onLikeSuccess(body: LikesResponse)
    fun onError()
}