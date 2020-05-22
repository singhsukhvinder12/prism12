package com.e.seasianoticeboard.views.institute.newsfeed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.AddUpdateImageInput
import java.io.File

class AddPostViewModel() : ViewModel() {
    var addPostResository:AddPostRepository?=null
    var addPostData= MutableLiveData<AddPostResponse>()
    init {
        addPostResository=AddPostRepository()
        addPostData= MutableLiveData()
    }

    fun getData(): LiveData<AddPostResponse>{
//        if(addPostData!!.value==null){
//            addPostData=MutableLiveData()
//        }
        return addPostData!!
    }

    fun hitAddPostAPI(userData: AddPostInput, videoFIle: File?, audioFIle: File?, imagesList: ArrayList<AddUpdateImageInput>?){
        addPostData= addPostResository!!.getPostData(userData,videoFIle,audioFIle,imagesList)!!
    }

}