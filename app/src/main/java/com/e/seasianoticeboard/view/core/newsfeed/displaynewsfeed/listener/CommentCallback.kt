package com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.listener

import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.CommentResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.DeleteCommentResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetCommentResponse

interface CommentCallback {

    fun getCommentList(comentList: GetCommentResponse)
    fun deleteComment(deleteComment: DeleteCommentResponse)
    fun sendComment(sendComment: CommentResponse)
    fun onError()
}