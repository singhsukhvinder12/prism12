package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetCommentResponse
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.view.CommentActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommentAdapter(
    var context: CommentActivity,
    var studentList: ArrayList<GetCommentResponse.ResultDataModel.LstgetCommentViewModelList>
) : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_commnet_layout, parent, false)
        return MyViewHolder(view, context, studentList)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvName!!.setText(studentList.get(position).CommentedBy)
        holder.tvComment!!.setText(studentList.get(position).Comment)

//        if (!studentList.get(position).dummyDate) {
//            holder.tvDate!!.setText(getLocalDate("yyyy-MM-dd'T'HH:mm:ss", studentList.get(position).CreatedDate, "yyyy-MM-dd")
//            )
//        } else {
//            holder.tvDate!!.setText(studentList.get(position).CreatedDate.toString())
//        }
        holder.tvDate!!.setText(studentList.get(position).strCreatedDate.toString())



        if (!TextUtils.isEmpty(studentList.get(position).ImageUrl)) {
            holder.ivTitle!!.visibility = View.GONE
            Glide.with(context).load(studentList.get(position).ImageUrl)
                .placeholder(R.drawable.user).error(R.drawable.user)
                .into(holder.linearLayout!!)
        } else {
            holder.ivTitle!!.bringToFront()
            holder.ivTitle!!.visibility = View.VISIBLE
            val first = studentList.get(position).CommentedBy.toString().substring(0, 1)
            holder.ivTitle!!.text = first
        }

        if (context.sharedPref!!.getString(PreferenceKeys.USER_ID, "").equals(
                studentList.get(
                    position
                ).CommentedById
            )
        ) {
            holder.btnDeleteComment!!.visibility = View.VISIBLE
        }

        holder.btnDeleteComment!!.setOnClickListener {
            context.deleteComment(studentList.get(position).CommentId, position)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun setData(data: ArrayList<GetCommentResponse.ResultDataModel.LstgetCommentViewModelList>) {
        this.studentList = data
        notifyDataSetChanged()
    }

    inner class MyViewHolder(
        itemView: View,
        var context: CommentActivity,
        var classList: ArrayList<GetCommentResponse.ResultDataModel.LstgetCommentViewModelList>
    ) : RecyclerView.ViewHolder(itemView) {

        var ivTitle: TextView? = null
        var parentLayout: RelativeLayout? = null
        var linearLayout: ImageView? = null
        var btnDeleteComment: ImageView? = null
        var tvName: TextView? = null
        var tvComment: TextView? = null
        var tvDate: TextView? = null

        init {
            ivTitle = itemView.findViewById(R.id.iv_title)
            parentLayout = itemView.findViewById(R.id.parentLayout)
            linearLayout = itemView.findViewById(R.id.tv_page_title)
            tvName = itemView.findViewById(R.id.tvName)
            tvComment = itemView.findViewById(R.id.tvComment)
            tvDate = itemView.findViewById(R.id.tvDate)
            btnDeleteComment = itemView.findViewById(R.id.btnDeleteComment)
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


