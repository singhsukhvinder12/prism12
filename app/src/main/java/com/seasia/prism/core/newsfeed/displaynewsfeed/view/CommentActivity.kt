package com.seasia.prism.core.newsfeed.displaynewsfeed.view

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.databinding.ActivityCommentBinding
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.util.UtilsFunctions
import com.seasia.prism.core.newsfeed.displaynewsfeed.callback.CommentCallback
import com.seasia.prism.core.newsfeed.displaynewsfeed.presenter.CommentPresenter
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.core.newsfeed.displaynewsfeed.adapter.CommentAdapter
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.*
import kotlin.collections.ArrayList


class CommentActivity : BaseActivity(), View.OnClickListener,
    CommentCallback {
    var commentInput: CommentInput? = null
    var binding: ActivityCommentBinding?=null
    lateinit var adapter: CommentAdapter
    var commentList:ArrayList<GetCommentResponse.ResultDataModel.LstgetCommentViewModelList>?=null
    var postedById=""
    var userId=""
    var postId=""
    var commentCount=""
    var deleteCommentPosition=0
    var commentPresenter: CommentPresenter?=null
    var position="";
    override fun getLayoutId(): Int {
        return R.layout.activity_comment
    }

    override fun initViews() {
        binding=viewDataBinding as ActivityCommentBinding
        binding!!.btnSend.setOnClickListener(this)
        binding!!.includeView.toolbatTitle.setText("Comment")
        binding!!.includeView.ivBack.setOnClickListener(this)
        userId=sharedPref!!.getString(PreferenceKeys.USER_ID,"")!!

        commentPresenter=
            CommentPresenter(
                this
            )
        if(intent.getStringExtra("postId")!=null){
            postedById=intent.getStringExtra("PostedById")!!
            postId=intent.getStringExtra("postId")!!
            position=intent.getStringExtra("position")!!
            showDialog()
            if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                return
            }
            commentPresenter!!.getComment(postId!!)

        }
        setAdapterData()
    }



    override fun onBackPressed() {
        var intent=Intent()
        intent.putExtra("position",position)
        intent.putExtra("updatedCount",commentCount.toString())
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    fun getImputData(): CommentInput {
        commentInput= CommentInput()
        commentInput!!.CommentId=""
        commentInput!!.PostId=postId
        commentInput!!.CommentedBy=userId
        commentInput!!.Comment=binding!!.etComment.text.toString().trim()
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
                if(binding!!.etComment.text.trim().isEmpty()){
                    Toast.makeText(this,"Please enter your comment",Toast.LENGTH_LONG).show()

                }
//
//                else if(binding!!.etComment.text.isBlank()){
//                    Toast.makeText(this,"Please enter your comment",Toast.LENGTH_LONG).show()
//                }
                else{
                    showDialog()
                    if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                        UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                        return
                    }
                    commentPresenter!!.sendComment(getImputData()!!)
                    binding!!.etComment.setText("")
                }
            }
            R.id.iv_back->{
                onBackPressed()
            }
        }
    }


    fun deleteComment(commentId: String?, position: Int,userId:String?) {
        var input= DeleteCommentInput()
        input.CommentId=commentId
        input.PostId=postId
        input.UserId=userId
        showDialog()
        deleteCommentPosition=position
        commentPresenter=
            CommentPresenter(
                this
            )
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        commentPresenter!!.deleteComment(input!!)

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

        if(deleteComment!=null){
            commentCount = deleteComment.ResultData!!
        }
    }

    override fun sendComment(sendComment: CommentResponse) {
        hideDialog()
        if (sendComment.ResultData != null) {
            commentCount = sendComment.ResultData!!
            showDialog()
            commentPresenter!!.getComment(postId)
        }
    }

    override fun onError() {
        hideDialog()
    }
}


