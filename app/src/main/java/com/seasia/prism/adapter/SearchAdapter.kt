package com.seasia.prism.adapter

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seasia.prism.R
import com.seasia.prism.core.auth.ProfileActivity
import com.seasia.prism.core.ui.SearchUserActivity
import com.seasia.prism.model.output.SearchResponse
import com.seasia.prism.model.output.TagList

class SearchAdapter(
    var context: SearchUserActivity,
    var studentList: ArrayList<SearchResponse.ResultDataList>,
    var comingFrom: String,
    var tagList: ArrayList<TagList>?
) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {
    var check=false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_list_adapter, parent, false)
        return MyViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        try {

//            var fiestName=studentList.get(position).FirstName
//            var lastName=studentList.get(position).LastName
            holder.tvName!!.setText(studentList.get(position).UserName)



            if (!TextUtils.isEmpty(studentList.get(position).ImageUrl)) {
                holder.ivTitle!!.visibility = View.GONE
                holder.linearLayoutDummy!!.visibility = View.GONE
                holder.linearLayout!!.visibility = View.VISIBLE
                Glide.with(context)
                    .load(studentList.get(position).ImageUrl)
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(holder.linearLayout!!)
            } else {
                holder.linearLayoutDummy!!.visibility = View.VISIBLE
                holder.linearLayout!!.visibility = View.GONE
                holder.ivTitle!!.bringToFront()
                holder.ivTitle!!.visibility = View.VISIBLE
                val first = studentList.get(position).FirstName.toString().substring(0, 1)
                holder.ivTitle!!.text = first
            }


            if(comingFrom.equals("userTag")) {
                holder.tagUserCheck!!.visibility=View.VISIBLE
                holder.tagUserParent!!.visibility=View.VISIBLE

                if(studentList.get(position).isSelected.equals("true")){
                    holder.tagUserCheck!!.setImageResource(R.drawable.ic_check_selected)
                } else{
                    holder.tagUserCheck!!.setImageResource(R.drawable.ic_check_unselected)
                }

                holder.tagUserParent!!.setOnClickListener {
                    if(tagList!!.size<5){
                        if(studentList.get(position).isSelected.equals("true")){
                            studentList.get(position).isSelected="false"

                            for (i in 0..tagList!!.size-1){

                                if (studentList.get(position).UserId.equals(tagList!!.get(i).tagId)){
                                    tagList!!.removeAt(i)
                                    break
                                }
                            }
                        } else{
                            studentList.get(position).isSelected="true"
                            val tag=TagList()
                            tag.tagId=studentList.get(position).UserId
                            tag.userName=studentList.get(position).UserName
                            tag.userImage=studentList.get(position).ImageUrl
                            tagList!!.add(tag)
                        }
                        notifyDataSetChanged()
                        context.updatedTagList(tagList!!)
                    } else{


                        if(studentList.get(position).isSelected.equals("true")){
                            studentList.get(position).isSelected="false"
                            for (i in 0..tagList!!.size-1){

                                if (studentList.get(position).UserId.equals(tagList!!.get(i).tagId)){
                                    tagList!!.removeAt(i)
                                    break
                                }
                            }
                            notifyDataSetChanged()
                            context.updatedTagList(tagList!!)
                        } else{
                            Toast.makeText(context,"You can tag only 5 users",Toast.LENGTH_LONG).show()
                        }
                    }

                }
            }
            holder.parentLayout!!.setOnClickListener {
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtra("comingFrom", "editProfile")
                    intent.putExtra("postedByMail", studentList.get(position).Email)
                    intent.putExtra("anotherUser", studentList.get(position).UserId)
                    intent.putExtra("profileImage", studentList.get(position).ImageUrl)
                    intent.putExtra("userName", studentList.get(position).UserName)
                    intent.putExtra("status", studentList.get(position).Bio)
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
        var tagUserParent: RelativeLayout? = null
        var linearLayout: ImageView? = null
        var linearLayoutDummy: ImageView? = null
        var tvName: TextView? = null
        var tagUserCheck:ImageView?=null

        init {
            ivTitle = itemView.findViewById(R.id.iv_title)
            tagUserParent = itemView.findViewById(R.id.tagUserParent)
            parentLayout = itemView.findViewById(R.id.parentLayout)
            linearLayout = itemView.findViewById(R.id.tv_page_title)
            linearLayoutDummy = itemView.findViewById(R.id.tv_page)
            tvName = itemView.findViewById(R.id.tvName)
            tagUserCheck = itemView.findViewById(R.id.tagUserCheck)
        }
    }
}