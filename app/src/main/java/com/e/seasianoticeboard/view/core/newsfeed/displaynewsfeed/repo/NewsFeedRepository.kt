package com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.repo

import androidx.lifecycle.MutableLiveData
import com.e.seasianoticeboard.api.GetRestAdapter
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.CasesRequest
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedInput
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedResponse
import retrofit2.Call
import retrofit2.Callback

class NewsFeedRepository {

        var feedData:MutableLiveData<ArrayList<GetFeedResponse.ResultDataList>>?=null

    init {
        feedData = MutableLiveData()
    }


    fun fetchComplaints(UserId: Int): MutableLiveData<ArrayList<GetFeedResponse.ResultDataList>> {
        var getNewsInput = GetFeedInput()
        getNewsInput.UserId = UserId.toString()

        val call = GetRestAdapter.getRestAdapter(true).getPostDara(getNewsInput)
        call.enqueue(object : Callback<GetFeedResponse> {
            override fun onResponse(
                call: Call<GetFeedResponse>, response: retrofit2.Response<GetFeedResponse>
            ) {
                if (response.body()?.StatusCode == "200") {
                    ///        presenter.onGetCompaintsSuccess(response.body().ResultData!!)
                    feedData!!.value = response.body().ResultData
                } else {
                  //  feedData!!.value = null
                }
            }

            override fun onFailure(call: Call<GetFeedResponse>, t: Throwable) {
                UtilsFunctions.showToastError(t.message)
                feedData!!.value = null
            }
        })
        return feedData!!
    }
}