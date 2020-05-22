package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.presenter

//import com.ngo.base.presenter.BasePresenter
//import com.ngo.pojo.request.CasesRequest
//import com.ngo.pojo.request.CreatePostRequest
//import com.ngo.pojo.response.*
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.CasesRequest
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedResponse

interface NewsFeedPresenter{
    fun getComplaints(casesRequest: CasesRequest, token: String, userRole: String, UserId: Int)
    fun onGetCompaintsSuccess(response: ArrayList<GetFeedResponse.ResultDataList>)
}