package com.e.seasianoticeboard.callbacks

import com.e.seasianoticeboard.model.output.AskQuestionResponse

interface QuestionCallback {
    fun onSuccess(body: AskQuestionResponse)
    fun onError()
}