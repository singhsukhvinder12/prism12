package com.seasia.prism.callbacks

import com.seasia.prism.model.output.SearchResponse

interface SearchUsersCallback {
    fun onSuccess(body: ArrayList<SearchResponse.ResultDataList>?)
    fun onError()
}