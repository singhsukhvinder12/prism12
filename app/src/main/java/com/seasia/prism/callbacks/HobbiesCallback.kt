package com.seasia.prism.callbacks

import com.seasia.prism.model.output.ChoiceResponse
import com.seasia.prism.model.output.QuestionResponse

interface HobbiesCallback {
    fun onSuccess(body: ChoiceResponse)
    fun onError()
    fun onSuccessQuestion(body: QuestionResponse)
}