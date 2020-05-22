package com.e.seasianoticeboard.callbacks

import com.e.seasianoticeboard.model.SignupVerificationResponse

interface EmailVeryfyCallback {
    fun onSuccess(body: SignupVerificationResponse)
    fun onError()
}