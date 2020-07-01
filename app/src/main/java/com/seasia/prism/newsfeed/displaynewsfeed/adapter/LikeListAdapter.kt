package com.seasia.prism.newsfeed.displaynewsfeed.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seasia.prism.R
import com.seasia.prism.newsfeed.displaynewsfeed.model.GetLikeResponse
import com.seasia.prism.newsfeed.displaynewsfeed.view.LikeListActivity

class LikeListAdapter(var context: LikeListActivity, var studentList: ArrayList<GetLikeResponse.ResultDataModel.LstgetLikesListViewModelsList>) : RecyclerView.Adapter<LikeListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.like_list_adapter, parent, false)
        return MyViewHolder(view, context, studentList)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvName!!.setText(studentList.get(position).LikedBy)


//        holder.ivTitle!!.bringToFront()
//        holder.ivTitle!!.visibility = View.VISIBLE
//        val first = studentList.get(position).LikedBy.toString().substring(0, 1)
//        holder.ivTitle!!.text = first


        if (!TextUtils.isEmpty(studentList.get(position).ImageUrl)) {
            holder.ivTitle!!.visibility = View.GONE
            Glide.with(context).load(studentList.get(position).ImageUrl).placeholder(R.drawable.user).error(R.drawable.user)
                .into(holder.linearLayout!!)
        } else {
            holder.ivTitle!!.bringToFront()
            holder.ivTitle!!.visibility = View.VISIBLE
            val first = studentList.get(position).LikedBy.toString().substring(0, 1)
            holder.ivTitle!!.text = first
        }


    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun setData(data: ArrayList<GetLikeResponse.ResultDataModel.LstgetLikesListViewModelsList>) {
        this.studentList = data
        notifyDataSetChanged()
    }

    inner class MyViewHolder(
            itemView: View,
            var context: LikeListActivity,
            var classList: ArrayList<GetLikeResponse.ResultDataModel.LstgetLikesListViewModelsList>
    ) : RecyclerView.ViewHolder(itemView) {

        var ivTitle: TextView? = null
        var parentLayout: RelativeLayout? = null
        var linearLayout: ImageView? = null
        var tvName: TextView? = null

        init {
            ivTitle = itemView.findViewById(R.id.iv_title)
            parentLayout = itemView.findViewById(R.id.parentLayout)
            linearLayout = itemView.findViewById(R.id.tv_page_title)
            tvName = itemView.findViewById(R.id.tvName)
        }
    }
}
