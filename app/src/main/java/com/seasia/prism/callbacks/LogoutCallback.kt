package com.seasia.prism.callbacks

import com.seasia.prism.model.output.LogoutResponse

interface LogoutCallback {
    fun onSuccess(body: LogoutResponse)
    fun onFailer()
}