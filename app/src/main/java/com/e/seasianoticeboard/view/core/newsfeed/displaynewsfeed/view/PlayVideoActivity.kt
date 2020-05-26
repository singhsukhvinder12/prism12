package com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed
import android.app.Activity
import android.app.ProgressDialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import android.widget.MediaController
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.databinding.ActivityPlayVideoBinding
import com.e.seasianoticeboard.views.core.BaseActivity


class PlayVideoActivity : BaseActivity() {
    val mediaController:MediaController?=null
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

        val mediaController = MediaController(this)
     //   mediaController.setAnchorView(binding!!.videoView)
        mediaController.hide();
        binding!!.videoView.setMediaController(mediaController)


//        binding!!.videoView.setMediaController(object : MediaController(this) {
//            override fun dispatchKeyEvent(event: KeyEvent): Boolean {
//                if (event.keyCode == KeyEvent.KEYCODE_BACK)
//                binding!!.videoView.stopPlayback()
//                finish()
//                return super.dispatchKeyEvent(event)
//            }
//        })

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

    override fun onBackPressed() {
      //  super.onBackPressed()

        binding!!.videoView.stopPlayback()
        finish()
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed()
        }
        return super.onKeyDown(keyCode, event)
    }

}
