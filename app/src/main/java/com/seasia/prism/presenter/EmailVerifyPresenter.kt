package com.seasia.prism.presenter

import com.seasia.prism.api.GetRestAdapter
import com.seasia.prism.model.SignupVerificationResponse
import com.seasia.prism.util.UtilsFunctions
import com.seasia.prism.core.auth.EmailActivity
import retrofit2.Call
import retrofit2.Callback

class EmailVerifyPresenter(var emailActivity: EmailActivity) {


    fun signupVerification(input: String) {
        val call = GetRestAdapter.getRestAdapter(true).signupVerification(input)
        call.enqueue(object : Callback<SignupVerificationResponse> {
            override fun onResponse(
                call: Call<SignupVerificationResponse>,
                response: retrofit2.Response<SignupVerificationResponse>?
            ) {

                if ((response!!.code() == 500)) {
                    UtilsFunctions.showToastError("Internal server error")
                    emailActivity.onError()
                } else {
                    if (response?.body() != null && response?.body()?.StatusCode == "200") {
                        emailActivity.onSuccess(response.body())
                    } else {
                        emailActivity.onError()
                        UtilsFunctions.showToastError(response?.body()?.Message)
                    }
                }
            }

            override fun onFailure(call: Call<SignupVerificationResponse>, t: Throwable) {
                emailActivity.onError()
                UtilsFunctions.showToastError(t.message)
            }
        }) }
}