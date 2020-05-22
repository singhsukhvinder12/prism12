package com.e.seasianoticeboard.view.core.newsfeed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.seasianoticeboard.App
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.repo.NewsFeedRepository
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.CommentInput
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.CommentResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetCommentResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.repo.ComentReposotory

class NewsFeedViewModel:ViewModel() {
    var feedData=MutableLiveData<ArrayList<GetFeedResponse.ResultDataList>>()

    var feedRepository:NewsFeedRepository?=null

    init {
        feedRepository = NewsFeedRepository()
    }

    fun newsFeedInput(inputRequest: Int) {
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        feedData = feedRepository?.fetchComplaints(inputRequest)!!
    }


    fun newsFeedResponse(): LiveData<ArrayList<GetFeedResponse.ResultDataList>> {
//        if(feedData.value==null){
//            feedData=MutableLiveData()
//            return feedData
//        }
        return feedData
    }
}