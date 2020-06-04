package com.seasia.prism.core.newsfeed.displaynewsfeed.view

import android.annotation.SuppressLint
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.MediaController
import com.seasia.prism.R
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.databinding.ActivityPlayVideoBinding


class PlayVideoActivity : BaseActivity() {
    var mediaController: MediaController? = null

    companion object{
        var binding: ActivityPlayVideoBinding? = null

    }
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

// onH
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event){
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            super.hide();
//            ((Activity) getContext()).finish();
//            return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }
//};


    fun playVideo(videoPath: String?) {
        val myUri = Uri.parse(videoPath)
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        mediaController = MediaController(this)
        //   mediaController.setAnchorView(binding!!.videoView)
        mediaController!!.setAnchorView(binding!!.videoView);
        mediaController!!.setMediaPlayer(binding!!.videoView);
        binding!!.videoView.setMediaController(mediaController)

        binding!!.videoView.setVideoURI(myUri)
        binding!!.videoView.requestFocus()
        binding!!.progress.visibility = View.VISIBLE
      binding!!.videoView.start()
        var  current =  binding!!.videoView.getCurrentPosition()

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




//        binding!!.progress.setProgress(0);
//        binding!!.progress.setMax(100);
       // MyAsync().execute();
    }




//    private class MyAsync : AsyncTask<Void?, Int?, Void?>() {
//        var duration = 0
//        var current = 0
//         @SuppressLint("WrongThread")
//         override fun doInBackground(vararg params: Void?): Void? {
//            binding!!.videoView.start()
//            binding!!.videoView.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
//                override fun onPrepared(p0: MediaPlayer?) {
//                    duration =  binding!!.videoView.getDuration()
//                }
//
//            })
//            do {
//                current =  binding!!.videoView.getCurrentPosition()
//
//
//                try {
//                    publishProgress((current * 100 / duration))
//                    if (binding!!.progress.getProgress() >= 100) {
//                        break
//                    }
//                } catch (e: Exception) {
//                }
//            } while (binding!!.progress.getProgress() <= 100)
//            return null
//        }
//
//         override fun onProgressUpdate(vararg values: Int?) {
//            super.onProgressUpdate(*values)
//             println(values[0])
//             binding!!.progress.setProgress(values[0]!!)
//
//
//         }
//    }




    override fun onBackPressed() {
        super.onBackPressed()
        finish()


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





}
