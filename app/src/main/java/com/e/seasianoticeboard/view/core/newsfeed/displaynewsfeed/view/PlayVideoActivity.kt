package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.view.View
import android.widget.MediaController
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.databinding.ActivityPlayVideoBinding
import com.e.seasianoticeboard.views.core.BaseActivity


class PlayVideoActivity : BaseActivity() {
    var mediaController: MediaController? = null
    var binding: ActivityPlayVideoBinding? = null
    var thumbnail=""
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
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        mediaController = MediaController(this)
        //   mediaController.setAnchorView(binding!!.videoView)
        mediaController!!.setAnchorView(binding!!.videoView);
        mediaController!!.setMediaPlayer(binding!!.videoView);
        binding!!.videoView.setMediaController(mediaController)

        binding!!.videoView.setVideoURI(myUri)
        binding!!.videoView.seekTo( 10);
        binding!!.videoView.requestFocus()
        binding!!.progress.visibility = View.VISIBLE
        binding!!.videoView.start()

        binding!!.videoView.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: MediaPlayer?) {

                mp!!.start()
                mp.setOnVideoSizeChangedListener(object : MediaPlayer.OnVideoSizeChangedListener {
                    override fun onVideoSizeChanged(p0: MediaPlayer?, p1: Int, p2: Int) {
                        binding!!.progress.visibility = View.GONE
                    mp.start()
                    }
            })
            }

        })

    }

//    fun playVideo(videoPath: String?) {
//        val myUri = Uri.parse(videoPath)
//
//        mediaController = MediaController(this)
//        //   mediaController.setAnchorView(binding!!.videoView)
//        mediaController!!.setAnchorView(binding!!.videoView);
//        mediaController!!.setMediaPlayer(binding!!.videoView);
//        binding!!.videoView.setMediaController(mediaController)
//
//        binding!!.videoView.setVideoURI(myUri)
//        binding!!.videoView.requestFocus()
//
//        // itemView.btnPlayVideo.visibility= View.GONE
//        val progressDialog = ProgressDialog.show(this, "", "Loading...", true);
//
//        progressDialog.dismiss()
//        binding!!.progress.visibility = View.VISIBLE
//        progressDialog.dismiss()
//        binding!!.videoView.start()
//
//        binding!!.videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener {
//            progressDialog.dismiss()
//            binding!!.videoView.start()
//            binding!!.progress.visibility = View.GONE
//        })
//
//    }
//




    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
