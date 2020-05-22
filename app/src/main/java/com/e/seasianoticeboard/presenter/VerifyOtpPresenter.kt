package com.e.seasianoticeboard.presenter

import com.e.seasianoticeboard.api.GetRestAdapter
import com.e.seasianoticeboard.model.SignupVerificationResponse
import com.e.seasianoticeboard.model.VerifyEmailInput
import com.e.seasianoticeboard.model.VerifyEmailResponse
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.view.core.auth.OtpVerifyActivity
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
                } else {
                    otpVerifyActivity.onOTPSucess(response!!.body())
                }
            }

            override fun onFailure(call: Call<VerifyEmailResponse>, t: Throwable) {
                otpVerifyActivity.onFailer()
                UtilsFunctions.showToastError(t.message)
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
                UtilsFunctions.showToastError(t.message)
            }
        })
    }
}


