package com.seasia.prism.newsfeed.displaynewsfeed.presenter

import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.api.GetRestAdapter
import com.seasia.prism.newsfeed.displaynewsfeed.model.*
import com.seasia.prism.newsfeed.displaynewsfeed.view.CommentActivity
import com.seasia.prism.util.UtilsFunctions
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import java.util.HashMap

class CommentPresenter(var commentActivity: CommentActivity) {

    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

    fun getComment(postId: String) {
        val call = GetRestAdapter.getRestAdapter(true).getCommentList(postId)
        call.enqueue(object : Callback<GetCommentResponse> {
            override fun onResponse(
                call: Call<GetCommentResponse>, response: retrofit2.Response<GetCommentResponse>?
            ) {
                if (response!!.code() == 500) {
                    UtilsFunctions.showToastError(response.message())
                    commentActivity.onError()
                    return
                }
                if (response?.body()?.StatusCode == "200") {
                    commentActivity.getCommentList(response.body())
                } else {
                    commentActivity.onError()
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }
            }

            override fun onFailure(call: Call<GetCommentResponse>, t: Throwable) {
                commentActivity.onError()
                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))

            }
        })
    }

    fun sendComment(input: CommentInput) {



        val map = HashMap<String, RequestBody>()
        // map["CommentId"] = toRequestBody(input.CommentId.toString())
        map["PostId"] = toRequestBody(input.PostId.toString())
        map["CommentedBy"] = toRequestBody(input.CommentedBy.toString())
        map["Comment"] = toRequestBody(input.Comment.toString())
        map["DeletedTagIds[0]"] = toRequestBody("0")

        if(input.TagIds!=null && input.TagIds!!.size>0){
            for (i in 0..input.TagIds!!.size-1) {
                val  requestBody = RequestBody.create(MediaType.parse("text/plain"), input.TagIds!!.get(i))
                map.put("TagIds[$i]", requestBody)
            }
        }

        val call = GetRestAdapter.getRestAdapter(true).viewComment(map)
        call.enqueue(object : Callback<CommentResponse> {
            override fun onResponse(
                call: Call<CommentResponse>, response: retrofit2.Response<CommentResponse>?
            ) {
                if (response!!.code() == 500) {
                    UtilsFunctions.showToastError(response.message())
                    commentActivity.onError()
                    return
                }
                if (response?.body()?.StatusCode == "200") {
                    commentActivity.sendComment(response.body())
                } else {
                    commentActivity.onError()
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }


            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                commentActivity.onError()
                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))

            }
        })
    }


    fun deleteComment(input: DeleteCommentInput) {
        val call = GetRestAdapter.getRestAdapter(true).deleteComment(input)
        call.enqueue(object : Callback<DeleteCommentResponse> {
            override fun onResponse(
                call: Call<DeleteCommentResponse>,
                response: retrofit2.Response<DeleteCommentResponse>?
            ) {
                if (response!!.code() == 500) {
                    UtilsFunctions.showToastError(response.message())
                    commentActivity.onError()
                    return
                }
                if (response?.body()?.StatusCode == "200") {
                    commentActivity.deleteComment(response.body())
                } else {
                    commentActivity.onError()
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }
            }

            override fun onFailure(call: Call<DeleteCommentResponse>, t: Throwable) {
                commentActivity.onError()
                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))

            }
        })
    }

}