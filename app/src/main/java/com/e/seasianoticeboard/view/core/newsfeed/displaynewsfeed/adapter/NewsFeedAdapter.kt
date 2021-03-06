package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.adapter

import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.databinding.ItemNewsFeedBinding
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.view.core.newsfeed.audioplayer.AndroidBuildingMusicPlayerActivity
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.adapter.SlidingImage_Adapter
import com.e.seasianoticeboard.views.core.BaseActivity
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.PlayVideoActivity
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.listener.OnNewsFeedItemClickListener
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.LikeInput
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.pagination.NewsFeedFragment
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.view.CommentActivity
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.view.LikeListActivity
import kotlinx.android.synthetic.main.item_news_feed.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewsFeedAdapter(
        var context: NewsFeedFragment,
        var mList: ArrayList<GetFeedResponse.ResultDataList>,
        private var listener: OnNewsFeedItemClickListener,
        var baseActivity: BaseActivity?
) :
        RecyclerView.Adapter<NewsFeedAdapter.ViewHolder>() {
    //private val isLoadingAdded = false
    private val mCurrentPosition = 0
    var progressDialog: ProgressDialog? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_news_feed,
                parent,
                false
        ) as ItemNewsFeedBinding

        return ViewHolder(binding.root)
    }

    //to add comment
    fun notifyParticularItemWithComment(
            complaintId: String,
            data: ArrayList<GetFeedResponse.ResultDataList>,
            commentsCounts: Int
    ) {
    }

    //for delete
    fun removeAt(position: Int) {
        this.mList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.mList.size)
    }

    //for like
    fun notifyParticularItem(complaintId: String, data: ArrayList<GetFeedResponse.ResultDataList>) {
    }

    fun addDataInMyCases(
            mLayoutManager: LinearLayoutManager,
            listItems: ArrayList<GetFeedResponse.ResultDataList>
    ) {
        var size = this.mList.size
        this.mList.addAll(listItems)
        //this.mList.addAll(mLayoutManager.getItemCount(),listItems)
        var sizeNew = this.mList.size
        notifyDataSetChanged()
        // notifyItemRangeChanged(size, sizeNew)
    }

    fun clear() {
        val size: Int = mList.size
//        mList.clear()
//        notifyItemRangeRemoved(0, size)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, mList, position, listener, baseActivity)

    }

    override fun getItemCount(): Int {
        //   return 10
        return mList.size
    }


    fun setList(mList: ArrayList<GetFeedResponse.ResultDataList>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    fun String.intOrString(): Any {
        val v = toIntOrNull()
        return when (v) {
            null -> this
            else -> v
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var index: Int? = null
        var mediaPlayer: MediaPlayer? = null
        var status = ""
        fun bind(
                context: NewsFeedFragment,
                item: ArrayList<GetFeedResponse.ResultDataList>,
                index: Int,
                listener: OnNewsFeedItemClickListener,
                baseActivity: BaseActivity?
        ) {
            this.index = index


            itemView.txtUserNameForPost.setText(item.get(index).PostedBy)
            itemView.txtPostInfo.setText(item.get(index).Description)
//            itemView.txtDateForPost.setText(baseActivity!!.getLocalDate("yyyy-MM-dd'T'HH:mm:ss", item.get(index).PostedDate, "yyyy-MM-dd"))
            itemView.txtDateForPost.setText(item.get(index).strCreatedDate)
            if (!item.get(index).TotalComments.equals("0")) {
                itemView.txtPostCommentNo.setText(item.get(index).TotalComments)
            }


            Glide.with(context).load(item.get(position).PostedByImageUrl)
                .placeholder(R.drawable.user).error(R.drawable.user)
                .into(itemView.img)

//            if (!item.get(index).TotalLikes.equals("0")) {
//                itemView.txtPostLikeNo.setText(item.get(index).TotalLikes)
//            }

            itemView.txtPostLikeNo.setText(item.get(index).TotalLikes)

           // status = item.get(index).IsLikedByMe!!

            if (item.get(index).IsLikedByMe.equals("true")) {
                itemView.img_like_post.setImageResource(R.drawable.ic_like_heart)
            } else {
                itemView.img_like_post.setImageResource(R.drawable.ic_unlike_heart)
            }


            var popup1 = PopupMenu(context.activity, itemView.iv_menu)
            popup1.inflate(R.menu.news_feed_menu)


            if(item.get(index).PostedById.equals(context.sharedPref!!.getString(PreferenceKeys.USER_ID,"").toString())){
                popup1.menu.getItem(0).setVisible(true)
                popup1.menu.getItem(1).setVisible(false)
            } else{
                popup1.menu.getItem(0).setVisible(false)
                popup1.menu.getItem(1).setVisible(true)
            }

                if (item.get(index).lstDocuments != null) {
                    if (item.get(index).lstDocuments!!.get(0).Type != null) {
                        var isTYpe = item.get(index).lstDocuments!!.get(0).Type
                        val ImagesArray = ArrayList<String>()
                        if (isTYpe.equals("Image")) {
                            itemView.viewPagerParent.visibility = View.VISIBLE
                            itemView.audioView.visibility = View.GONE
                            itemView.parentVideoView.visibility = View.GONE

                            if (item.get(index).lstDocuments != null) {

                                for (i in 0..item.get(index)!!.lstDocuments!!.size - 1) {
                                    var url = item.get(index).lstDocuments!!.get(i).URL
                                    ImagesArray.add(url!!);
                                }
                                var mPager = itemView.findViewById(R.id.viewPager) as ViewPager
                                mPager.adapter = SlidingImage_Adapter(context.activity, ImagesArray)
                            }

                        } else if (isTYpe.equals("Audio")) {
                            var url = item.get(index).lstDocuments!!.get(0).URL
                            itemView.audioView.visibility = View.VISIBLE
                            itemView.viewPagerParent.visibility = View.GONE
                            itemView.parentVideoView.visibility = View.GONE

                            var playAUdio = false


                            itemView.btnPlayAudio.setOnClickListener {

                                var intent = Intent(context.activity, AndroidBuildingMusicPlayerActivity::class.java);
                                intent.putExtra("audio", url)
                                context.startActivity(intent)
                            }
                        } else if (isTYpe.equals("Video")) {
                            itemView.viewPagerParent.visibility = View.GONE
                            itemView.audioView.visibility = View.GONE
                            itemView.parentVideoView.visibility = View.VISIBLE

                            val url = item.get(index).lstDocuments!!.get(0).URL
                            itemView.btnPlayVideo.setOnClickListener {
                                var intent = Intent(context.activity, PlayVideoActivity::class.java)
                                intent.putExtra("videoPath", url)
                                context.startActivity(intent)
                            }
//                            Glide
//                                .with(context)
//                                .asBitmap()
//                                .load(url)
//                                .into(itemView.btnPlayVideo);
                        }
                    }
                }

            itemView.iv_menu!!.setOnClickListener {
                popup1.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item1: MenuItem): Boolean {

                        when (item1.getItemId()) {
                            R.id.delete -> {
                                context.deletePost(item.get(index).NewsLetterId.toString(),index)
                            }
                            R.id.report -> {
                                context.reportPost(item.get(index).NewsLetterId.toString())
                                //baseActivity.showMessage(baseActivity, "Coming soon")
                            }
                        }
                        return false
                    }
                })
                popup1.show()
            }

            itemView.layoutCommentPost.setOnClickListener {
                    //    baseActivity!!.showMessage(context,"Coming soon")
                    var intent = Intent(context.activity, CommentActivity::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable("commentData", item.get(index).lstComments)
                    intent.putExtras(bundle)
                    intent.putExtra("postId", item.get(index).NewsLetterId.toString())
                    intent.putExtra("position",""+index)
                    intent.putExtra("PostedById", item.get(index).PostedById.toString())
                    context.startActivityForResult(intent,588)
                }
                itemView.img_like_post.setOnClickListener {
                    if (item.get(index).IsLikedByMe.equals("true")) {
                        itemView.img_like_post.setImageResource(R.drawable.ic_unlike_heart)
                        var input = LikeInput()
                      //  status="false"
                        item.get(index).IsLikedByMe="false"
                        input.IsLiked = "false"
                        input.LikedBy = baseActivity!!.sharedPref!!.getString(PreferenceKeys.USER_ID, "")
                        input.PostId = item.get(index).NewsLetterId.toString()
                        input.TotalLikes = item.get(index).TotalLikes.toString()
                        context.likeHitApi(input, itemView.txtPostLikeNo, index,item)
                    } else  if (item.get(index).IsLikedByMe.equals("false")) {
                        itemView.img_like_post.setImageResource(R.drawable.ic_like_heart)
                        var input = LikeInput()
                     //   status="true"
                        item.get(index).IsLikedByMe="true"
                        input.IsLiked = "true"
                        input.LikedBy = baseActivity!!.sharedPref!!.getString(PreferenceKeys.USER_ID, "")
                        input.PostId = item.get(index).NewsLetterId.toString()
                        input.TotalLikes = item.get(index).TotalLikes.toString()
                        context.likeHitApi(input, itemView.txtPostLikeNo, index, item)
                    }
                }

                itemView.txtPostLikeNo.setOnClickListener {
                    var intent = Intent(context.activity, LikeListActivity::class.java)

                    intent.putExtra("postId",item.get(index).NewsLetterId.toString())
                    context.startActivity(intent)
                }
                itemView.share.setOnClickListener {
                if(item.get(index).lstDocuments!=null && item.get(index).lstDocuments!!.get(0).Type != null){
                    context.sharePost(item.get(index).lstDocuments!!.get(0).URL)
                } else{
                    context.sharePost(item.get(index).Description)
                }

                }
            }
        }

    fun getLocalDate(format: String, milisec: String?, outputFormat: String?): String {
        val inputFormat = SimpleDateFormat(format, Locale.getDefault())
        val outputFormat1 = SimpleDateFormat(outputFormat, Locale.getDefault())

        //val tz = TimeZone.getTimeZone("Local")
        // inputFormat.timeZone = tz
        val date = inputFormat.parse(milisec)

        val tzLocal = TimeZone.getDefault()
        outputFormat1.timeZone = tzLocal
        return outputFormat1.format(date)
    }

    }
