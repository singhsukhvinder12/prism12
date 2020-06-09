package com.seasia.prism.presenter

import com.seasia.prism.App
import com.seasia.prism.MainActivity
import com.seasia.prism.R
import com.seasia.prism.api.GetRestAdapter
import com.seasia.prism.model.output.LogoutResponse
import com.seasia.prism.util.UtilsFunctions
import retrofit2.Call
import retrofit2.Callback

class LogoutPresenter(var mainActivity: MainActivity) {

    fun hitApiLogout(userId: String, deviceType: String) {
        val call = GetRestAdapter.getRestAdapter(true).logout(userId,deviceType)
        call.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(
                call: Call<LogoutResponse>,
                response: retrofit2.Response<LogoutResponse>?
            ) {

                if ((response!!.code() == 500)) {
                    UtilsFunctions.showToastError("Internal server error")
                    mainActivity.onFailer()
                } else {
                    if (response?.body() != null) {
                        mainActivity.onSuccess(response.body())
                    } else {
                        mainActivity.onFailer()
                        UtilsFunctions.showToastError(response?.body()?.Message)
                    }
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                mainActivity.onFailer()
                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))

            }
        }) }
}