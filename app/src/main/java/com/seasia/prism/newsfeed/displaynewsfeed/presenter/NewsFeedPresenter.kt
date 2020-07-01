package com.seasia.prism.newsfeed.displaynewsfeed.presenter

import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.api.GetRestAdapter
import com.seasia.prism.newsfeed.displaynewsfeed.model.*
import com.seasia.prism.model.DeviceTokenInput
import com.seasia.prism.model.DeviceTokenResponse
import com.seasia.prism.util.UtilsFunctions
import com.seasia.prism.newsfeed.displaynewsfeed.view.NewsFeedFragment

import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import java.util.HashMap

class NewsFeedPresenter(
    var newsFeedFragment: NewsFeedFragment,
    var feedList: ArrayList<GetFeedResponse.ResultDataList>
) {


    fun fetchComplaints(input: GetFeedInput) {

//        var getNewsInput = GetFeedInput()
//        getNewsInput.UserId = UserId.toString()

        val call = GetRestAdapter.getRestAdapter(true).getPostDara(input)
        call.enqueue(object : Callback<GetFeedResponse> {
            override fun onResponse(
                call: Call<GetFeedResponse>, response: retrofit2.Response<GetFeedResponse>
            ) {
                if (response.code() == 500) {
                    UtilsFunctions.showToastError(response.message())
                    newsFeedFragment.onError()
                    return
                }
                if (response.body()?.StatusCode == "200") {


                    if (input.Skip.equals("0")) {
                        if (feedList != null && feedList.size > 0) {
                            feedList.clear()
                        }
                        feedList = response.body().ResultData!!
                    } else {
                        feedList.addAll(response.body().ResultData!!)

                    }
                    newsFeedFragment.onSuccess(feedList)
                } else {
                    newsFeedFragment.onError()
                }
            }

            override fun onFailure(call: Call<GetFeedResponse>, t: Throwable) {
                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))

                newsFeedFragment.onError()
            }
        })
    }

    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

    fun reportPost(input: ReportPostInput) {


        val map = HashMap<String, RequestBody>()
        map["IssueReportedId"] = toRequestBody(input.IssueReportedId.toString())
        map["ReportId"] = toRequestBody(input.ReportId.toString())
        map["NewsletterId"] = toRequestBody(input.NewsletterId.toString())
        map["UserId"] = toRequestBody(input.UserId.toString())


        val call = GetRestAdapter.getRestAdapter(true).reportPost(map)
        call.enqueue(object : Callback<RepostPostResponse> {
            override fun onResponse(
                call: Call<RepostPostResponse>, response: retrofit2.Response<RepostPostResponse>?
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

                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))

            }
        })
    }

    fun deletaPost(input: DeletePostInput) {
        val call = GetRestAdapter.getRestAdapter(true).deletePost(input)
        call.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(
                call: Call<DeletePostResponse>, response: retrofit2.Response<DeletePostResponse>?
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
                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))

            }
        })

    }

    fun sendDeviceToken(input: DeviceTokenInput) {
        val call = GetRestAdapter.getRestAdapter(true).sendDeviceToken(input)
        call.enqueue(object : Callback<DeviceTokenResponse> {
            override fun onResponse(
                call: Call<DeviceTokenResponse>, response: retrofit2.Response<DeviceTokenResponse>?
            ) {
                if (response!!.code() == 500) {
                    // UtilsFunctions.showToastError(response.message())
                    // newsFeedFragment.onError()
                    return
                }
            }

            override fun onFailure(call: Call<DeviceTokenResponse>, t: Throwable) {
                newsFeedFragment.onError()
                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))

            }
        })
    }
}