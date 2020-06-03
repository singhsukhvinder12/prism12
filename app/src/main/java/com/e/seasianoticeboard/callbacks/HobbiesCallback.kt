package com.e.seasianoticeboard.callbacks

import com.e.seasianoticeboard.model.output.ChoiceResponse
import com.e.seasianoticeboard.model.output.QuestionResponse

interface HobbiesCallback {
    fun onSuccess(body: ChoiceResponse)
    fun onError()
    fun onSuccessQuestion(body: QuestionResponse)
}