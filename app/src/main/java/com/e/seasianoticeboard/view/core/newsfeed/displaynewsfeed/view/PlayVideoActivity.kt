package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.databinding.ActivityPlayVideoBinding
import com.e.seasianoticeboard.views.core.BaseActivity
import java.security.AccessController.getContext


class PlayVideoActivity : BaseActivity() {
    var mediaController:MediaController?=null
    var binding: ActivityPlayVideoBinding? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_play_video
    }

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        binding = viewDataBinding as ActivityPlayVideoBinding
        binding!!.includeView.toolbatTitle.setText(getString(R.string.video_playing))
      //  baseActivity!!.hideDialog()
        binding!!.includeView.ivBack.setOnClickListener {
            binding!!.videoView.stopPlayback()
            finish()
        }

        val videoPath = intent.getStringExtra("videoPath")

        var ountDownTimer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                playVideo(videoPath)
            }
        }.start()


    }

    fun playVideo(videoPath: String?) {
        val myUri = Uri.parse(videoPath)

         mediaController = MediaController(this)
     //   mediaController.setAnchorView(binding!!.videoView)
        binding!!.videoView.setMediaController(mediaController)

        binding!!.videoView.setVideoURI(myUri)
        binding!!.videoView.requestFocus()

        // itemView.btnPlayVideo.visibility= View.GONE
        val progressDialog = ProgressDialog.show(this, "", "Loading...", true);

        progressDialog.dismiss()
        binding!!.progress.visibility = View.VISIBLE
        binding!!.videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            progressDialog.dismiss()
            binding!!.videoView.start()
            binding!!.progress.visibility = View.GONE
        })

    }

//    override fun onBackPressed() {
////        super.onBackPressed()
////        binding!!.videoView.pause()
////        binding!!.videoView.stopPlayback()
////        finish()
////    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
//
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            onBackPressed()
//        }
//        return super.onKeyDown(keyCode, event)
//    }
}
