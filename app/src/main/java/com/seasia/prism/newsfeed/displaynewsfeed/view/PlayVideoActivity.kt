package com.seasia.prism.newsfeed.displaynewsfeed.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.MediaController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.seasia.prism.R
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.databinding.ActivityPlayVideoBinding
import com.seasia.prism.util.DownloadTask
import com.vaibhavlakhera.circularprogressview.CircularProgressView
import java.io.File


class PlayVideoActivity : BaseActivity() {
    var mediaController: MediaController? = null
    var thumbNail = ""
    var documentId = ""
    var dialog1: Dialog? = null
    var percent = 0
    var task: DownloadTask? = null
    var mPath = ""

    companion object {
        var binding: ActivityPlayVideoBinding? = null
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_play_video
    }

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        binding = viewDataBinding as ActivityPlayVideoBinding
        binding!!.includeView.toolbatTitle.setText(getString(R.string.video_playing))
        binding!!.includeView.ivBack.setOnClickListener {
            binding!!.videoView.stopPlayback()
            finish()
        }
        val videoPath = intent.getStringExtra("videoPath")
        mPath = videoPath
        documentId = intent.getStringExtra("documentId")
        documentId = "VID_" + documentId
        if (intent.getStringExtra("thumbNail") != null) {
            thumbNail = intent.getStringExtra("thumbNail")
        }

        try {
            val fileName =
                File(Environment.getExternalStorageDirectory(), "SeasiaPrism/" + documentId)
            if (fileName.exists()) {
                playVideo(fileName.absolutePath)
            } else {
                DownloadTask(this, videoPath, documentId, this)
            }
        } catch (e: Exception) {

        }
        if (!thumbNail.isEmpty()) {
            Glide.with(this)
                .asBitmap()
                .load(thumbNail)
                .timeout(60000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.video_bg)
                .error(R.drawable.video_bg)
                .into(binding!!.imageView);
            binding!!.imageView.visibility = View.VISIBLE
        }
    }

    fun setLoader(circleProgress: CircularProgressView, percentage: Int,dialog:Dialog) {
        try {

                runOnUiThread {
                    if(percentage!=0){
                        circleProgress.setProgress(percentage, true);
                    }
                }
            dialog1=dialog

        } catch (e: Exception) {
        }
    }

    fun play() {
        var fileName = File(Environment.getExternalStorageDirectory(), "SeasiaPrism/" + documentId)
        playVideo(fileName.absolutePath)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (DownloadTask.task != null) {
            if (!DownloadTask.task.isCancelled) {
                DownloadTask.task.cancel(true)
            }
        }
    }


    fun playVideo(videoPath: String?) {
        try {
            val myUri = Uri.parse(videoPath)
            getWindow().setFormat(PixelFormat.TRANSLUCENT);
            mediaController = MediaController(this)
            //   mediaController.setAnchorView(binding!!.videoView)
            mediaController!!.setAnchorView(binding!!.videoView);
            mediaController!!.setMediaPlayer(binding!!.videoView);
            binding!!.videoView.setMediaController(mediaController)
            binding!!.videoView.setVideoURI(myUri)
            binding!!.videoView.requestFocus()
            binding!!.videoView.start()

            binding!!.videoView.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                override fun onPrepared(mp: MediaPlayer?) {
                    mp!!.start()
                    binding!!.imageView.visibility = View.GONE
                }
            })
            var firstPlay = true
            binding!!.videoView.setOnErrorListener(object : MediaPlayer.OnErrorListener {
                override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                    if (firstPlay) {
                        firstPlay = false
                        var fileName = File(
                            Environment.getExternalStorageDirectory(),
                            "SeasiaPrism/" + documentId
                        )
                        fileName.delete()
                        DownloadTask(
                            this@PlayVideoActivity,
                            mPath,
                            documentId,
                            this@PlayVideoActivity
                        )
                    }
                    return true
                }
            })

        } catch (e: Exception) {
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
