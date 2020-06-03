package com.e.seasianoticeboard.presenter

import com.e.seasianoticeboard.api.GetRestAdapter
import com.e.seasianoticeboard.model.input.AskQuestionInput
import com.e.seasianoticeboard.model.output.AskQuestionResponse
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.view.core.fragment.CorridorFragment
import retrofit2.Call
import retrofit2.Callback

class QuestionsPresenter(var corridorFragment: CorridorFragment) {
    fun getData(input: AskQuestionInput) {
        val call = GetRestAdapter.getRestAdapter(true).addQuestion(input)
        call.enqueue(object : Callback<AskQuestionResponse> {
            override fun onResponse(
                call: Call<AskQuestionResponse>,
                response: retrofit2.Response<AskQuestionResponse>?
            ) {
                if ((response!!.code() == 500)) {
                    UtilsFunctions.showToastError("Internal server error")
                    corridorFragment.onError()
                } else {
                    if (response.body() != null && response.body()?.StatusCode == "200") {
                        corridorFragment.onSuccess(response.body())
                    } else {
                        corridorFragment.onError()
                        UtilsFunctions.showToastError(response.body()?.Message)
                    }
                }
            }

            override fun onFailure(call: Call<AskQuestionResponse>, t: Throwable) {
                corridorFragment.onError()
                UtilsFunctions.showToastError(t.message)
            }
        })
    }
}