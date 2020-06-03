package com.e.seasianoticeboard.callbacks

import com.e.seasianoticeboard.model.output.AddChoiceResponse

interface ChoiceCallback {
    fun onSuccess(body: AddChoiceResponse)
    fun onError()
}