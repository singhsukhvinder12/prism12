package com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.e.seasianoticeboard.R
import java.util.*

class ViewFullImageAdapter(
    private val context: Context,
    private val IMAGES: ArrayList<String>
) :
    PagerAdapter() {
    private val inflater: LayoutInflater
    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return IMAGES.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout =
            inflater.inflate(R.layout.image_full_layout, view, false)!!
        val imageView = imageLayout.findViewById<View>(
            R.id.image
        ) as ImageView
        val mCount = imageLayout.findViewById<TextView>(
            R.id.count
        )
        Glide.with(context).load(IMAGES[position]).placeholder(R.drawable.image_placeholder)
            .error(
                R.drawable.image_placeholder
            ).into(imageView)
        view.addView(imageLayout, 0)
        val count = position + 1
        if (IMAGES.size != 1) {
            mCount.text = "" + count + "/" + IMAGES.size
        }
        return imageLayout
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view == `object`
    }

    override fun restoreState(
        state: Parcelable?,
        loader: ClassLoader?
    ) {
    }

    override fun saveState(): Parcelable? {
        return null
    }

    init {
        inflater = LayoutInflater.from(context)
    }
}