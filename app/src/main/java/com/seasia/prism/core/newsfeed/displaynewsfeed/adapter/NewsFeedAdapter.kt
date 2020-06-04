package com.seasia.prism.core.newsfeed.displaynewsfeed.adapter

import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.util.CheckRuntimePermissions
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.util.UtilsFunctions
import com.seasia.prism.core.auth.UserProfileActivity
import com.seasia.prism.core.newsfeed.audioplayer.AndroidBuildingMusicPlayerActivity
import com.seasia.prism.core.newsfeed.displaynewsfeed.adapter.SlidingImage_Adapter
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.GetFeedResponse
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.LikeInput
import com.seasia.prism.core.newsfeed.displaynewsfeed.view.CommentActivity
import com.seasia.prism.core.newsfeed.displaynewsfeed.view.LikeListActivity
import com.seasia.prism.core.newsfeed.displaynewsfeed.view.NewsFeedFragment
import com.seasia.prism.core.newsfeed.displaynewsfeed.view.NewsFeedFragment.Companion.videoOpenStatus
import com.seasia.prism.core.newsfeed.displaynewsfeed.view.PlayVideoActivity


class NewsFeedAdapter(
    var context: NewsFeedFragment,
    var mList: ArrayList<GetFeedResponse.ResultDataList>,
    var baseActivity: BaseActivity?
) :
    RecyclerView.Adapter<NewsFeedAdapter.ViewHolder>() {

    //private val isLoadingAdded = false
    private val mCurrentPosition = 0
    var progressDialog: ProgressDialog? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news_feed, parent, false)

//        val binding = DataBindingUtil.inflate(
//            LayoutInflater.from(parent.context),
//            R.layout.item_news_feed,
//            parent,
//            false
//        ) as ItemNewsFeedBinding

        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // holder.bind(context, mList, position, baseActivity)

        try {


            holder.txtUserNameForPost!!.setText(mList.get(position).PostedBy)
            holder.txtPostInfo!!.setText(mList.get(position).Description)
            holder.txtDateForPost!!.setText(mList.get(position).strCreatedDate)


            holder.txtPostLikeNo!!.setText(mList.get(position).TotalLikes)
            holder.txtPostCommentNo!!.setText(mList.get(position).TotalComments)

            Glide.with(context).load(mList.get(position).PostedByImageUrl)
                .placeholder(R.drawable.user).error(R.drawable.user)
                .into(holder.img!!)
            if (mList.get(position).IsLikedByMe.equals("true")) {
                holder.img_like_post!!.setImageResource(R.drawable.ic_like_heart)
            } else {
                holder.img_like_post!!.setImageResource(R.drawable.ic_unlike_heart)
            }


            var popup1 = PopupMenu(context.activity, holder.iv_menu)
            popup1.inflate(R.menu.news_feed_menu)
            if (mList.get(position).PostedById.equals(
                    context.sharedPref!!.getString(
                        PreferenceKeys.USER_ID,
                        ""
                    ).toString()
                )
            ) {
                popup1.menu.getItem(0).setVisible(true)
                popup1.menu.getItem(1).setVisible(false)
            } else {
                popup1.menu.getItem(0).setVisible(false)
                popup1.menu.getItem(1).setVisible(true)
            }

            if (mList.get(position).lstDocuments != null) {
                if (mList.get(position).lstDocuments!!.get(0).Type != null) {
                    var isTYpe = mList.get(position).lstDocuments!!.get(0).Type
                    val ImagesArray = ArrayList<String>()
                    if (isTYpe.equals("Image")) {
                        holder.viewPagerParent!!.visibility = View.VISIBLE
                        holder.audioView!!.visibility = View.GONE
                        holder.parentVideoView!!.visibility = View.GONE
                        if (mList.get(position).lstDocuments != null) {

                            for (i in 0..mList.get(position)!!.lstDocuments!!.size - 1) {
                                var url = mList.get(position).lstDocuments!!.get(i).URL
                                ImagesArray.add(url!!);
                            }
                            // var mPager = holder.findViewById(R.id.viewPager) as ViewPager
                            holder.viewPager!!.adapter =
                                SlidingImage_Adapter(context.activity, ImagesArray)
                        }

                    } else if (isTYpe.equals("Audio")) {
                        var url = mList.get(position).lstDocuments!!.get(0).URL
                        holder.audioView!!.visibility = View.VISIBLE
                        holder.viewPagerParent!!.visibility = View.GONE
                        holder.parentVideoView!!.visibility = View.GONE
                        var playAUdio = false


                        holder.btnPlayAudio!!.setOnClickListener {

                            if (CheckRuntimePermissions.checkMashMallowPermissions(
                                    baseActivity,
                                    context.PERMISSION_READ_STORAGE, context.REQUEST_PERMISSIONS
                                )
                            ) {
                                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                                } else {

                                    if (videoOpenStatus == 0) {

                                        var intent = Intent(
                                            context.activity,
                                            AndroidBuildingMusicPlayerActivity::class.java
                                        );
                                        intent.putExtra("audio", url)
                                        context.startActivity(intent)
                                        videoOpenStatus = 1
                                    }

                                }
                            }
                        }
                    } else if (isTYpe.equals("Video")) {
                        holder.viewPagerParent!!.visibility = View.GONE
                        holder.audioView!!.visibility = View.GONE
                        holder.parentVideoView!!.visibility = View.VISIBLE

                        val url = mList.get(position).lstDocuments!!.get(0).URL
                        var thumbNail=""


                        holder.videoView1!!.setOnClickListener {
                            if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                                UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                            } else {

                                if (videoOpenStatus == 0) {
                                    videoOpenStatus = 1
                                    var intent =
                                        Intent(context.activity, PlayVideoActivity::class.java)
                                    intent.putExtra("videoPath", url)
                                    intent.putExtra("thumbNail", thumbNail)
                                    context.startActivity(intent)
                                }
                                //baseActivity!!.showDialog()
                            }
                        }

                        try {
                            if(mList.get(position).lstDocuments!!.size>1) {
                                if (mList.get(position).lstDocuments!!.get(1).URL != null) {
                                    thumbNail = mList.get(position).lstDocuments!!.get(1).URL!!
                                }
                                Glide.with(context)
                                    .asBitmap()
                                    .load(thumbNail)
                                    .timeout(60000)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.video_thumbnail)
                                    .error(R.drawable.video_thumbnail)
                                    .into(holder.videoView1!!);
                            } else{
                                Glide.with(context)
                                    .asBitmap()
                                    .load(url)
                                    .timeout(60000)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.video_thumbnail)
                                    .error(R.drawable.video_thumbnail)
                                    .into(holder!!.videoView1!!);
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else{
                        holder.viewPagerParent!!.visibility = View.GONE
                        holder.audioView!!.visibility = View.GONE
                        holder.parentVideoView!!.visibility = View.GONE
                    }
                } else{
                    holder.viewPagerParent!!.visibility = View.GONE
                    holder.audioView!!.visibility = View.GONE
                    holder.parentVideoView!!.visibility = View.GONE
                }
            } else{
                holder.viewPagerParent!!.visibility = View.GONE
                holder.audioView!!.visibility = View.GONE
                holder.parentVideoView!!.visibility = View.GONE
            }
            holder.img!!.setOnClickListener {
                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                } else {
                    if (mList.get(position).PostedById != null) {
                        if (videoOpenStatus == 0) {
                            var intent =
                                Intent(context.activity, UserProfileActivity::class.java)
                            intent.putExtra("comingFrom", "editProfile")
                            intent.putExtra("postedByMail", mList.get(position).Email)
                            intent.putExtra("anotherUser", mList.get(position).PostedById)
                            context.startActivityForResult(intent, 205)
                            videoOpenStatus = 1
                        }
                    } else {
                        Toast.makeText(context.activity, "Somthing went wrong", Toast.LENGTH_LONG)
                            .show()
                    }
                }

            }

            holder.iv_menu!!.setOnClickListener {
                popup1.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item1: MenuItem): Boolean {

                        when (item1.getItemId()) {
                            R.id.delete -> {

                                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                                } else {
                                    context.deletePost(
                                        mList.get(position).NewsLetterId.toString(),
                                        position
                                    )
                                }


                            }
                            R.id.report -> {
                                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                                } else {
                                    context.reportPost(mList.get(position).NewsLetterId.toString())
                                }
                            }
                        }
                        return false
                    }
                })
                popup1.show()
            }

            holder.layoutCommentPost!!.setOnClickListener {

                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                } else {
                    if (videoOpenStatus == 0) {
                        var intent = Intent(context.activity, CommentActivity::class.java)
                        val bundle = Bundle()
                        bundle.putSerializable("commentData", mList.get(position).lstComments)
                        intent.putExtras(bundle)
                        intent.putExtra("postId", mList.get(position).NewsLetterId.toString())
                        intent.putExtra("position", "" + position)
                        intent.putExtra("PostedById", mList.get(position).PostedById.toString())
                        context.startActivityForResult(intent, 588)
                        videoOpenStatus = 1
                    }
                }

            }
            holder.img_like_post!!.setOnClickListener {

                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                } else {
                    if (mList.get(position).IsLikedByMe.equals("true")) {
                        holder.img_like_post!!.setImageResource(R.drawable.ic_unlike_heart)
                        var input = LikeInput()
                        //  status="false"
                        mList.get(position).IsLikedByMe = "false"
                        input.IsLiked = "false"
                        input.LikedBy =
                            baseActivity!!.sharedPref!!.getString(PreferenceKeys.USER_ID, "")
                        input.PostId = mList.get(position).NewsLetterId.toString()
                        input.TotalLikes = mList.get(position).TotalLikes.toString()
                        context.likeHitApi(input, holder!!.txtPostLikeNo!!, position, mList)
                    } else if (mList.get(position).IsLikedByMe.equals("false")) {
                        holder.img_like_post!!.setImageResource(R.drawable.ic_like_heart)
                        var input = LikeInput()
                        //   status="true"
                        mList.get(position).IsLikedByMe = "true"
                        input.IsLiked = "true"
                        input.LikedBy =
                            baseActivity!!.sharedPref!!.getString(PreferenceKeys.USER_ID, "")
                        input.PostId = mList.get(position).NewsLetterId.toString()
                        input.TotalLikes = mList.get(position).TotalLikes.toString()
                        context.likeHitApi(input, holder.txtPostLikeNo!!, position, mList)
                    }
                }

            }

            holder.txtPostLikeNo!!.setOnClickListener {

                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                } else {
                    if (videoOpenStatus == 0) {
                        var intent = Intent(context.activity, LikeListActivity::class.java)
                        intent.putExtra("postId", mList.get(position).NewsLetterId.toString())
                        context.startActivity(intent)
                        videoOpenStatus = 1
                    }
                }
            }
            holder.share!!.setOnClickListener {
                if (mList.get(position).lstDocuments != null && mList.get(position).lstDocuments!!.get(0).Type != null) {
                    var isTYpe = mList.get(position).lstDocuments!!.get(0).Type
                    if(isTYpe.equals("Image")){
                        var ImagesArray=ArrayList<String>()
                        for (i in 0..mList.get(position)!!.lstDocuments!!.size - 1) {
                            var url = mList.get(position).lstDocuments!!.get(i).URL
                            ImagesArray.add(url!!);
                            ///image=image.app
                        }
                        val builder = StringBuilder()
                        for (s in ImagesArray) {
                            builder.append(s+"\n")
                        }
                        val url = builder.toString()
                        context.sharePost(url)
                    }else{
                        context.sharePost(mList.get(position).lstDocuments!!.get(0).URL)
                    }

                } else {
                    context.sharePost(mList.get(position).Description)
                }

            }
        } catch (e: Exception) {
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }


    fun setList(list1: ArrayList<GetFeedResponse.ResultDataList>) {
        this.mList = list1
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var index: Int? = null
        var mediaPlayer: MediaPlayer? = null
        var status = ""
        var txtUserNameForPost: TextView? = null
        var txtPostInfo: TextView? = null
        var txtDateForPost: TextView? = null
        var txtPostCommentNo: TextView? = null
        var txtPostLikeNo: TextView? = null
        var img: ImageView? = null
        var img_like_post: ImageView? = null
        var videoView1: ImageView? = null
        var btnPlayAudio: ImageView? = null
        var share: ImageView? = null
        var iv_menu: ImageView? = null
        var viewPagerParent: RelativeLayout? = null
        var audioView: RelativeLayout? = null
        var parentVideoView: RelativeLayout? = null
        var btnPlayVideo: ImageView? = null
        var layoutCommentPost: LinearLayout? = null
        var viewPager: ViewPager? = null

        init {
            txtUserNameForPost = itemView.findViewById(R.id.txtUserNameForPost)
            txtPostInfo = itemView.findViewById(R.id.txtPostInfo)
            txtDateForPost = itemView.findViewById(R.id.txtDateForPost)
            txtPostCommentNo = itemView.findViewById(R.id.txtPostCommentNo)
            txtPostLikeNo = itemView.findViewById(R.id.txtPostLikeNo)
            img = itemView.findViewById(R.id.img)
            img_like_post = itemView.findViewById(R.id.img_like_post)
            videoView1 = itemView.findViewById(R.id.videoView1)
            btnPlayAudio = itemView.findViewById(R.id.audioPlay)
            share = itemView.findViewById(R.id.share)
            iv_menu = itemView.findViewById(R.id.iv_menu)
            viewPagerParent = itemView.findViewById(R.id.viewPagerParent)
            audioView = itemView.findViewById(R.id.audioView)
            parentVideoView = itemView.findViewById(R.id.parentVideoView)
            btnPlayVideo = itemView.findViewById(R.id.btnPlayVideo)
            layoutCommentPost = itemView.findViewById(R.id.layoutCommentPost)
            viewPager = itemView.findViewById(R.id.viewPager)
        }
    }

}
