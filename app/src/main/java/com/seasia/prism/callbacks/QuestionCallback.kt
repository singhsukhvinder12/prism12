package com.seasia.prism.callbacks

import com.seasia.prism.model.output.AskQuestionResponse

interface QuestionCallback {
    fun onSuccess(body: AskQuestionResponse)
    fun onError()
}