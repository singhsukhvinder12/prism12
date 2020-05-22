package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.pagination

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.databinding.FragmentNewsFeedBinding
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.CasesRequest
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.ReportPostInput
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.RepostPostResponse
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.pagination.EndlessRecyclerViewScrollListenerImplementation
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.presenter.LikeInterface
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.presenter.LikePresenter
import com.e.seasianoticeboard.view.core.newsfeed.viewmodel.NewsFeedViewModel
import com.ngo.ui.home.fragments.cases.presenter.NewsFeedImplClass
import com.e.seasianoticeboard.views.core.BaseFragment
import com.e.seasianoticeboard.views.institute.newsfeed.AddPostActivity
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.adapter.NewsFeedAdapter
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.listener.OnNewsFeedItemClickListener
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.*
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.presenter.NewsFeedPresenter
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.view.NewsFeedView
import com.e.seasianoticeboard.views.institute.newsfeed.viewmodel.CommentViewModel
import kotlinx.android.synthetic.main.fragment_news_feed.*

class NewsFeedFragment : BaseFragment(true), NewsFeedView, View.OnClickListener, LikeInterface,
    OnNewsFeedItemClickListener, /*AlertDialogListener,*/
    EndlessRecyclerViewScrollListenerImplementation.OnScrollPageChangeListener {
    override fun showDescError() {

    }

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
    var postiton: Int? = null
    var type = ""
    var horizontalLayoutManager: LinearLayoutManager? = null
    //pagination
    var page: String = "0"
    var perPage: String = "0"
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

    var feedViewModel: NewsFeedViewModel? = null

    companion object {
        var change = 0
        var commentChange = 0
        var fromIncidentDetailScreen = 0
        var commentsCount = 0
    }


    private var complaints = ArrayList<GetFeedResponse.ResultDataList>()
    private var adapter: NewsFeedAdapter? = null
    private var presenter: NewsFeedPresenter = NewsFeedImplClass(this)
    private var statusId = "-1"
    private var complaintId = "-1"
    private var currentStatus = ""
    var complaintIdTobeLiked: String? = null
    var actionChanged: Boolean = false


    override fun initViews(view: View) {
        UserId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")!!
        setAdapter()
        setupUI()

    }

    fun getFeedData() {
        feedViewModel = ViewModelProviders.of(this).get(NewsFeedViewModel::class.java)
        feedViewModel!!.newsFeedInput(UserId.toInt())
        baseActivity!!.showDialog()

        feedViewModel?.newsFeedResponse()!!.observe(
            viewLifecycleOwner,
            object : Observer<ArrayList<GetFeedResponse.ResultDataList>> {
                override fun onChanged(data: ArrayList<GetFeedResponse.ResultDataList>) {
                    if (data != null) {
                        complaints = data
                        setAdapter()
                    }
                    baseActivity!!.hideDialog()

                }
            })

    }


    override fun getQuery(p0: String?) {
    }

    fun setAdapter() {
        adapter = NewsFeedAdapter(this@NewsFeedFragment, complaints, this, baseActivity)
        horizontalLayoutManager = LinearLayoutManager(mContext)
        // rvPublic.setNestedScrollingEnabled(false)
        rvPublic?.layoutManager = horizontalLayoutManager
        rvPublic?.adapter = adapter
    }

    fun setupUI() {
        setProfilePic()

        imgAdd.setOnClickListener(this)

        // doApiCall()
        getFeedData()

        txtAddPost.setOnClickListener {
            //show the addPost layout
            layoutAddPost.visibility = View.GONE
            layoutPost.visibility = View.VISIBLE
        }

        btnPost.setOnClickListener {
            if (edtPostInfo.text.toString().trim().equals("")) {
//                Utilities.showMessage(activity!!, "Please enter post title")
                Toast.makeText(activity, "Please enter post title", Toast.LENGTH_LONG).show()
            } /*else if (path == null || path.equals("")) {
                Utilities.showMessage(activity!!, "Please select image or video")
            }*/ else {
                //  Utilities.showProgress(mContext)
                val pathArray = arrayOf(path)
                //hit api to add post and display post layout
                // val request = CreatePostRequest(edtPostInfo.text.toString().trim(), pathArray, media_type!!)
                // authorizationToken = PreferenceHandler.readString(mContext, PreferenceHandler.AUTHORIZATION, "")
                //   presenter.createPost(request, authorizationToken)
                layoutAddPost.visibility = View.VISIBLE
                layoutPost.visibility = View.GONE
            }
        }

        btnCancel.setOnClickListener {
            //show the addPost layout
            imgPost.visibility = View.GONE
            path = ""
            layoutAddPost.visibility = View.VISIBLE
            layoutPost.visibility = View.GONE
            edtPostInfo.setText("")
            imgPost.setImageResource(0)
            try {
                Glide.with(this)
                    .load("")
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.camera_placeholder)
                    )
                    .into(imgPost)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        img_attach.setOnClickListener {

        }

        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                activity!!,
                R.color.color_primary_blue
            )
        )
        itemsswipetorefresh.setColorSchemeColors(Color.WHITE)

        itemsswipetorefresh.setOnRefreshListener {
            pageCount = 1
            adapter?.clear()
            endlessScrollListener?.resetState()
            //   doApiCall()
            getFeedData()

            // itemsCells.clear()
            // setItemsData()
            // adapter = Items_RVAdapter(itemsCells)
            //  itemsrv.adapter = adapter
            itemsswipetorefresh.isRefreshing = false
        }
    }

    private fun galleryIntent() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*" //"image/* video/*"
        startActivityForResult(pickIntent, IMAGE_REQ_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        complaints.get(0).TotalComments="50"
//        adapter!!.notifyDataSetChanged()
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
                    //compression
                    /*val compClass = CompressImageUtilities()
                    val newPathString = compClass.compressImage(mContext, picturePath!!)
                    path = newPathString*/
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
                        getFeedData()
                    }
                }


            } catch (e: Exception) {
            }
        } else if (requestCode == 588 && resultCode == Activity.RESULT_OK && null != data) {

            try {
                if (data != null) {
                    if (!data.getStringExtra("updatedCount").isEmpty()) {
                        var position=data.getStringExtra("position")
                        var count=data.getStringExtra("updatedCount")
                        complaints.get(position.toInt()).TotalComments =count
                        adapter!!.notifyDataSetChanged()
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

    fun setProfilePic() {

    }

    override fun showGetComplaintsResponse(response: ArrayList<GetFeedResponse.ResultDataList>) {
        // Utilities.dismissProgress() //       val jsondata = GsonBuilder().create().fromJson(response, GetCasesResponse::class.java)
        complaints = response
        setAdapter()
        if (complaints.isNotEmpty()) {
            if (tvRecord != null) {
                tvRecord.visibility = View.GONE
                rvPublic.visibility = View.VISIBLE

                if (!isLike) {
                    if (commentChange == 0 && !whenDeleteCall) {
                        if (pageCount == 1) {
                            adapter?.clear()
                            adapter?.setList(response) //now
                        } else {
                            progressBar.visibility = View.GONE
                            adapter?.addDataInMyCases(
                                horizontalLayoutManager!!,
                                complaints
                            )
                            //adapter?.setList(response.data.toMutableList())
                        }
                    } else {
                        if (whenDeleteCall) {
                            adapter?.removeAt(deleteItemposition!!)
                            whenDeleteCall = false
                        } else {
                            if (pageCount == 1) {
                                adapter?.clear()
                                adapter?.setList(response) //now
                            } else {
                                adapter?.notifyParticularItemWithComment(
                                    commentChange.toString(),
                                    response, commentsCount
                                )
                            }
                            commentsCount = 0
                            commentChange = 0
                        }
                    }
                } else {
                    //when to change like status
                    adapter?.notifyParticularItem(complaintIdTobeLiked!!, response!!)
                    isLike = false
                }
                //change = 1
            }
        } else {
            if (pageCount == 1) {
                tvRecord.visibility = View.VISIBLE
                rvPublic.visibility = View.GONE
            } else {
                if (complaints.size == 0) {
                    tvRecord.visibility = View.VISIBLE
                    rvPublic.visibility = View.GONE
                }
            }
            progressBar.visibility = View.GONE
        }

        setProfilePic()
    }


    override fun onItemClick(
        complaintsData: ArrayList<GetFeedResponse.ResultDataList>,
        type: String,
        position: Int
    ) {

    }

    override fun onDeleteItem(complaintsData: ArrayList<GetFeedResponse.ResultDataList>) {
    }

    override fun changeLikeStatus(complaintsData: ArrayList<GetFeedResponse.ResultDataList>) {
    }


    override fun onStatusClick(statusId: String) {
        this.statusId = statusId
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgAdd -> {
                this.startActivityForResult(
                    Intent(context, AddPostActivity::class.java),
                    HOME_REQUEST_CODE
                )
            }
        }
    }

    override fun onResume() {  //isfirst // !isfirst //
        super.onResume()

//        if (isFirst) {
//            doApiCall()
//            isFirst = false
//        } else if (!isFirst && change == 1) {
//            adapter?.clear()
//            endlessScrollListener?.resetState()
//            doApiCall()
//            change = 0
//        } else {
//            if (fromIncidentDetailScreen == 0) {
//                if (commentChange != 0) {
//                    doApiCall()
//                }
//
//            }
//        }

        fromIncidentDetailScreen == 0
    }

    fun doApiCall() {
        val casesRequest =
            CasesRequest("1", "", "-1", "1", "10")  //type = -1 for fetching both cases and posts
        //  Utilities.showProgress(mContext)
        presenter.getComplaints(casesRequest, token, type, UserId.toInt())

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_news_feed
    }


    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
        pageCount = page
        val casesRequest =
            CasesRequest("1", "", "-1", page.toString(), "10" /*totalItemsCount.toString()*/)
        presenter.getComplaints(casesRequest, token, type, UserId.toInt())
        progressBar.visibility = View.VISIBLE
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
            dialog.hide()
            var commentViewModel: CommentViewModel? = null
            commentViewModel = ViewModelProviders.of(this).get(CommentViewModel::class.java)
            var input = DeletePostInput()
            input.NewsLetterId = postId
            input.UserId = sharedPref!!.getString(PreferenceKeys.USER_ID, "");
            commentViewModel.deletePostData(input)
            commentViewModel?.getDeletedData()!!.observe(
                this,
                object : Observer<DeletePostResponse> {
                    override fun onChanged(data: DeletePostResponse) {
                        if (data != null) {
                            ///txtPostLikeNo!!.setText(data.ResultData)
                            Toast.makeText(
                                activity,
                                "" + data.Message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                            complaints.removeAt(index)
                            adapter!!.notifyDataSetChanged()
                        }
                    }
                })
        }
        btnCancel.setOnClickListener {
            dialog.hide()
        }
        dialog.show()
    }

    fun reportPost(postId: String){
        var commentViewModel: CommentViewModel? = null
        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel::class.java)
        var input = ReportPostInput()
        input.NewsletterId = postId
        input.IssueReportedId = "0"
        input.ReportId = "1"
        input.NewsletterId = postId
        input.UserId = sharedPref!!.getString(PreferenceKeys.USER_ID, "");
        commentViewModel.reportPostInput(input)
        baseActivity!!.showDialog()
        commentViewModel?.reportPostResponse()!!.observe(
            this,
            object : Observer<RepostPostResponse> {
                override fun onChanged(data: RepostPostResponse) {
                    baseActivity!!.hideDialog()
                    commentViewModel?.reportPostResponse().removeObserver(this)
                    if (data != null) {
                        Toast.makeText(activity, data.Message , Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    fun sharePost(url: String?) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Insert Subject here")
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(shareIntent, "Share via"))

    }

    override fun onSuccess(body: LikesResponse) {
        baseActivity!!.hideDialog()
        if (body.ResultData != null) {
            item!!.get(index!!).TotalLikes = body.ResultData
            txtPostLikeNo!!.setText(item!!.get(index!!).TotalLikes)
        }
    }

    override fun onError() {
        baseActivity!!.hideDialog()
    }

}