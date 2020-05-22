package com.ngo.ui.home.fragments.cases.presenter

import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.CasesRequest
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.NewsFeedModel
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.presenter.NewsFeedPresenter
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.view.NewsFeedView

class NewsFeedImplClass(private var view: NewsFeedView) : NewsFeedPresenter {

    var model = NewsFeedModel(this)

    override fun getComplaints(casesRequest: CasesRequest, token: String, userRole: String, UserId: Int) {
        model.fetchComplaints(casesRequest,token,userRole,UserId)
    }

    override fun onGetCompaintsSuccess(response: ArrayList<GetFeedResponse.ResultDataList>) {

        view.showGetComplaintsResponse(response)
    }
}
