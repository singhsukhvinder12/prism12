package com.seasia.prism.presenter

import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.api.GetRestAdapter
import com.seasia.prism.core.fragment.ChoiceFragment
import com.seasia.prism.model.input.AddChoiceInput
import com.seasia.prism.model.output.AddChoiceResponse
import com.seasia.prism.util.UtilsFunctions

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
                UtilsFunctions.showToastError(App.app.getString(R.string.somthing_went_wrong))

            }
        })
    }
}