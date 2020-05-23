package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.databinding.ActivityCommentBinding
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.listener.CommentCallback
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.repo.CommentPresenter
import com.e.seasianoticeboard.views.core.BaseActivity
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.adapter.CommentAdapter
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.*
import com.e.seasianoticeboard.views.institute.newsfeed.viewmodel.CommentViewModel
import kotlin.collections.ArrayList


class CommentActivity : BaseActivity(), View.OnClickListener, CommentCallback {
    var commentViewModel: CommentViewModel? = null
    var commentInput: CommentInput? = null
    var binding: ActivityCommentBinding?=null
    lateinit var adapter: CommentAdapter
    var commentList:ArrayList<GetCommentResponse.ResultDataModel.LstgetCommentViewModelList>?=null
    var postedById=""
    var userId=""
    var postId=""
    var commentCount=""
    var deleteCommentPosition=0
    var commentPresenter:CommentPresenter?=null
    var position="";
    var commentModel:GetCommentResponse.ResultDataModel.LstgetCommentViewModelList?=null
    override fun getLayoutId(): Int {
        return R.layout.activity_comment
    }

    override fun initViews() {
        binding=viewDataBinding as ActivityCommentBinding
        binding!!.btnSend.setOnClickListener(this)
        binding!!.includeView.toolbatTitle.setText("Comment")
        binding!!.includeView.ivBack.setOnClickListener(this)
        userId=sharedPref!!.getString(PreferenceKeys.USER_ID,"")!!

        commentPresenter=CommentPresenter(this)
        val bundle: Bundle = intent.getExtras()!!
         //commentList = bundle.getSerializable("commentData") as ArrayList<GetFeedResponse.ResultDataList.LstComments>?
        if(intent.getStringExtra("postId")!=null){
            postedById=intent.getStringExtra("PostedById")!!
            postId=intent.getStringExtra("postId")!!
            position=intent.getStringExtra("position")!!
          //  commentGet(postId)
            showDialog()
            commentPresenter!!.getComment(postId!!)

        }
        setAdapterData()
    }

    fun commentSend() {
        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel::class.java)
        commentViewModel?.commentOnPost(getImputData())
        showDialog()
        commentViewModel?.commentSendData()!!.observe(
                this,
                object : Observer<CommentResponse> {
                    override fun onChanged(data: CommentResponse) {
                        commentViewModel!!.commentSendData().removeObserver(this)
                        hideDialog()
                        if (data != null) {
                       //     showMessage(this@CommentActivity,data.Message.toString())
                            commentCount=data.ResultData!!
                         //   commentGet(postId)
                            commentPresenter!!.getComment(postId!!)
                        }
                    }
                })
    }


    override fun onBackPressed() {
        //super.onBackPressed()
        var intent=Intent()
        intent.putExtra("position",position)
        intent.putExtra("updatedCount",commentCount.toString())
        setResult(Activity.RESULT_OK,intent)
        finish()
    }
    fun commentGet(postId: String) {
        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel::class.java)
        commentViewModel?.getCommentList(postId)
        showDialog()
        commentViewModel?.getCommentData()!!.observe(
                this,
                object : Observer<GetCommentResponse> {
                    override fun onChanged(data: GetCommentResponse) {
                        hideDialog()
                        commentViewModel!!.getCommentData().removeObserver(this)
                        if (data.ResultData != null) {
                           // showMessage(this@CommentActivity,data.Message.toString())
                            commentList=data.ResultData!!.lstgetCommentViewModel
                            setAdapterData()

                        }
                    }
                })
    }

    fun getImputData(): CommentInput {
        commentInput= CommentInput()
        commentInput!!.CommentId=""
        commentInput!!.PostId=postId
        commentInput!!.CommentedBy=userId
        commentInput!!.Comment=binding!!.etComment.text.toString()
        commentInput!!.AllFiles=""
        return commentInput!!
    }
    fun  setAdapterData(){
        if(commentList!=null) {
            val mLayoutManager = LinearLayoutManager(this)
            mLayoutManager.setStackFromEnd(true);
            binding!!.rvPublic.layoutManager = mLayoutManager
            adapter = CommentAdapter(this@CommentActivity, commentList!!)
            binding!!.rvPublic.adapter = adapter
        }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnSend->{
                if(!binding!!.etComment.text.isEmpty()){
                    //commentSend()

                    showDialog()
                    commentPresenter!!.sendComment(getImputData()!!)
//                    if(commentList==null){
//                        commentList=ArrayList<GetCommentResponse.ResultDataModel.LstgetCommentViewModelList>()
//                        setAdapterData()
//                    }
//                    commentModel=GetCommentResponse.ResultDataModel.LstgetCommentViewModelList()
//                    commentModel!!.Comment=binding!!.etComment.text.toString()
//                    commentModel!!.CommentedBy=sharedPref!!.getString(PreferenceKeys.USERNAME,"")
//                    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
//                    commentModel!!.CreatedDate=date
//                    commentModel!!.dummyDate=true
//                    commentList!!.add(commentModel!!)
                   // adapter.setData(commentList!!)
                    binding!!.etComment.setText("")


                }
            }
            R.id.iv_back->{
               finish()
            }
        }
    }


    fun deleteComment(commentId: String?, position: Int) {
        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel::class.java)
        var input=DeleteCommentInput()
        input.CommentId=commentId
        input.PostId=postId
        input.UserId=userId
        showDialog()
        deleteCommentPosition=position
        commentPresenter=CommentPresenter(this)
        commentPresenter!!.deleteComment(input!!)
//        commentViewModel?.deleteCommentInput(input)
//        showDialog()
//        commentViewModel?.deleteCommentResponse()!!.observe(
//                this,
//                object : Observer<DeleteCommentResponse> {
//                    override fun onChanged(data: DeleteCommentResponse) {
//                        hideDialog()
//                        if(data!=null){
//                            commentGet(postId)
//                        }
//                    }
//                })
    }

    override fun getCommentList(commentListData:GetCommentResponse) {
        hideDialog()
        if (commentListData.ResultData != null) {
            commentList=commentListData.ResultData!!.lstgetCommentViewModel!!
            setAdapterData()
        }
    }

    override fun deleteComment(deleteComment: DeleteCommentResponse) {
        hideDialog()
        commentList!!.removeAt(deleteCommentPosition)
        adapter.notifyDataSetChanged()
     //   commentPresenter!!.getComment(postId)
    }

    override fun sendComment(sendComment: CommentResponse) {
        hideDialog()
        if (sendComment.ResultData != null) {
            commentCount = sendComment.ResultData!!
            //  commentGet(postId)
            commentPresenter!!.getComment(postId)
        }
    }

    override fun onError() {
        hideDialog()
    }
}


