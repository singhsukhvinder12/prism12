package com.seasia.prism.newsfeed.displaynewsfeed.view

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.bumptech.glide.Glide
import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.core.BaseFragment
import com.seasia.prism.databinding.FragmentNewsFeedBinding
import com.seasia.prism.model.DeviceTokenInput
import com.seasia.prism.newsfeed.AddPostActivity
import com.seasia.prism.newsfeed.displaynewsfeed.adapter.NewsFeedAdapter
import com.seasia.prism.newsfeed.displaynewsfeed.callback.LikeInterface
import com.seasia.prism.newsfeed.displaynewsfeed.callback.NewsFeedCallback
import com.seasia.prism.newsfeed.displaynewsfeed.model.*
import com.seasia.prism.newsfeed.displaynewsfeed.pagination.EndlessRecyclerViewScrollListenerImplementation
import com.seasia.prism.newsfeed.displaynewsfeed.presenter.LikePresenter
import com.seasia.prism.newsfeed.displaynewsfeed.presenter.NewsFeedPresenter
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.util.UtilsFunctions
import kotlinx.android.synthetic.main.fragment_news_feed.*

class NewsFeedFragment : BaseFragment(true),
    LikeInterface, View.OnClickListener,
    EndlessRecyclerViewScrollListenerImplementation.OnScrollPageChangeListener,
    NewsFeedCallback {


    private var isResumeRun: Boolean = false
    lateinit var binding: FragmentNewsFeedBinding

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

    private var fabOpenAnimation: Animation? = null
    private var fabCloseAnimation: Animation? = null
    var isFabMenuOpen = false

    var feedPresenter: NewsFeedPresenter? = null
    var horizontalLayoutManager: LinearLayoutManager? = null

    //pagination
    var endlessScrollListener: EndlessRecyclerViewScrollListenerImplementation? = null
    var pageCount: Int = 1
    var deleteItemposition: Int? = null
    var isLike: Boolean = false
    var whenDeleteCall: Boolean = false
    var adapterActionPosition: Int? = null
    var UserId = ""
    var index: Int? = null
    var item: ArrayList<GetFeedResponse.ResultDataList>? = null
    var txtPostLikeNo: TextView? = null


    companion object {
        var videoOpenStatus = 0

        var url = ""

        var change = 0
        var commentChange = 0
        var fromIncidentDetailScreen = 0
        var commentsCount = 0


    }


    var complaints = ArrayList<GetFeedResponse.ResultDataList>()
    var adapter: NewsFeedAdapter? = null
    private var statusId = "-1"
    private var complaintId = "-1"
    private var currentStatus = ""
    var complaintIdTobeLiked: String? = null
    var actionChanged: Boolean = false
    var mComtext: Context? = null


    override fun initViews(view: View) {

        UserId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")!!
        setAdapter()
        setupUI()
        //getAnimations()
    }


    private fun getAnimations() {
        fabOpenAnimation = AnimationUtils.loadAnimation(activity, R.anim.fab_open)
        fabCloseAnimation = AnimationUtils.loadAnimation(activity, R.anim.fab_close)
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
        // input.UserId=""
        input.PageSize = "10"
        input.Search = ""
        input.Skip = itemCount.toString()
        input.SortColumnDir = ""
        input.SortColumn = ""
        input.ParticularId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")
        return input
    }


    override fun getQuery(p0: String?) {
    }

    fun setAdapter() {
        adapter = NewsFeedAdapter(this@NewsFeedFragment, complaints, baseActivity)
        horizontalLayoutManager = LinearLayoutManager(baseActivity,LinearLayoutManager.VERTICAL,false)
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
        feedPresenter =
            NewsFeedPresenter(
                this,
                feedList
            )
        sendDeviceToken()
        imgAdd.setOnClickListener(this)
        baseFloatingActionButton.setOnClickListener(this)
        searchUsers.setOnClickListener(this)
        addPost.setOnClickListener(this)
        getFeedData()

        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                activity!!,
                R.color.color_primary_blue
            )
        )
        itemsswipetorefresh.setColorSchemeColors(Color.WHITE)

        itemsswipetorefresh.setOnRefreshListener {
            pageCount = 1
            endlessScrollListener?.resetState()
            getFeedData()
            itemsswipetorefresh.isRefreshing = false
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK && null != data) {
            imgPost.visibility = View.VISIBLE
            media_type = "photos"
            val selectedMedia: Uri = data.getData()!!
            val cr = mContext!!.contentResolver
            val mime = cr.getType(selectedMedia)
            if (mime?.toLowerCase()?.contains("image")!!) {
                val selectedImage = data.data
                val filePathColumn =
                    arrayOf(MediaStore.Images.Media.DATA)
                val cursor = activity!!.contentResolver
                    .query(selectedImage!!, filePathColumn, null, null, null)
                cursor?.moveToFirst()
                val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                val picturePath = cursor?.getString(columnIndex!!)
                cursor?.close()
                try {
                    Glide.with(this).load(picturePath).into(imgPost)
                    path = picturePath!!
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                change = 0
            } else if (mime.toLowerCase().contains("video")) {
                media_type = "videos"


            }
        } else if (requestCode == HOME_REQUEST_CODE && resultCode == Activity.RESULT_OK && null != data) {

            try {
                if (data != null) {
                    if (data.getStringExtra("backPress").equals("1")) {
                        setAdapter = 1
                        getFeedData()
                    }
                }

            } catch (e: Exception) {
            }
        } else if (requestCode == 588 && resultCode == Activity.RESULT_OK && null != data) {

            try {
                if (data != null) {
                    if (!data.getStringExtra("updatedCount").isEmpty()) {
                        var position = data.getStringExtra("position")
                        var count = data.getStringExtra("updatedCount")
                        complaints.get(position.toInt()).TotalComments = count
                        adapter!!.notifyDataSetChanged()
                    }
                }

            } catch (e: Exception) {
            }
        } else if (requestCode == 205) {

            try {
                if (data != null) {
                    if (data.getStringExtra("imageCode").equals("1")) {
                        getFeedData()
                    }
                }
            } catch (e: Exception) {

            }

        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            /* Utilities.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 galleryIntent()
             }*/
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgAdd -> {
                if (videoOpenStatus == 0) {
                    this.startActivityForResult(
                        Intent(context, AddPostActivity::class.java),
                        HOME_REQUEST_CODE
                    )
                    videoOpenStatus = 1
                }
            }

//            R.id.baseFloatingActionButton -> {
//                if (isFabMenuOpen)
//                    openFloating();
//                else
//                    closeFloating();
//            }
//
//            R.id.searchUsers -> {
//                val intent = Intent(activity, SearchUserActivity::class.java)
//                startActivity(intent)
//                if (isFabMenuOpen)
//                    openFloating();
//                else
//                    closeFloating();
//            }
//
//            R.id.addPost -> {
//                if (videoOpenStatus == 0) {
//                    this.startActivityForResult(
//                        Intent(context, AddPostActivity::class.java),
//                        HOME_REQUEST_CODE
//                    )
//                    videoOpenStatus = 1
//
//                    if (isFabMenuOpen)
//                        openFloating();
//                    else
//                        closeFloating();
//                }
//            }
        }
    }

    override fun onResume() {  //isfirst // !isfirst //
        super.onResume()
        videoOpenStatus = 0
        fromIncidentDetailScreen == 0
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_news_feed
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
            adapter!!.setList(complaints)
            adapter!!.notifyDataSetChanged()

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
                activity,
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
            Toast.makeText(activity, data.Message, Toast.LENGTH_LONG).show()
        }
    }


    fun sendDeviceToken() {
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        var input = DeviceTokenInput()
        input.DeviceToken = UtilsFunctions.TOKEN
        input.UserId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")
        input.DeviceType = "1"
        feedPresenter!!.sendDeviceToken(input)
        Log.e("device_token123", UtilsFunctions.TOKEN!!);
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
        baseActivity?.showDialog()
        feedPresenter!!.fetchComplaints(noticeBoardInput(adapter!!.itemCount))
    }



}