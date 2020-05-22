package com.e.seasianoticeboard.callbacks

import com.e.seasianoticeboard.model.SignupVerificationResponse
import com.e.seasianoticeboard.model.VerifyEmailResponse

interface VerifyOtpCallback {
    fun onOTPSucess(body: VerifyEmailResponse)
    fun onResendOtp(body: SignupVerificationResponse)
    fun onFailer()
}