package com.e.seasianoticeboard.callbacks

import com.e.seasianoticeboard.model.output.LogoutResponse

interface LogoutCallback {
    fun onSuccess(body: LogoutResponse)
    fun onFailer()
}