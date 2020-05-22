package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.view

//import com.ngo.base.view.BaseView
//import com.ngo.pojo.response.*
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedResponse

interface NewsFeedView  {
    fun showGetComplaintsResponse(response: ArrayList<GetFeedResponse.ResultDataList>)
    fun showDescError()
  /*  fun onPostAdded(responseObject: CreatePostResponse)
    fun onComplaintDeleted(responseObject: DeleteComplaintResponse)
    fun onLikeStatusChanged(responseObject: DeleteComplaintResponse)
    fun adhaarSavedSuccess(responseObject: SignupResponse)
    fun onListFetchedSuccess(responseObject: GetStatusResponse)
    fun statusUpdationSuccess(responseObject: UpdateStatusSuccess)*/
}