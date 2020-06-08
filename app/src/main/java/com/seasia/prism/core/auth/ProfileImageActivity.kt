package com.seasia.prism.core.auth

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.seasia.prism.R
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.databinding.ActivityProfileImageBinding
import java.lang.Exception

class ProfileImageActivity : BaseActivity() {
    var url=""
var binding:ActivityProfileImageBinding?=null
    override fun getLayoutId(): Int {
        return R.layout.activity_profile_image
    }

    override fun initViews() {
        binding=viewDataBinding as ActivityProfileImageBinding
        binding!!.includeView.toolbatTitle.setText("Profile Image")
        binding!!.includeView.ivBack.setOnClickListener { finish() }

        if(intent.getStringExtra("imageUrl")!=null){
            url=intent.getStringExtra("imageUrl")!!
        }
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding!!.image)
    }

    override fun onResume() {
        super.onResume()
        try {
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)

        } catch (e: Exception) {

        }
    }
}