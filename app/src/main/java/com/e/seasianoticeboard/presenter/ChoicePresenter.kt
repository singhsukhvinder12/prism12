package com.e.seasianoticeboard.presenter

import com.e.seasianoticeboard.api.GetRestAdapter
import com.e.seasianoticeboard.model.input.AddChoiceInput
import com.e.seasianoticeboard.model.output.AddChoiceResponse
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.view.core.fragment.ChoiceFragment
import retrofit2.Call
import retrofit2.Callback

class ChoicePresenter(var choiceFragment: ChoiceFragment) {

    fun getData(input:AddChoiceInput) {
        val call = GetRestAdapter.getRestAdapter(true).addChoice(input)
        call.enqueue(object : Callback<AddChoiceResponse> {
            override fun onResponse(
                call: Call<AddChoiceResponse>,
                response: retrofit2.Response<AddChoiceResponse>?
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

            override fun onFailure(call: Call<AddChoiceResponse>, t: Throwable) {
                choiceFragment.onError()
                UtilsFunctions.showToastError(t.message)
            }
        })
    }
}