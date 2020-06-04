package com.seasia.prism.presenter

import com.seasia.prism.api.GetRestAdapter
import com.seasia.prism.model.input.AskQuestionInput
import com.seasia.prism.model.output.AskQuestionResponse
import com.seasia.prism.util.UtilsFunctions
import com.seasia.prism.core.fragment.CorridorFragment
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