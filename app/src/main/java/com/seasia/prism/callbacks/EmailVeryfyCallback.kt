package com.seasia.prism.callbacks

import com.seasia.prism.model.SignupVerificationResponse

interface EmailVeryfyCallback {
    fun onSuccess(body: SignupVerificationResponse)
    fun onError()
}