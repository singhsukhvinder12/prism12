package com.seasia.prism.presenter

import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.api.GetRestAdapter
import com.seasia.prism.model.SignupVerificationResponse
import com.seasia.prism.model.VerifyEmailInput
import com.seasia.prism.model.VerifyEmailResponse
import com.seasia.prism.util.UtilsFunctions
import com.seasia.prism.core.auth.OtpVerifyActivity
import retrofit2.Call
import retrofit2.Callback

class VerifyOtpPresenter(var otpVerifyActivity: OtpVerifyActivity) {

    fun emailVarified(input: VerifyEmailInput) {
        val call = GetRestAdapter.getRestAdapter(true).verifyEmail(input)
        call.enqueue(object : Callback<VerifyEmailResponse> {
            override fun onResponse(
                call: Call<VerifyEmailResponse>, response: retrofit2.Response<VerifyEmailResponse>?
            ) {
                if (response!!.code() == 500) {
                    UtilsFunctions.showToastError("internal server error")
                    otpVerifyActivity.onFailer()
                } else if (response.body() != null && response.body().StatusCode == "200") {
                    otpVerifyActivity.onOTPSucess(response!!.body())
                }
                else if (response.body() != null && response.body().StatusCode == "411"){
                    UtilsFunctions.showToastError("Invalid OTP")
                    otpVerifyActivity.onFailer()
                }
                else if (response.body() != null) {
                    UtilsFunctions.showToastError(response.body().Message)
                    otpVerifyActivity.onFailer()
                } else {
                    UtilsFunctions.showToastError(response.message())
                    otpVerifyActivity.onFailer()
                }
            }

            override fun onFailure(call: Call<VerifyEmailResponse>, t: Throwable) {
                otpVerifyActivity.onFailer()
                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))

            }
        })
    }

    fun signupVerification(input: String) {
        val call = GetRestAdapter.getRestAdapter(true).signupVerification(input)
        call.enqueue(object : Callback<SignupVerificationResponse> {
            override fun onResponse(
                call: Call<SignupVerificationResponse>,
                response: retrofit2.Response<SignupVerificationResponse>?
            ) {

                if ((response!!.code() == 500)) {
                    UtilsFunctions.showToastError("Internal server error")
                    otpVerifyActivity.onFailer()
                } else {
                    if (response?.body() != null && response?.body()?.StatusCode == "200") {
                        otpVerifyActivity.onResendOtp(response.body())
                    } else {
                        otpVerifyActivity.onFailer()
                        UtilsFunctions.showToastError(response?.body()?.Message)
                    }
                }
            }

            override fun onFailure(call: Call<SignupVerificationResponse>, t: Throwable) {
                otpVerifyActivity.onFailer()
                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))
            }
        })
    }
}


