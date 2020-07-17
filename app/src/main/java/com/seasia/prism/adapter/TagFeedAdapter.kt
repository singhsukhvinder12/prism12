package com.seasia.prism.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seasia.prism.R
import com.seasia.prism.core.auth.ProfileActivity
import com.seasia.prism.newsfeed.displaynewsfeed.model.GetFeedResponse
import com.seasia.prism.newsfeed.displaynewsfeed.view.NewsFeedFragment

class TagFeedAdapter(
    var context: NewsFeedFragment,
    var studentList: ArrayList<GetFeedResponse.ResultDataList.lstTaggedUsersList>?
) : RecyclerView.Adapter<TagFeedAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.raw_tag_feed, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {





//        if(position==0){
//            holder.tagTextView!!.setTextColor(Color.parseColor("#000000"))
//            holder.tagTextView!!.setText(studentList!!.get(position).Name)
//        }


//          if(studentList!!.size==position+1){
//            holder.tagTextView!!.setText(studentList!!.get(position).Name)
//        } else{
//            holder.tagTextView!!.setText(studentList!!.get(position).Name+",")
//        }

        holder.tagTextView!!.setText("@"+studentList!!.get(position).Name)


        holder.tagTextView!!.setOnClickListener {


                val intent = Intent(context.activity, ProfileActivity::class.java)
                intent.putExtra("comingFrom", "editProfile")
                intent.putExtra("postedByMail", studentList!!.get(position).Email)
                intent.putExtra("anotherUser", studentList!!.get(position).Id)
                intent.putExtra("profileImage", studentList!!.get(position).ImageUrl)
                intent.putExtra("userName", studentList!!.get(position).Name)
                intent.putExtra("status", studentList!!.get(position).Bio)
                context.startActivityForResult(intent, 205)

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