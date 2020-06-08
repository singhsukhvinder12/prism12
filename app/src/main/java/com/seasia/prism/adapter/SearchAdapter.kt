package com.seasia.prism.adapter

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.seasia.prism.R
import com.seasia.prism.core.auth.HobbiesActivity
import com.seasia.prism.core.auth.SearchUserActivity
import com.seasia.prism.core.auth.UserProfileActivity
import com.seasia.prism.core.newsfeed.displaynewsfeed.adapter.LikeListAdapter
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.GetLikeResponse
import com.seasia.prism.core.newsfeed.displaynewsfeed.view.LikeListActivity
import com.seasia.prism.model.output.SearchResponse

class SearchAdapter(
    var context: SearchUserActivity,
    var studentList: ArrayList<SearchResponse.ResultDataList>
) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_list_adapter, parent, false)
        return MyViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        try {

            var fiestName=studentList.get(position).FirstName
            var lastName=studentList.get(position).LastName
            holder.tvName!!.setText(fiestName+" "+lastName)



            if (!TextUtils.isEmpty(studentList.get(position).ImageUrl)) {
                holder.ivTitle!!.visibility = View.GONE
                holder.linearLayoutDummy!!.visibility=View.GONE
                holder.linearLayout!!.visibility=View.VISIBLE
                Glide.with(context)
                    .load(studentList.get(position).ImageUrl)
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(holder.linearLayout!!)
            } else {
                holder.linearLayoutDummy!!.visibility=View.VISIBLE
                holder.linearLayout!!.visibility=View.GONE
                holder.ivTitle!!.bringToFront()
                holder.ivTitle!!.visibility = View.VISIBLE
                val first = studentList.get(position).FirstName.toString().substring(0, 1)
                holder.ivTitle!!.text = first
            }

            holder.parentLayout!!.setOnClickListener {
                var intent = Intent(context, UserProfileActivity::class.java)
                intent.putExtra("comingFrom", "editProfile")
                intent.putExtra("postedByMail", studentList.get(position).Email)
                intent.putExtra("anotherUser", studentList.get(position).UserId)
                context.startActivity(intent)
            }

        } catch (e: Exception) {

        }

    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun setData(data: ArrayList<SearchResponse.ResultDataList>) {
        this.studentList = data
        notifyDataSetChanged()
    }

    inner class MyViewHolder(
        itemView: View,
        var context: SearchUserActivity
    ) : RecyclerView.ViewHolder(itemView) {

        var ivTitle: TextView? = null
        var parentLayout: RelativeLayout? = null
        var linearLayout: ImageView? = null
        var linearLayoutDummy: ImageView? = null
        var tvName: TextView? = null

        init {
            ivTitle = itemView.findViewById(R.id.iv_title)
            parentLayout = itemView.findViewById(R.id.parentLayout)
            linearLayout = itemView.findViewById(R.id.tv_page_title)
            linearLayoutDummy = itemView.findViewById(R.id.tv_page)
            tvName = itemView.findViewById(R.id.tvName)
        }
    }
}