package com.e.seasianoticeboard.presenter

import com.e.seasianoticeboard.api.GetRestAdapter
import com.e.seasianoticeboard.model.output.ChoiceResponse
import com.e.seasianoticeboard.model.output.QuestionResponse
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.view.core.auth.HobbiesActivity
import retrofit2.Call
import retrofit2.Callback

class HobbiesPresenter(var choiceFragment: HobbiesActivity) {

    fun getData(userId:String) {
        val call = GetRestAdapter.getRestAdapter(true).getChoiceQuestion(userId)
        call.enqueue(object : Callback<ChoiceResponse> {
            override fun onResponse(
                call: Call<ChoiceResponse>,
                response: retrofit2.Response<ChoiceResponse>?
            ) {

                if ((response!!.code() == 500)) {
                    UtilsFunctions.showToastError("Internal server error")
                    choiceFragment.onError()
                } else {
                    if (response.body() != null && response.body()?.StatusCode == "200") {
                        choiceFragment.onSuccess(response.body())
                    } else {
                        choiceFragment.onError()
                        UtilsFunctions.showToastError(response.body()?.Message)
                    }
                }
            }

            override fun onFailure(call: Call<ChoiceResponse>, t: Throwable) {
                choiceFragment.onError()
                UtilsFunctions.showToastError(t.message)
            }
        })
    }


    fun getQuestionData(userId: String) {
        val call = GetRestAdapter.getRestAdapter(true).getQuestion(userId)
        call.enqueue(object : Callback<QuestionResponse> {
            override fun onResponse(
                call: Call<QuestionResponse>,
                response: retrofit2.Response<QuestionResponse>?
            ) {
                if ((response!!.code() == 500)) {
                    UtilsFunctions.showToastError("Internal server error")
                    choiceFragment.onError()
                } else {
                    if (response.body() != null && response.body()?.StatusCode == "200") {
                        choiceFragment.onSuccessQuestion(response.body())
                    } else {
                        choiceFragment.onError()
                        UtilsFunctions.showToastError(response.body()?.Message)
                    }
                }
            }

            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                choiceFragment.onError()
                UtilsFunctions.showToastError(t.message)
            }
        })
    }

}