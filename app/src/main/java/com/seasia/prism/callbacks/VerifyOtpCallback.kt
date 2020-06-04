package com.seasia.prism.callbacks

import com.seasia.prism.model.SignupVerificationResponse
import com.seasia.prism.model.VerifyEmailResponse

interface VerifyOtpCallback {
    fun onOTPSucess(body: VerifyEmailResponse)
    fun onResendOtp(body: SignupVerificationResponse)
    fun onFailer()
}