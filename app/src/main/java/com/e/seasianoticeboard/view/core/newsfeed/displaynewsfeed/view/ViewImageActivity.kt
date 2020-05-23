package com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.databinding.ActivityViewImageBinding
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.adapter.SlidingImage_Adapter
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.adapter.ViewFullImageAdapter
import com.yanzhenjie.album.mvp.BaseActivity
import java.lang.Exception

class ViewImageActivity : com.e.seasianoticeboard.views.core.BaseActivity() {

    var binding: ActivityViewImageBinding? = null
    var poistion = "0"

    override fun getLayoutId(): Int {
        return R.layout.activity_view_image
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityViewImageBinding
        binding!!.includeView.toolbatTitle.setText("View Images")
        binding!!.includeView.ivBack.setOnClickListener {
            finish()
        }
        if (intent.getStringArrayListExtra("ImageList") != null) {
            binding!!.viewPager.adapter =
                ViewFullImageAdapter(this, intent.getStringArrayListExtra("ImageList"))

            poistion = intent.getStringExtra("position")
            binding!!.viewPager.setCurrentItem(poistion.toInt())
        }
    }


    override fun onResume() {
        super.onResume()
        try {
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)

        } catch (e: Exception) {

        }
    }
}
