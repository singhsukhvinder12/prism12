package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.repo

import androidx.lifecycle.MutableLiveData
import com.e.seasianoticeboard.api.GetRestAdapter
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.ReportPostInput
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.RepostPostResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import java.util.HashMap

class ComentReposotory {

    var commentSendData: MutableLiveData<CommentResponse>? = null
    var getCommentData: MutableLiveData<GetCommentResponse>? = null
    var likeList: MutableLiveData<GetLikeResponse>? = null
    var likeData: MutableLiveData<LikesResponse>? = null
    var deletePostData: MutableLiveData<DeletePostResponse>? = null
    var depleteComment: MutableLiveData<DeleteCommentResponse>? = null
    var reportPost: MutableLiveData<RepostPostResponse>? = null

    init {
        commentSendData = MutableLiveData()
        getCommentData = MutableLiveData()
        likeData = MutableLiveData()
        deletePostData = MutableLiveData()
        likeList = MutableLiveData()
        depleteComment = MutableLiveData()
        reportPost = MutableLiveData()
    }


    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }
    fun sendComment(
            input: CommentInput
    ): MutableLiveData<CommentResponse> {

        val map = HashMap<String, RequestBody>()
       // map["CommentId"] = toRequestBody(input.CommentId.toString())
        map["PostId"] = toRequestBody(input.PostId.toString())
        map["CommentedBy"] = toRequestBody(input.CommentedBy.toString())
        map["Comment"] = toRequestBody(input.Comment.toString())



        val call = GetRestAdapter.getRestAdapter(true).viewComment(map)
        call.enqueue(object : Callback<CommentResponse> {
            override fun onResponse(call: Call<CommentResponse>, response: retrofit2.Response<CommentResponse>?
            ) {
                if (response?.body()?.StatusCode == "200") {
                    commentSendData?.value = response.body()
                } else {
                    commentSendData?.value = null
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }

            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                commentSendData?.value = null
                UtilsFunctions.showToastError(t.message)
            }
        })
        return commentSendData!!
    }

    fun getComment(postId: String): MutableLiveData<GetCommentResponse>{
        val call = GetRestAdapter.getRestAdapter(true).getCommentList(postId)
        call.enqueue(object : Callback<GetCommentResponse> {
            override fun onResponse(call: Call<GetCommentResponse>, response: retrofit2.Response<GetCommentResponse>?
            ) {

                if (response?.body()?.StatusCode == "200") {
                    getCommentData?.value = response.body()
                } else {
                    getCommentData!!.value = null
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }
            }

            override fun onFailure(call: Call<GetCommentResponse>, t: Throwable) {
                getCommentData?.value = null
                UtilsFunctions.showToastError(t.message)
            }
        })
        return getCommentData!!
    }




    fun getLikes(input: LikeInput): MutableLiveData<LikesResponse>{
        val call = GetRestAdapter.getRestAdapter(true).getLikes(input)
        call.enqueue(object : Callback<LikesResponse> {
            override fun onResponse(call: Call<LikesResponse>, response: retrofit2.Response<LikesResponse>?
            ) {

                if (response?.body()?.StatusCode == "200") {
                    likeData?.value = response.body()
                } else {
                    likeData?.value = null
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }

            }

            override fun onFailure(call: Call<LikesResponse>, t: Throwable) {
                likeData?.value = null
                UtilsFunctions.showToastError(t.message)
            }
        })
        return likeData!!
    }




    fun deletaPost(input: DeletePostInput): MutableLiveData<DeletePostResponse>{
        val call = GetRestAdapter.getRestAdapter(true).deletePost(input)
        call.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(call: Call<DeletePostResponse>, response: retrofit2.Response<DeletePostResponse>?
            ) {

                if (response?.body()?.StatusCode == "200") {
                    deletePostData?.value = response.body()
                } else {
                    deletePostData?.value = null
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }
            }

            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                deletePostData?.value = null
                UtilsFunctions.showToastError(t.message)
            }
        })
        return deletePostData!!
    }





    fun getLikeList(postId: String): MutableLiveData<GetLikeResponse>{
        val call = GetRestAdapter.getRestAdapter(true).getLikeList(postId)
        call.enqueue(object : Callback<GetLikeResponse> {
            override fun onResponse(call: Call<GetLikeResponse>, response: retrofit2.Response<GetLikeResponse>?
            ) {

                if (response?.body()?.StatusCode == "200") {
                    likeList?.value = response.body()
                } else {
                    likeList!!.value = null
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }
            }

            override fun onFailure(call: Call<GetLikeResponse>, t: Throwable) {
                likeList?.value = null
                UtilsFunctions.showToastError(t.message)
            }
        })
        return likeList!!
    }

    fun deleteComment(input: DeleteCommentInput): MutableLiveData<DeleteCommentResponse>{
        val call = GetRestAdapter.getRestAdapter(true).deleteComment(input)
        call.enqueue(object : Callback<DeleteCommentResponse> {
            override fun onResponse(call: Call<DeleteCommentResponse>, response: retrofit2.Response<DeleteCommentResponse>?
            ) {

                if (response?.body()?.StatusCode == "200") {
                    depleteComment?.value = response.body()
                } else {
                    depleteComment!!.value = null
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }
            }

            override fun onFailure(call: Call<DeleteCommentResponse>, t: Throwable) {
                depleteComment?.value = null
                UtilsFunctions.showToastError(t.message)
            }
        })
        return depleteComment!!
    }



    fun reportPost(input: ReportPostInput): MutableLiveData<RepostPostResponse>{



        val map = HashMap<String, RequestBody>()
        map["IssueReportedId"] = toRequestBody(input.IssueReportedId.toString())
        map["ReportId"] = toRequestBody(input.ReportId.toString())
        map["NewsletterId"] = toRequestBody(input.NewsletterId.toString())
        map["UserId"] = toRequestBody(input.UserId.toString())



        val call = GetRestAdapter.getRestAdapter(true).reportPost(map)
        call.enqueue(object : Callback<RepostPostResponse> {
            override fun onResponse(call: Call<RepostPostResponse>, response: retrofit2.Response<RepostPostResponse>?
            ) {
                if (response?.body()?.StatusCode == "200") {
                    reportPost?.value = response.body()
                } else {
                    reportPost!!.value = null
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }
            }

            override fun onFailure(call: Call<RepostPostResponse>, t: Throwable) {
                reportPost?.value = null
                UtilsFunctions.showToastError(t.message)
            }
        })
        return reportPost!!
    }




}