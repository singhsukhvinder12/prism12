package com.seasia.prism.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.seasia.prism.R
import com.seasia.prism.newsfeed.AddPostActivity

class PostBackgroundColor(
    var context: AddPostActivity,
    var studentList: ArrayList<String>?
) : RecyclerView.Adapter<PostBackgroundColor.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_color_code, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tagTextView!!.getBackground().setColorFilter(Color.parseColor(studentList!!.get(position)), PorterDuff.Mode.SRC_ATOP);

        holder.tagTextView!!.setOnClickListener {
            context.backgroundColor(studentList!!.get(position),position)
        }
        if(position==0){
            holder.tagTextView!!.setBackground(ContextCompat.getDrawable(context, R.drawable.rectagle_gray_shap));
        }
    }


    fun setData(data: ArrayList<String>) {
        this.studentList = data
        notifyDataSetChanged()
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

            tagTextView = itemView.findViewById(R.id.tvColorView)
        }
    }
}