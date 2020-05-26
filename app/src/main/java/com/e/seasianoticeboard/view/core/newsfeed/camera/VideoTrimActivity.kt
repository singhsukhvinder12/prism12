package com.e.seasianoticeboard.camera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.databinding.ActivityVideoTrimBinding
import com.e.seasianoticeboard.camera.trim.FontsConstants
import com.e.seasianoticeboard.camera.trim.FontsHelper
import com.e.seasianoticeboard.camera.trim.VideoProgressIndeterminateDialog
import com.e.seasianoticeboard.views.core.BaseActivity
import com.e.seasianoticeboard.views.institute.newsfeed.AddPostActivity
import com.video.trimmer.interfaces.OnTrimVideoListener
import com.video.trimmer.interfaces.OnVideoListener
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class VideoTrimActivity : BaseActivity(), OnTrimVideoListener, OnVideoListener {
    var uri:Uri?=null

    var binding: ActivityVideoTrimBinding?=null
    private val progressDialog: VideoProgressIndeterminateDialog by lazy { VideoProgressIndeterminateDialog(this, "Cropping video. Please wait...") }

    override fun getLayoutId(): Int {
        return R.layout.activity_video_trim
    }

    override fun initViews() {
        try {
            binding=viewDataBinding as ActivityVideoTrimBinding

            if(intent.getStringExtra("path")!=null){
                var videoUri=intent.getStringExtra("path")
                binding!!.videoTrimmer.setTextTimeSelectionTypeface(FontsHelper[this, FontsConstants.SEMI_BOLD])
                        .setOnTrimVideoListener(this)
                        .setOnVideoListener(this)
                        .setVideoURI(Uri.parse(videoUri))
                        .setVideoInformationVisibility(true)
                        .setMaxDuration(60)
                        .setMinDuration(2)
                        .setDestinationPath()
            }
            binding!!.back.setOnClickListener {
                try {
                    binding!!.videoTrimmer.onCancelClicked()
                    finish()
                } catch (e: Exception) {
                }
            }

            binding!!.save.setOnClickListener {
                try {
                    binding!!.videoTrimmer.onSaveClicked()
                } catch (e: Exception) {
                }
            }
        } catch (e: Exception) {
        }
    }

    fun getVideoFilePath(): String? {
        return getAndroidMoviesFolder().absolutePath + "/" + SimpleDateFormat("yyyyMM_dd-HHmmss").format(Date()) + "cameraRecorder1"
    }

    fun getAndroidMoviesFolder(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    }

    override fun onTrimStarted() {
    }

    override fun getResult(uri: Uri) {

        this.uri=uri

        onBackPressed()

    }

    override fun onBackPressed() {
        //super.onBackPressed()

        setResult(Activity.RESULT_OK, getIntent().putExtra("filePath", uri.toString()))
        finish()
    }

    override fun cancelAction() {
    }

    override fun onError(message: String) {
    }

    override fun onVideoPrepared() {
    }

}
