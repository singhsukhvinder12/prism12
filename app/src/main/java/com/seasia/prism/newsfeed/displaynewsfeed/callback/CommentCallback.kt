package com.seasia.prism.newsfeed.displaynewsfeed.callback

import com.seasia.prism.newsfeed.displaynewsfeed.model.CommentResponse
import com.seasia.prism.newsfeed.displaynewsfeed.model.DeleteCommentResponse
import com.seasia.prism.newsfeed.displaynewsfeed.model.GetCommentResponse


interface CommentCallback {

    fun getCommentList(comentList: GetCommentResponse)
    fun deleteComment(deleteComment: DeleteCommentResponse)
    fun sendComment(sendComment: CommentResponse)
    fun onError()
}