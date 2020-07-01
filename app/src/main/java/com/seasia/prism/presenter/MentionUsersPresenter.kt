package com.seasia.prism.presenter

import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.api.GetRestAdapter
import com.seasia.prism.core.ui.SearchUserActivity
import com.seasia.prism.model.input.SearchInput
import com.seasia.prism.model.output.SearchResponse
import com.seasia.prism.newsfeed.displaynewsfeed.view.CommentActivity
import com.seasia.prism.util.UtilsFunctions
import retrofit2.Call
import retrofit2.Callback

class MentionUsersPresenter (var searchUserActivity: CommentActivity) {


    fun getData(input: SearchInput) {
        val call = GetRestAdapter.getRestAdapter(true).searchUser(input)
        call.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: retrofit2.Response<SearchResponse>?
            ) {
                if ((response!!.code() == 500)) {
                    UtilsFunctions.showToastError("Internal server error")
                    searchUserActivity.onError()
                } else {
                    if (response.body() != null && response.body()?.StatusCode == "200") {

                        searchUserActivity.onSuccess(response.body().ResultData)
                    } else {
                        searchUserActivity.onError()
                        UtilsFunctions.showToastError(response.body()?.Message)
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                searchUserActivity.onError()
                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))
            }
        })
    }
}
