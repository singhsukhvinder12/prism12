package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.view

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.databinding.ActivityLikeListBinding
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.callback.LikeListCallback
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.presenter.LikeListPresenter
import com.e.seasianoticeboard.views.core.BaseActivity
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.adapter.LikeListAdapter
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetLikeResponse

class LikeListActivity : BaseActivity(), View.OnClickListener, LikeListCallback {
    var likeListPresenter: LikeListPresenter?=null

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

        likeListPresenter=
            LikeListPresenter(
                this
            )
        if(intent.getStringExtra("postId")!=null){
           postId=intent.getStringExtra("postId")!!
            getLikeList()
       }

        setAdapterData()

    }

    fun getLikeList(){
        showDialog()
        likeListPresenter!!.getLikeList(postId)
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

    override fun onSuccess(data: GetLikeResponse) {
        hideDialog()
        if (data.ResultData != null) {
            likeList=data.ResultData!!.lstgetLikesListViewModels!!
            setAdapterData()
        }    }

    override fun onError() {
        hideDialog()
    }
}