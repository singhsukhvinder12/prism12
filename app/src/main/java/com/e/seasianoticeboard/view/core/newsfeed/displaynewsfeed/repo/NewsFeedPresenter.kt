package com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.repo

import com.e.seasianoticeboard.api.GetRestAdapter
import com.e.seasianoticeboard.model.DeviceTokenInput
import com.e.seasianoticeboard.model.DeviceTokenResponse
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.ReportPostInput
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.RepostPostResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.DeletePostInput
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.DeletePostResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedInput
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.pagination.NewsFeedFragment
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import java.util.HashMap

class NewsFeedPresenter(var newsFeedFragment: NewsFeedFragment) {

    fun fetchComplaints(UserId: Int) {
        var getNewsInput = GetFeedInput()
        getNewsInput.UserId = UserId.toString()

        val call = GetRestAdapter.getRestAdapter(true).getPostDara(getNewsInput)
        call.enqueue(object : Callback<GetFeedResponse> {
            override fun onResponse(
                call: Call<GetFeedResponse>, response: retrofit2.Response<GetFeedResponse>
            ) {
                if (response!!.code() == 500) {
                    UtilsFunctions.showToastError(response.message())
                    newsFeedFragment.onError()
                    return
                }
                if (response.body()?.StatusCode == "200") {
                    newsFeedFragment.onSuccess(response.body().ResultData)
                } else {
                    newsFeedFragment.onError()
                }
            }

            override fun onFailure(call: Call<GetFeedResponse>, t: Throwable) {
                UtilsFunctions.showToastError(t.message)
                newsFeedFragment.onError()
            }
        })
    }
    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

    fun reportPost(input: ReportPostInput){



        val map = HashMap<String, RequestBody>()
        map["IssueReportedId"] = toRequestBody(input.IssueReportedId.toString())
        map["ReportId"] = toRequestBody(input.ReportId.toString())
        map["NewsletterId"] = toRequestBody(input.NewsletterId.toString())
        map["UserId"] = toRequestBody(input.UserId.toString())



        val call = GetRestAdapter.getRestAdapter(true).reportPost(map)
        call.enqueue(object : Callback<RepostPostResponse> {
            override fun onResponse(call: Call<RepostPostResponse>, response: retrofit2.Response<RepostPostResponse>?
            ) {

                if (response!!.code() == 500) {
                    UtilsFunctions.showToastError(response.message())
                    newsFeedFragment.onError()
                    return
                }
                if (response?.body()?.StatusCode == "200") {
                    newsFeedFragment.onRepostPost(response.body())
                } else {
                    newsFeedFragment.onError()
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }
            }

            override fun onFailure(call: Call<RepostPostResponse>, t: Throwable) {
                newsFeedFragment.onError()

                UtilsFunctions.showToastError(t.message)
            }
        })
    }

    fun deletaPost(input: DeletePostInput){
        val call = GetRestAdapter.getRestAdapter(true).deletePost(input)
        call.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(call: Call<DeletePostResponse>, response: retrofit2.Response<DeletePostResponse>?
            ) {
                if (response!!.code() == 500) {
                    UtilsFunctions.showToastError(response.message())
                    newsFeedFragment.onError()
                    return
                }
                if (response?.body()?.StatusCode == "200") {
                    newsFeedFragment.onDeletePost(response.body())
                } else {
                    newsFeedFragment.onError()
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }
            }

            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                newsFeedFragment.onError()
                UtilsFunctions.showToastError(t.message)
            }
        })

    }

    fun sendDeviceToken(input:DeviceTokenInput){
        val call = GetRestAdapter.getRestAdapter(true).sendDeviceToken(input)
        call.enqueue(object : Callback<DeviceTokenResponse> {
            override fun onResponse(call: Call<DeviceTokenResponse>, response: retrofit2.Response<DeviceTokenResponse>?
            ) {
                if (response!!.code() == 500) {
                   // UtilsFunctions.showToastError(response.message())
                   // newsFeedFragment.onError()
                    return
                }
            }

            override fun onFailure(call: Call<DeviceTokenResponse>, t: Throwable) {
                newsFeedFragment.onError()
                UtilsFunctions.showToastError(t.message)
            }
        })
    }
}