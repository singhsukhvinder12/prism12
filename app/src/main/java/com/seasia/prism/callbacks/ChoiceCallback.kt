package com.seasia.prism.callbacks

import com.seasia.prism.model.output.AddChoiceResponse

interface ChoiceCallback {
    fun onSuccess(body: AddChoiceResponse)
    fun onError()
}