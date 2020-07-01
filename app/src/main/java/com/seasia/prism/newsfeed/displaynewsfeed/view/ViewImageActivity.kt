package com.seasia.prism.newsfeed.displaynewsfeed.view

import com.seasia.prism.R
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.newsfeed.displaynewsfeed.adapter.ViewFullImageAdapter
import com.seasia.prism.databinding.ActivityViewImageBinding

import java.lang.Exception

class ViewImageActivity : BaseActivity() {

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
