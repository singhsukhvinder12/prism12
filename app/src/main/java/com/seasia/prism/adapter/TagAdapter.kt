package com.seasia.prism.adapter

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
import com.seasia.prism.model.output.TagList
import com.seasia.prism.newsfeed.AddPostActivity

class TagAdapter(
    var context: AddPostActivity,
    var studentList: ArrayList<TagList>
) : RecyclerView.Adapter<TagAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.raw_tag_layout, parent, false)
        return MyViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        try {
            holder.tvName!!.setText(studentList.get(position).userName)

            holder.deleteTag!!.setOnClickListener {
                context.removeTag(position)
            }

            if (!TextUtils.isEmpty(studentList.get(position).userImage)) {
                holder.ivTitle!!.visibility = View.GONE
                holder.linearLayoutDummy!!.visibility = View.GONE
                holder.linearLayout!!.visibility = View.VISIBLE
                Glide.with(context)
                    .load(studentList.get(position).userImage)
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(holder.linearLayout!!)
            } else {
                holder.linearLayoutDummy!!.visibility = View.VISIBLE
                holder.linearLayout!!.visibility = View.GONE
                holder.ivTitle!!.bringToFront()
                holder.ivTitle!!.visibility = View.VISIBLE
                val first = studentList.get(position).userName.toString().substring(0, 1)
                holder.ivTitle!!.text = first
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

    fun setData(data: ArrayList<TagList>) {
        this.studentList = data
        notifyDataSetChanged()
    }

    inner class MyViewHolder(
        itemView: View,
        var context: AddPostActivity
    ) : RecyclerView.ViewHolder(itemView) {

        var ivTitle: TextView? = null
        var parentLayout: RelativeLayout? = null
        var linearLayout: ImageView? = null
        var linearLayoutDummy: ImageView? = null
        var deleteTag: ImageView? = null
        var tvName: TextView? = null

        init {
            ivTitle = itemView.findViewById(R.id.iv_title)
            parentLayout = itemView.findViewById(R.id.parentLayout)
            linearLayout = itemView.findViewById(R.id.tv_page_title)
            linearLayoutDummy = itemView.findViewById(R.id.tv_page)
            tvName = itemView.findViewById(R.id.tvName)
            deleteTag = itemView.findViewById(R.id.deleteTag)
        }
    }
}