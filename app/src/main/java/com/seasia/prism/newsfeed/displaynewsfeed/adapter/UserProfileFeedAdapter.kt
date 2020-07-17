package com.seasia.prism.newsfeed.displaynewsfeed.adapter



import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
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
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.adapter.TagFeedAdapter
import com.seasia.prism.adapter.TagProfileFeedAdapter
import com.seasia.prism.util.CheckRuntimePermissions
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.util.UtilsFunctions
import com.seasia.prism.core.auth.UserProfileActivity
import com.seasia.prism.newsfeed.audioplayer.AndroidBuildingMusicPlayerActivity
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.core.auth.ProfileActivity
import com.seasia.prism.core.auth.ProfileActivity.Companion.clickStatus
import com.seasia.prism.core.ui.HobbiesActivity
import com.seasia.prism.newsfeed.displaynewsfeed.model.GetFeedResponse
import com.seasia.prism.newsfeed.displaynewsfeed.model.LikeInput
import com.seasia.prism.newsfeed.displaynewsfeed.view.CommentActivity
import com.seasia.prism.newsfeed.displaynewsfeed.view.LikeListActivity
import com.seasia.prism.newsfeed.displaynewsfeed.view.NewsFeedFragment
import com.seasia.prism.newsfeed.displaynewsfeed.view.PlayVideoActivity


class UserProfileFeedAdapter(
    var context: ProfileActivity,
    var mList: ArrayList<GetFeedResponse.ResultDataList>,
    var baseActivity: BaseActivity?
) :
    RecyclerView.Adapter<UserProfileFeedAdapter.ViewHolder>() {

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


            if(mList.get(position).TypeId!=null){
                if(!mList.get(position).TypeId.equals("0")){
                    holder.parentLayout!!.visibility=View.GONE
                    holder.btnMemories!!.visibility=View.VISIBLE
                    holder.btnMemories!!.setOnClickListener {
                        var intent = Intent(context, HobbiesActivity::class.java)
                        intent.putExtra("userId", mList.get(position).PostedById)
                        intent.putExtra("typeId", mList.get(position).TypeId)
                        context.startActivity(intent)
                    }

                } else{
                    holder.parentLayout!!.visibility=View.VISIBLE
                    holder.btnMemories!!.visibility=View.GONE
                }
            }


            if(mList.get(position).ColorCode!=null && !mList.get(position).ColorCode!!.isEmpty()){

                holder.txtPostInfo!!.visibility=View.GONE
                try {
                    if(mList.get(position).ColorCode!!.contains("#")){
                        if(!mList.get(position).ColorCode.equals("#FFFFFF")){
                            holder.txtPostInfoColor!!.getBackground().setColorFilter(Color.parseColor(mList.get(position).ColorCode), PorterDuff.Mode.SRC_ATOP);
                            holder.txtPostInfoWithoutPadding!!.visibility=View.GONE
                            holder.txtPostInfoColor!!.setText(mList.get(position).Description)
                            holder.txtPostInfoColor!!.visibility=View.VISIBLE

                        } else{
//                            holder.txtPostInfoColor!!.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                            holder.txtPostInfoWithoutPadding!!.setTextColor(Color.parseColor("#000000"))
                            holder.txtPostInfoWithoutPadding!!.setText(mList.get(position).Description)
                            holder.txtPostInfoColor!!.visibility=View.GONE
                            holder.txtPostInfoWithoutPadding!!.visibility=View.VISIBLE
                        }
                    } else{
                        //  holder.txtPostInfoColor!!.visibility=View.GONE
                        //  holder.txtPostInfoColor!!.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                        holder.txtPostInfoWithoutPadding!!.setTextColor(Color.parseColor("#000000"))
                        holder.txtPostInfoWithoutPadding!!.setText(mList.get(position).Description)
                        holder.txtPostInfoWithoutPadding!!.visibility=View.VISIBLE
                        holder.txtPostInfoColor!!.visibility=View.GONE
                    }
                }catch (e:Exception){

                }
            }else{
//                holder.txtPostInfoColor!!.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                holder.txtPostInfoWithoutPadding!!.setTextColor(Color.parseColor("#000000"))
                holder.txtPostInfoWithoutPadding!!.setText(mList.get(position).Description)
                holder.txtPostInfo!!.visibility=View.GONE
                holder.txtPostInfoColor!!.visibility=View.GONE
                holder.txtPostInfoWithoutPadding!!.visibility=View.VISIBLE
            }



            holder.txtUserNameForPost!!.setText(mList.get(position).PostedBy)
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


            var popup1 = PopupMenu(context, holder.iv_menu)
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



            if (mList.get(position).lstTaggedUsers != null) {

//                var arrayList = ArrayList<GetFeedResponse.ResultDataList.lstTaggedUsersList>()
//
//                if(arrayList.size>0){
//                    arrayList.clear()
//                }
//
//
//                var myList = GetFeedResponse.ResultDataList.lstTaggedUsersList()
//                myList.Id = ""
//                myList.Name = "Tagged at: "
//                myList.Email = ""
//                myList.ImageUrl = ""
//                arrayList.add(myList)
//                arrayList.addAll(mList.get(position).lstTaggedUsers!!)

                holder.tagRecyclerView!!.visibility = View.VISIBLE
                val tagFeedAdapter = TagProfileFeedAdapter(context, mList.get(position).lstTaggedUsers)
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.setFlexWrap(FlexWrap.WRAP)
                holder.tagRecyclerView!!.layoutManager = layoutManager
                holder.tagRecyclerView!!.adapter = tagFeedAdapter
            } else {
                holder.tagRecyclerView!!.visibility = View.GONE
            }




            if (mList.get(position).lstDocuments != null) {
                holder.txtPostInfo!!.setText(mList.get(position).Description)
                holder.txtPostInfoColor!!.visibility=View.GONE
                holder.txtPostInfoWithoutPadding!!.visibility=View.GONE
                holder.txtPostInfo!!.visibility=View.VISIBLE

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
                                SlidingImage_Adapter(context, ImagesArray)

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

                                    if (clickStatus == 0) {

                                        var intent = Intent(context, AndroidBuildingMusicPlayerActivity::class.java);
                                        intent.putExtra("audio", url)
                                        context.startActivity(intent)
                                        clickStatus = 1
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
                        var documentId= mList.get(position).lstDocuments!!.get(0).DocumentId

                        holder.videoView1!!.setOnClickListener {

                            if (CheckRuntimePermissions.checkMashMallowPermissions(
                                    baseActivity,
                                    context.PERMISSION_READ_STORAGE, context.REQUEST_PERMISSIONS)) {
                                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                                } else {
                                    if (clickStatus == 0) {
                                        try{
                                            clickStatus = 1
                                            var intent = Intent(context, PlayVideoActivity::class.java)
                                            intent.putExtra("videoPath", url)
                                            intent.putExtra("thumbNail", thumbNail)
                                            intent.putExtra("documentId", documentId)
                                            context.startActivity(intent)
                                        }catch (e:Exception){
                                            e.printStackTrace()
                                        }
                                    }
                                    //baseActivity!!.showDialog()
                                }
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
                                    .placeholder(R.drawable.video_bg)
                                    .error(R.drawable.video_bg)
                                    .into(holder.videoView1!!);
                            } else{
                                Glide.with(context)
                                    .asBitmap()
                                    .load(url)
                                    .timeout(60000)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.video_bg)
                                    .error(R.drawable.video_bg)
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
//            holder.img!!.setOnClickListener {
//                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
//                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
//                } else {
//                    if (mList.get(position).PostedById != null) {
//                        if (clickStatus == 0) {
//                            var intent =
//                                Intent(context, UserProfileActivity::class.java)
//                            intent.putExtra("comingFrom", "editProfile")
//                            intent.putExtra("postedByMail", mList.get(position).Email)
//                            intent.putExtra("anotherUser", mList.get(position).PostedById)
//                            context.startActivityForResult(intent, 205)
//                            clickStatus = 1
//                        }
//                    } else {
//                        Toast.makeText(context, "Somthing went wrong", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                }
//
//            }
//
//            holder.txtUserNameForPost!!.setOnClickListener {
//                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
//                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
//                } else {
//                    if (mList.get(position).PostedById != null) {
//                        if (clickStatus == 0) {
//                            var intent =
//                                Intent(context, UserProfileActivity::class.java)
//                            intent.putExtra("comingFrom", "editProfile")
//                            intent.putExtra("postedByMail", mList.get(position).Email)
//                            intent.putExtra("anotherUser", mList.get(position).PostedById)
//                            context.startActivityForResult(intent, 205)
//                            clickStatus = 1
//                        }
//                    } else {
//                        Toast.makeText(context, "Somthing went wrong", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                }
//
//            }

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
                    if (clickStatus == 0) {
                        var intent = Intent(context, CommentActivity::class.java)
                        val bundle = Bundle()
                        bundle.putSerializable("commentData", mList.get(position).lstComments)
                        intent.putExtras(bundle)
                        intent.putExtra("postId", mList.get(position).NewsLetterId.toString())
                        intent.putExtra("position", "" + position)
                        intent.putExtra("PostedById", mList.get(position).PostedById.toString())
                        context.startActivityForResult(intent, 588)
                        clickStatus = 1
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
                    if (clickStatus == 0) {
                        var intent = Intent(context, LikeListActivity::class.java)
                        intent.putExtra("postId", mList.get(position).NewsLetterId.toString())
                        context.startActivity(intent)
                        clickStatus = 1
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
        var txtPostInfoColor: TextView? = null
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
        var tagRecyclerView: RecyclerView? = null
        var txtPostInfoWithoutPadding: TextView? = null
        var parentLayout: LinearLayout? = null
        var btnMemories: ImageView? = null

        init {
            btnMemories = itemView.findViewById(R.id.btnMemories)
            parentLayout = itemView.findViewById(R.id.parentLayout)
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
            tagRecyclerView = itemView.findViewById(R.id.tagRecyclerView)
            txtPostInfoColor = itemView.findViewById(R.id.txtPostInfoColor)
            txtPostInfoWithoutPadding = itemView.findViewById(R.id.txtPostInfoWithoutPadding)

        }
    }

}
