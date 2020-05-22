package com.e.seasianoticeboard.views.institute.newsfeed

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.AddUpdateImageInput

class ImageListAdapter(
        var context: AddPostActivity, var listImages:
        ArrayList<AddUpdateImageInput>?
) : RecyclerView.Adapter<ImageListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
                LayoutInflater.from(parent.context).inflate(R.layout.row_scrolll_image_adapter, parent, false)
        return MyViewHolder(view, listImages, context!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = listImages!![position]
        holder.tvFileName!!.text = data.imageFileName
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.mipmap.ic_launcher)
            requestOptions.error(R.mipmap.ic_launcher)
            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(listImages!![position].imageUrl)
                    .into(holder.imgSchool!!)


        holder.imgSchool!!.setOnClickListener(View.OnClickListener {
            if (!data.fileType.equals("Image")) {
                val uris = Uri.parse(listImages!![position].imageUrl)
                val intents = Intent(Intent.ACTION_VIEW, uris)
                val b = Bundle()
                b.putBoolean("new_window", true)
                intents.putExtras(b)
                context.startActivity(intents)
            }
        })


    }

    override fun getItemCount(): Int {
        return listImages!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    /**
     * This method is used to fetch ID of views from xml
     */
    inner class MyViewHolder(
        itemView: View,
        listImages: ArrayList<AddUpdateImageInput>?,
        context: AddPostActivity
    ) : RecyclerView.ViewHolder(itemView) {
        var imgSchool: ImageView? = null
        var crossIcon: ImageView? = null
        var tvFileName: TextView? = null

        init {
            imgSchool = itemView.findViewById(R.id.img_school)
            crossIcon = itemView.findViewById(R.id.cross_icon)
            tvFileName = itemView.findViewById(R.id.tv_file_name)

//            imgSchool!!.setOnClickListener {
//                if (listImages!![adapterPosition].fileType.equals("File")) {
//                    context.openAttachement(
//                        listImages[adapterPosition].imageUrl,
//                        listImages[adapterPosition].imageFileName
//                    )
//                }
//            }

            crossIcon!!.setOnClickListener {
                context.DeleteImage(adapterPosition)
            }
        }
    }
}