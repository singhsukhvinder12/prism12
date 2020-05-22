package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed

import android.app.ProgressDialog
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.widget.MediaController
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.databinding.ActivityPlayVideoBinding
import com.e.seasianoticeboard.views.core.BaseActivity

class PlayVideoActivity : BaseActivity() {

    var binding: ActivityPlayVideoBinding? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_play_video
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityPlayVideoBinding
        binding!!.includeView.toolbatTitle.setText("Video Playing")
        binding!!.includeView.ivBack.setOnClickListener {
            binding!!.videoView.stopPlayback()
            finish()
        }

        var videoPath = intent.getStringExtra("videoPath")
        playVideo(videoPath)
    }

    fun playVideo(videoPath: String?) {
        val myUri = Uri.parse(videoPath)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding!!.videoView)
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
}
