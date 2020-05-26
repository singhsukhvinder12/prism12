package com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.presenter

import com.e.seasianoticeboard.api.GetRestAdapter
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetLikeResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.view.LikeListActivity
import retrofit2.Call
import retrofit2.Callback

class LikeListPresenter(var likeListActivity: LikeListActivity) {

    fun getLikeList(postId: String){
        val call = GetRestAdapter.getRestAdapter(true).getLikeList(postId)
        call.enqueue(object : Callback<GetLikeResponse> {
            override fun onResponse(call: Call<GetLikeResponse>, response: retrofit2.Response<GetLikeResponse>?
            ) {
                if (response!!.code() == 500) {
                    UtilsFunctions.showToastError(response.message())
                    likeListActivity.onError()
                    return
                }
                if (response?.body()?.StatusCode == "200") {
                    likeListActivity.onSuccess(response.body())
                } else {
                    likeListActivity.onError()
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }
            }

            override fun onFailure(call: Call<GetLikeResponse>, t: Throwable) {
                likeListActivity.onError()
                UtilsFunctions.showToastError(t.message)
            }
        })
    }
}