package com.seasia.prism.core.newsfeed.displaynewsfeed.presenter

import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.api.GetRestAdapter
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.GetLikeResponse
import com.seasia.prism.core.newsfeed.displaynewsfeed.view.LikeListActivity
import com.seasia.prism.util.UtilsFunctions
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
                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))

            }
        })
    }
}