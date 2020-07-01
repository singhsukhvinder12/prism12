package com.seasia.prism.newsfeed.displaynewsfeed.callback

import com.seasia.prism.newsfeed.displaynewsfeed.model.LikesResponse


interface LikeInterface {
    fun onLikeSuccess(body: LikesResponse)
    fun onError()
}