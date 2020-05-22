package com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.presenter

import com.e.seasianoticeboard.api.GetRestAdapter
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.LikeInput
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.LikesResponse
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
                    callback.onSuccess(response.body())
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