package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model


import com.e.seasianoticeboard.api.GetRestAdapter
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.CasesRequest
import com.ngo.ui.home.fragments.cases.presenter.NewsFeedImplClass
import com.e.seasianoticeboard.utils.UtilsFunctions
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback

class NewsFeedModel(private var presenter: NewsFeedImplClass) {

    private val imgMediaType = "image/*"

    fun fetchComplaints(casesRequest: CasesRequest, token: String, userRole: String, UserId: Int) {
        var getNewsInput = GetFeedInput()
        getNewsInput.UserId = UserId.toString()

        val call = GetRestAdapter.getRestAdapter(true).getPostDara(getNewsInput)
        call.enqueue(object : Callback<GetFeedResponse> {
            override fun onResponse(
                call: Call<GetFeedResponse>, response: retrofit2.Response<GetFeedResponse>
            ) {
                if (response.body()?.StatusCode == "200") {
                    presenter.onGetCompaintsSuccess(response.body().ResultData!!)

                }
            }

            override fun onFailure(call: Call<GetFeedResponse>, t: Throwable) {
                UtilsFunctions.showToastError(t.message)
            }
        })
    }
}

