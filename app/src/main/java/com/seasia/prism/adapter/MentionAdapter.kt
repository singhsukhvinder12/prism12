package com.seasia.prism.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seasia.prism.R
import com.seasia.prism.model.output.SearchResponse
import com.seasia.prism.newsfeed.displaynewsfeed.view.CommentActivity

class MentionAdapter(
    var context: CommentActivity,
    var studentList: ArrayList<SearchResponse.ResultDataList>?
) : RecyclerView.Adapter<MentionAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_mention_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tagTextView!!.setText(studentList!!.get(position).UserName)

        holder.tagTextView!!.setOnClickListener {
            context.selectedText(studentList!!.get(position).UserName!!,studentList!!.get(position).UserId)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return studentList!!.size
    }

    inner class MyViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {


        var tagTextView: TextView? = null

        init {

            tagTextView = itemView.findViewById(R.id.tagTextView)
        }
    }
}