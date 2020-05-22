package com.e.seasianoticeboard.views.institute.newsfeed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.seasianoticeboard.App
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.ReportPostInput
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.RepostPostResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.*
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.repo.ComentReposotory

class CommentViewModel:ViewModel() {

    var commentOnPostData=MutableLiveData<CommentResponse>()
    var getLikeList=MutableLiveData<GetLikeResponse>()
    var deleteComment=MutableLiveData<DeleteCommentResponse>()
    var reportPost=MutableLiveData<RepostPostResponse>()
    var getCommentData=MutableLiveData<GetCommentResponse>()
    var likesData: MutableLiveData<LikesResponse>? = null
    var deletePostData: MutableLiveData<DeletePostResponse>? = null
    var commentRepo: ComentReposotory? = null

    init {
        commentRepo = ComentReposotory()
    }

    fun commentSendData(): LiveData<CommentResponse> {
        return commentOnPostData!!
    }



    fun commentOnPost(inputRequest: CommentInput) {
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        commentOnPostData = commentRepo?.sendComment(inputRequest)!!
    }


    fun getCommentData(): LiveData<GetCommentResponse> {
//        if(getCommentData!!.value==null){
//            getCommentData=MutableLiveData()
//        }
        return getCommentData!!
    }


    fun getCommentList(postId: String) {
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        getCommentData = commentRepo!!.getComment(postId)
    }




//    fun getLikesList(inputRequest: LikeInput) {
//        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
//            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
//            return
//        }
//        likesData = commentRepo?.getLikes(inputRequest)
//    }
//    fun getLikedata(): LiveData<LikesResponse> {
//        return likesData!!
//    }


    fun deletePostData(inputRequest: DeletePostInput) {
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        deletePostData = commentRepo?.deletaPost(inputRequest)
    }
    fun getDeletedData(): LiveData<DeletePostResponse> {
//        if(deletePostData!!.value==null){
//            deletePostData=MutableLiveData()
//        }
        return deletePostData!!
    }

    fun getLikeList(postId: String) {
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        getLikeList = commentRepo?.getLikeList(postId)!!
    }
    fun likeData(): LiveData<GetLikeResponse> {
//        if(getLikeList!!.value==null){
//            getLikeList=MutableLiveData()
//        }
        return getLikeList!!
    }


    fun deleteCommentInput(postId: DeleteCommentInput) {
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        deleteComment = commentRepo?.deleteComment(postId)!!
    }
    fun deleteCommentResponse(): LiveData<DeleteCommentResponse> {
//        if(deleteComment!!.value==null){
//            deleteComment=MutableLiveData()
//        }
        return deleteComment!!
    }


    fun reportPostInput(input: ReportPostInput) {
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        reportPost = commentRepo?.reportPost(input)!!
    }
    fun reportPostResponse(): LiveData<RepostPostResponse> {
//        if(reportPost!!.value==null){
//            reportPost= MutableLiveData()
//        }
        return reportPost!!
    }
}