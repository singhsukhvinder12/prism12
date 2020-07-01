package com.seasia.prism.core.auth

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.core.ui.HobbiesActivity
import com.seasia.prism.databinding.ActivityProfileBinding
import com.seasia.prism.databinding.FragmentNewsFeedBinding
import com.seasia.prism.newsfeed.displaynewsfeed.adapter.UserProfileFeedAdapter
import com.seasia.prism.newsfeed.displaynewsfeed.callback.LikeInterface
import com.seasia.prism.newsfeed.displaynewsfeed.callback.NewsFeedCallback
import com.seasia.prism.newsfeed.displaynewsfeed.model.*
import com.seasia.prism.newsfeed.displaynewsfeed.pagination.EndlessRecyclerViewScrollListenerImplementation
import com.seasia.prism.newsfeed.displaynewsfeed.presenter.LikePresenter
import com.seasia.prism.newsfeed.displaynewsfeed.presenter.NewsFeedPresenter
import com.seasia.prism.newsfeed.displaynewsfeed.presenter.UserProfileFeedPresenter
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.util.UtilsFunctions
import kotlinx.android.synthetic.main.fragment_news_feed.*

@Suppress("UNREACHABLE_CODE")
class ProfileActivity : BaseActivity(), LikeInterface, View.OnClickListener,
    EndlessRecyclerViewScrollListenerImplementation.OnScrollPageChangeListener,
    NewsFeedCallback {
    var binding: ActivityProfileBinding? = null
    private var isResumeRun: Boolean = false

    // var mContext: Context?=null
    private var IMAGE_REQ_CODE = 101
    private var HOME_REQUEST_CODE = 123
    private var path: String = ""
    private var imageUri: Uri? = null
    private var imageUrl: String? = null
    private var authorizationToken: String? = ""
    private var media_type: String? = ""
    private var token: String = ""
    var isFirst = true
    var deleteItemIndex = "0"
    var pageSize: Int = 10
    var postiton: Int? = null
    var setAdapter = 0;
    var type = ""
    var RC_CODE_PICKER_LOGO = 2000
    val PERMISSION_READ_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
    var REQUEST_PERMISSIONS = 1


    var feedPresenter: UserProfileFeedPresenter? = null
    var horizontalLayoutManager: LinearLayoutManager? = null

    //pagination
    var endlessScrollListener: EndlessRecyclerViewScrollListenerImplementation? = null
    var pageCount: Int = 1
    var deleteItemposition: Int? = null
    var isLike: Boolean = false
    var whenDeleteCall: Boolean = false
    var adapterActionPosition: Int? = null
    var index: Int? = null
    var item: ArrayList<GetFeedResponse.ResultDataList>? = null
    var txtPostLikeNo: TextView? = null


    var comingFrom = ""
    var postedByMail = ""
    var anotherUser = ""
    var profileImage = ""
    var userName = ""
    var status = ""


    companion object {
        var clickStatus = 0
        var url = ""
        var change = 0
        var commentChange = 0
        var fromIncidentDetailScreen = 0
        var commentsCount = 0

    }

    var complaints = ArrayList<GetFeedResponse.ResultDataList>()
    var adapter: UserProfileFeedAdapter? = null
    private var statusId = "-1"
    private var complaintId = "-1"
    private var currentStatus = ""
    var complaintIdTobeLiked: String? = null
    var actionChanged: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.activity_profile
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityProfileBinding
//        UserId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")!!
        binding!!.includeView.toolbatTitle.text = "Profile"
        binding!!.includeView.ivBack.setOnClickListener {
            finish()
        }

        comingFrom = intent.getStringExtra("comingFrom")!!
        postedByMail = intent.getStringExtra("postedByMail")!!
        anotherUser = intent.getStringExtra("anotherUser")!!
        profileImage = intent.getStringExtra("profileImage")!!
        userName = intent.getStringExtra("userName")!!

        if( intent.getStringExtra("status")!=null){
            status = intent.getStringExtra("status")!!
        }
        if (sharedPref!!.getString(PreferenceKeys.USER_ID, "").equals(anotherUser)) {
            binding!!.btnEditProfile.setText("Edit Profile")
        } else {
            binding!!.btnEditProfile.setText("View Profile")
        }

        binding!!.btnEditProfile.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra("comingFrom", comingFrom)
            intent.putExtra("postedByMail", postedByMail)
            intent.putExtra("anotherUser", anotherUser)
            intent.putExtra("profileImage", profileImage)
            intent.putExtra("userName", userName)
            startActivityForResult(intent, 205)
        }


        binding!!.userName.setText(userName)
        Glide.with(this)
            .load(profileImage)
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(binding!!.ivProfile)
        binding!!.status.setText(status)





        binding!!.includeView.ivQuestion.visibility = View.VISIBLE
        binding!!.includeView.ivQuestion.setOnClickListener {
            var intent = Intent(this@ProfileActivity, HobbiesActivity::class.java)
            intent.putExtra("userId", anotherUser)
            startActivity(intent)
        }


        setAdapter()
        setupUI()
    }

    fun getFeedData() {
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }

        baseActivity!!.showDialog()
        feedPresenter!!.fetchComplaints(noticeBoardInput(0))
    }

    fun noticeBoardInput(itemCount: Int): GetFeedInput {
        var input = GetFeedInput()
        input.UserId = anotherUser
        input.PageSize = "10"
        input.Search = ""
        input.Skip = itemCount.toString()
        input.SortColumnDir = ""
        input.SortColumn = ""
        input.ParticularId = "0"
        return input
    }

    fun setAdapter() {
        adapter = UserProfileFeedAdapter(this@ProfileActivity, complaints, baseActivity)
        horizontalLayoutManager = LinearLayoutManager(baseActivity)
        rvPublic?.layoutManager = horizontalLayoutManager
        endlessScrollListener = EndlessRecyclerViewScrollListenerImplementation(
            horizontalLayoutManager, this
        )
        endlessScrollListener?.setmLayoutManager(horizontalLayoutManager)
        rvPublic.addOnScrollListener(endlessScrollListener!!)
        rvPublic?.adapter = adapter

    }

    fun setupUI() {
        var feedList = ArrayList<GetFeedResponse.ResultDataList>()
        feedPresenter = UserProfileFeedPresenter(this, feedList)
        getFeedData()

        //  getFeedData()

//        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(
//            ContextCompat.getColor(
//                activity!!,
//                R.color.color_primary_blue
//            )
//        )
//        itemsswipetorefresh.setColorSchemeColors(Color.WHITE)

//        itemsswipetorefresh.setOnRefreshListener {
//            pageCount = 1
//            endlessScrollListener?.resetState()
//            getFeedData()
//            itemsswipetorefresh.isRefreshing = false
//        }
    }

    override fun onResume() {
        super.onResume()
        clickStatus = 0

        if (sharedPref!!.getString(PreferenceKeys.USER_ID, "").equals(anotherUser)) {
            binding!!.userName.setText(sharedPref!!.getString(PreferenceKeys.USERNAME, ""))
            binding!!.status.setText(sharedPref!!.getString(PreferenceKeys.BIO, ""))
            Glide.with(this)
                .load(sharedPref!!.getString(PreferenceKeys.USER_IMAGE, ""))
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(binding!!.ivProfile)
        }

    }

    fun likeHitApi(
        input: LikeInput,
        txtPostLikeNo: TextView,
        index: Int,
        item: ArrayList<GetFeedResponse.ResultDataList>
    ) {
        this.index = index;
        this.item = item;
        this.txtPostLikeNo = txtPostLikeNo;
        baseActivity!!.showDialog()
        var likePresenter = LikePresenter(this)
        likePresenter.getLikes(input)
    }

    fun deletePost(postId: String, index: Int) {
        var dialog = Dialog(baseActivity!!)
        dialog.setContentView(R.layout.custom_delete_item_dialog);
        dialog.setCanceledOnTouchOutside(false)
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent);
        var btnDelete = dialog.findViewById<TextView>(R.id.tv_delete)
        var btnCancel = dialog.findViewById<TextView>(R.id.tv_cancel)
        val title = dialog.findViewById<TextView>(R.id.std_name)
        title.setText(getString(R.string.delete_post))
        btnDelete.setOnClickListener {
            dialog.dismiss()
            baseActivity!!.showDialog()
            var input = DeletePostInput()
            input.NewsLetterId = postId
            deleteItemIndex = index.toString()
            input.UserId = sharedPref!!.getString(PreferenceKeys.USER_ID, "");
            feedPresenter!!.deletaPost(input)
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()
    }


    fun reportPost(postId: String) {
        var input = ReportPostInput()
        input.NewsletterId = postId.toString()
        input.IssueReportedId = "0"
        input.ReportId = "1"
        input.NewsletterId = postId.toString()
        input.UserId = sharedPref!!.getString(PreferenceKeys.USER_ID, "");
        feedPresenter!!.reportPost(input)
        baseActivity!!.showDialog()
    }

    fun sharePost(url: String?) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Insert Subject here")
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(shareIntent, "Share via"))

    }

    override fun onSuccess(resultData: ArrayList<GetFeedResponse.ResultDataList>?) {
        if (resultData != null) {

            complaints = resultData

            if (complaints.size > 0) {
                binding!!.noRecordFound.visibility = View.GONE
                adapter!!.setList(complaints)
                adapter!!.notifyDataSetChanged()

            } else {
                binding!!.noRecordFound.visibility = View.VISIBLE
            }


//             baseActivity!!.runOnUiThread {
//                complaints = resultData
//                 if(setAdapter==1){
//                     setAdapter()
//                     setAdapter=0
//                 } else{
//                     adapter!!.setList(complaints)
//                     adapter!!.notifyDataSetChanged()
//                 }
//            }
        }
        baseActivity!!.hideDialog()
    }

    override fun onLikeSuccess(body: LikesResponse) {
        baseActivity!!.hideDialog()
        if (body.ResultData != null) {
            item!!.get(index!!).TotalLikes = body.ResultData
            txtPostLikeNo!!.setText(item!!.get(index!!).TotalLikes)
        }
    }

    override fun onError() {
        baseActivity!!.hideDialog()
    }

    override fun onDeletePost(data: DeletePostResponse) {
        baseActivity!!.hideDialog()
        if (data != null) {
            Toast.makeText(
                this,
                "" + data.Message.toString(),
                Toast.LENGTH_LONG
            ).show()
            complaints.removeAt(deleteItemIndex.toInt())
            adapter!!.notifyDataSetChanged()
            //  getFeedData()
        }
    }

    override fun onRepostPost(data: RepostPostResponse) {
        baseActivity!!.hideDialog()
        if (data != null) {
            Toast.makeText(this, data.Message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
        baseActivity?.showDialog()
        feedPresenter!!.fetchComplaints(noticeBoardInput(adapter!!.itemCount))
    }

    override fun onClick(p0: View?) {
    }

}