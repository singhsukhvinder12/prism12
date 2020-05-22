package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.databinding.ActivityLikeListBinding
import com.e.seasianoticeboard.views.core.BaseActivity
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.adapter.CommentAdapter
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.adapter.LikeListAdapter
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.CommentResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetLikeResponse
import com.e.seasianoticeboard.views.institute.newsfeed.viewmodel.CommentViewModel

class LikeListActivity : BaseActivity(), View.OnClickListener {
    var commentViewModel: CommentViewModel? = null

    var binding: ActivityLikeListBinding?=null
    lateinit var adapter: LikeListAdapter
    var postId=""
    var likeList:ArrayList<GetLikeResponse.ResultDataModel.LstgetLikesListViewModelsList>?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_like_list
    }

    override fun initViews() {
        binding=viewDataBinding as ActivityLikeListBinding
        binding!!.includeView.toolbatTitle.setText("Likes")
        binding!!.includeView.ivBack.setOnClickListener(this)
        if(intent.getStringExtra("postId")!=null){
           postId=intent.getStringExtra("postId")!!
            getLikeList()
       }

        setAdapterData()

    }

    fun getLikeList(){
        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel::class.java)
        showDialog()
        commentViewModel?.getLikeList(postId)
        commentViewModel?.likeData()!!.observe(
                this,
                object : Observer<GetLikeResponse> {
                    override fun onChanged(data: GetLikeResponse) {
                        hideDialog()
                        if (data.ResultData != null) {
                            likeList=data.ResultData!!.lstgetLikesListViewModels!!
                            setAdapterData()
                        }
                    }
                })
    }


    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.iv_back->{
                finish()
            }
        }
    }
    fun  setAdapterData(){
        if(likeList!=null) {
            val mLayoutManager = LinearLayoutManager(this)
            binding!!.rvPublic.layoutManager = mLayoutManager
            adapter = LikeListAdapter(this@LikeListActivity, likeList!!)
            binding!!.rvPublic.adapter = adapter
        }
    }

}