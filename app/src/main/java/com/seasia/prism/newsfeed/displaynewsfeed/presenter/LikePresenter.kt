package com.seasia.prism.newsfeed.displaynewsfeed.presenter

import com.seasia.prism.api.GetRestAdapter
import com.seasia.prism.util.UtilsFunctions
import com.seasia.prism.newsfeed.displaynewsfeed.callback.LikeInterface
import com.seasia.prism.newsfeed.displaynewsfeed.model.LikeInput
import com.seasia.prism.newsfeed.displaynewsfeed.model.LikesResponse
import retrofit2.Call
import retrofit2.Callback

class LikePresenter(var callback: LikeInterface) {

    fun getLikes(input: LikeInput) {
        val call = GetRestAdapter.getRestAdapter(true).getLikes(input)
        call.enqueue(object : Callback<LikesResponse> {
            override fun onResponse(call: Call<LikesResponse>, response: retrofit2.Response<LikesResponse>?
            ) {

                if (response?.body()?.StatusCode == "200") {
                    //likeData?.value = response.body()
                    callback.onLikeSuccess(response.body())
                } else {
                   // likeData?.value = null
                    callback.onError()
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }

            }

            override fun onFailure(call: Call<LikesResponse>, t: Throwable) {
              //  likeData?.value = null
                UtilsFunctions.showToastError(t.message)
                callback.onError()
            }
        })

    }

}