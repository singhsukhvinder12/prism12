package com.seasia.prism.presenter

import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.api.GetRestAdapter
import com.seasia.prism.core.ui.SearchUserActivity
import com.seasia.prism.model.input.SearchInput
import com.seasia.prism.model.output.SearchResponse
import com.seasia.prism.util.UtilsFunctions
import retrofit2.Call
import retrofit2.Callback

class SearchPresenter(var searchUserActivity: SearchUserActivity, var userList: ArrayList<SearchResponse.ResultDataList>) {


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

                            if (input.Skip.equals("0")) {
                                if (userList != null && userList.size > 0) {
                                    userList.clear()
                                }
                                userList = response.body().ResultData!!
                            } else {
                                userList.addAll(response.body().ResultData!!)
                            }

                            searchUserActivity.onSuccess(userList)
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
