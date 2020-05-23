package com.e.seasianoticeboard.views.institute.newsfeed

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.e.seasianoticeboard.App
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.callbacks.AddPostCallback
import com.e.seasianoticeboard.databinding.ActivityAddPostBinding
import com.e.seasianoticeboard.presenter.AddPostPresenter
import com.e.seasianoticeboard.util.CheckRuntimePermissions
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.view.core.newsfeed.audio.RecordAudioActivity
import com.e.seasianoticeboard.view.core.newsfeed.audioplayer.AndroidBuildingMusicPlayerActivity
import com.e.seasianoticeboard.view.core.newsfeed.camera.SquareActivity
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.AddUpdateImageInput
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.UpdateSchoolInput
import com.e.seasianoticeboard.views.core.BaseActivity
import com.vincent.videocompressor.VideoCompress
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("INACCESSIBLE_TYPE")
class AddPostActivity : BaseActivity(), View.OnClickListener, AddPostCallback {
    var binding: ActivityAddPostBinding? = null
    private var mSeekPosition = 0
    private var cachedHeight = 0
    var presenter: AddPostPresenter? = null
    var status = "0"
    var AudioSavePathInDevice = ""
    private var isFullscreen = false
    val PERMISSION_READ_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
    private val REQUEST_PERMISSIONS = 1
    private val REQUEST_PERMISSIONS_ATTACHEMENT = 2
    private val RC_CODE_PICKER_LOGO = 2000
    private val RECODE_AUDIO = 1
    private val Video_AUDIO = 2
    var imagesAdapter: ImageListAdapter? = null
    var fileUri = ""
    var mediaController: MediaController? = null
    var addPost: AddPostViewModel? = null
    var audioFIle: File? = null
    var videoFIle: File? = null
    var RandomAudioFileName = "ABCDEFGHIJKLMNOP"
    var deletedImageList: ArrayList<UpdateSchoolInput.LstDeleteAttachment>? = ArrayList()
    var random: Random? = null
    var seekBar: SeekBar? = null;
    var wasPlaying = false;
    var mediaPlayer: MediaPlayer? = null

    public var myDir_images_sent =
        File(Environment.getExternalStoragePublicDirectory("Fang"), "Images/Sent");
    public var myDir_omorni = File(Environment.getExternalStoragePublicDirectory("Fang"), "Data");

    private val SEEK_POSITION_KEY = "SEEK_POSITION_KEY"

    override fun getLayoutId(): Int {
        return R.layout.activity_add_post
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityAddPostBinding
        binding!!.includeView.toolbatTitle.setText("Add Post")
        binding!!.includeView.ivBack.setOnClickListener(this)
        binding!!.btnChooseImage.setOnClickListener(this)
        binding!!.btnChooseVideo.setOnClickListener(this)
        binding!!.btnChooseAudio.setOnClickListener(this)
        binding!!.btnChooseAudio.setOnClickListener(this)
        binding!!.deleteVideo.setOnClickListener(this)
        binding!!.deleteAudio.setOnClickListener(this)
        binding!!.btnUpload.setOnClickListener(this)
        binding!!.cardView.setOnClickListener(this)
        presenter = AddPostPresenter(this)
        setImagesAdapter()
        mediaPlayer = MediaPlayer()
        addPost = ViewModelProviders.of(this).get(AddPostViewModel::class.java)
        random = Random()
        binding!!.userName.setText(sharedPref!!.getString(PreferenceKeys.USERNAME, "").toString())

        Glide.with(this).load(sharedPref!!.getString(PreferenceKeys.USER_IMAGE))
            .placeholder(R.drawable.user).error(
                R.drawable.user
            ).into(binding!!.imgLogo)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition)
    }

    override fun onRestoreInstanceState(outState: Bundle) {
        super.onRestoreInstanceState(outState)
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY)
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.btnChooseImage -> {

                if (imagesList!!.size < 5) {

                    if (fileUri.isEmpty()) {
                        if (CheckRuntimePermissions.checkMashMallowPermissions(
                                baseActivity,
                                PERMISSION_READ_STORAGE, REQUEST_PERMISSIONS
                            )
                        ) {
                            var i = Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            );
                            startActivityForResult(i, RC_CODE_PICKER_LOGO);

                        }
                    } else {
                        baseActivity!!.showMessage(this, "Please delete selected media")
                    }
                } else {
                    baseActivity!!.showMessage(this, getString(R.string.max_five_photos))

                }
            }
            R.id.btnChooseVideo -> {
                if (CheckRuntimePermissions.checkMashMallowPermissions(
                        baseActivity,
                        PERMISSION_READ_STORAGE, REQUEST_PERMISSIONS
                    )
                ) {
                    if (imagesList!!.size == 0 && fileUri.isEmpty()) {
                        var intent = Intent(this, SquareActivity::class.java)
                        startActivityForResult(intent, Video_AUDIO);
                    } else {
                        baseActivity!!.showMessage(this, "Please delete selected media")
                    }
                }

            }
            R.id.btnChooseAudio -> {
                if (imagesList!!.size == 0 && fileUri.isEmpty()) {
                    if (CheckRuntimePermissions.checkMashMallowPermissions(
                            baseActivity,
                            PERMISSION_READ_STORAGE, REQUEST_PERMISSIONS
                        )
                    ) {
                        ///   recordActivity()

                        var intent = Intent(this, RecordAudioActivity::class.java)
                        startActivityForResult(intent, 1)
                    }
                } else {
                    baseActivity!!.showMessage(this, "Please delete selected media")
                }
            }
            R.id.deleteVideo -> {
                fileUri = ""
                videoFIle = null
                binding!!.parentSelectedMedia.visibility = View.GONE
                binding!!.videoView1.stopPlayback()
            }

            R.id.deleteAudio -> {
                fileUri = ""
                audioFIle = null
                binding!!.parentAudio.visibility = View.GONE
            }
            R.id.btnUpload -> {
                if (!binding!!.textPost.text.toString().isEmpty()) {


                    if (videoFIle != null) {
                        binding!!.videoView1.stopPlayback()
                        videoCompressorCustom()
                    } else {
                        showDialog()
                        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                            return
                        }
                        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                            return
                        }
                        presenter!!.getPostData(addPostData(), videoFIle, audioFIle, imagesList)

                    }


                } else {
                    showMessage(this, "Please enter description")
                }
            }
            R.id.card_view -> {
                val intent = Intent(this, AndroidBuildingMusicPlayerActivity::class.java);
                intent.putExtra("audio", fileUri)
                startActivity(intent)
            }
        }
    }


    private fun videoCompressorCustom() {

        var myDirectory = File(Environment.getExternalStorageDirectory(), "Pictures");

        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }

        var outPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + File.separator + "VID_" + SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                getLocale()
            ).format(Date()) + ".mp4";


        var progressDialog = ProgressDialog(this)

        VideoCompress.compressVideoMedium(fileUri, outPath, object :
            VideoCompress.CompressListener {
            override fun onStart() {
                Log.e("Compressing", "Compress Start")
                progressDialog.setCancelable(false)
                progressDialog.setMessage("Processing Video...")
                progressDialog.show()
            }

            override fun onSuccess() {

                try {
                    if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                } catch (e: IllegalArgumentException) { // Handle or log or ignore

                } catch (e: java.lang.Exception) { // Handle or log or ignore

                }
                showDialog()
                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                    return
                }
                presenter!!.getPostData(addPostData(), File(outPath), audioFIle, imagesList)

//                addPost!!.hitAddPostAPI(addPostData(), File(outPath), audioFIle, imagesList)
//                addPost?.getData()?.observe(
//                    this@AddPostActivity, object : Observer<AddPostResponse> {
//                        override fun onChanged(data: AddPostResponse?) {
//                            baseActivity!!.hideDialog()
//                            if (data != null) {
//
//                                if (data.StatusCode == 200) {
//                                    showMessage(this@AddPostActivity, data.Message!!)
//                                    //finish()
//                                    status = "1"
//                                    onBackPressed()
//                                } else {
//                                    showMessage(this@AddPostActivity, data.Message!!)
//                                }
//                            } else {
//                                showMessage(this@AddPostActivity, "Somthing went wrong")
//                            }
//                        }
//                    })

            }

            override fun onFail() {
                Log.e("Compressing", "Compress Failed!")
                try {
                    if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                } catch (e: IllegalArgumentException) { // Handle or log or ignore

                } catch (e: java.lang.Exception) { // Handle or log or ignore

                }
            }

            override fun onProgress(percent: Float) {
                progressDialog.setMessage("Compressing video " + percent.toInt() + "%")
                Log.e("Compressing", percent.toString())

            }
        })


    }

    private fun getLocale(): Locale? {
        val config = resources.configuration
        var sysLocale: Locale? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config)
        } else {
            sysLocale = getSystemLocaleLegacy(config)
        }
        return sysLocale
    }

    fun getSystemLocaleLegacy(config: Configuration): Locale? {
        return config.locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun getSystemLocale(config: Configuration): Locale? {
        return config.locales[0]
    }


    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, getIntent().putExtra("backPress", status))
        finish()
    }

    fun addPostData(): AddPostInput {
        var addPost = AddPostInput()
        addPost.Title = ""
        addPost.Description = binding!!.textPost.text.toString()
        addPost.Links = "seasia.in"
        addPost.DeleteIds = "0"
        addPost.NewsLetterIds = "0"
        addPost.ParticularId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")
        addPost.TypeId = "0"
        return addPost
    }

    fun getVideoFilePath(): String? {
        return getAndroidMoviesFolder()!!.absolutePath + "/" + SimpleDateFormat("yyyyMM_dd-HHmmss").format(
            Date()
        ) + "cameraRecorder1.mp4"
    }

    fun getAndroidMoviesFolder(): File? {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    }
//
//    fun recordActivity() {
//
//        val filePath = getVideoFilePath()
//        val color = resources.getColor(R.color.colorPrimaryDark)
//        AndroidAudioRecorder.with(this)
//            .setFilePath(filePath)
//            .setColor(color)
//            .setRequestCode(RECODE_AUDIO)
//            .setSource(AudioSource.MIC)
//            .setChannel(AudioChannel.STEREO)
//            .setSampleRate(AudioSampleRate.HZ_48000)
//            .setAutoStart(true)
//            .setKeepDisplayOn(true)
//            .record()
//    }

    fun playVideo(videoUri: String?) {
        mediaController = MediaController(this)
        try {
            val uri: Uri = Uri.parse(videoUri.toString())
            binding!!.videoView1.setMediaController(mediaController)
            binding!!.videoView1.setVideoURI(uri)
            binding!!.videoView1.requestFocus()
            binding!!.videoView1.start()

        } catch (e: Exception) {
        }

    }

    var imagesList: ArrayList<AddUpdateImageInput>? = ArrayList()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_CODE_PICKER_LOGO && resultCode == RESULT_OK) {
            binding!!.parentImage.visibility = View.VISIBLE
            val selectedVideoPath = getAbsolutePath(this, data!!.data!!)
            val list = ArrayList<AddUpdateImageInput>()
            val addUpdateImageInput = AddUpdateImageInput()
            addUpdateImageInput.imageUrl = selectedVideoPath
            val file = File(selectedVideoPath)
            addUpdateImageInput.fileType = "Image"
            addUpdateImageInput.fileUrl = file
            addUpdateImageInput.imageFileName = selectedVideoPath
            list.add(addUpdateImageInput)
            imagesList!!.addAll(list)
            imagesAdapter!!.notifyDataSetChanged()

        } else if (requestCode == RECODE_AUDIO && resultCode == RESULT_OK) {

            if (data != null) {
                val bundle = data!!.getExtras();
                fileUri = bundle!!.getString("data")!!
                binding!!.parentSelectedMedia.visibility = View.GONE
                binding!!.parentAudio.visibility = View.VISIBLE
                val file = File(fileUri)
                audioFIle = file
                var calendar = Calendar.getInstance();
                var timeMilli2 = calendar.getTimeInMillis();
                binding!!.tvRecording.setText("Audio_" + timeMilli2)
            }
        } else if (requestCode == Video_AUDIO && resultCode == RESULT_OK) {

            try {
                if (data != null) {
                    val bundle = data.getExtras();
                    if (bundle!!.getString("filePath") != null && !bundle.getString("filePath").equals(
                            "null"
                        )
                    ) {
                        var uri = bundle.getString("filePath")!!
                        fileUri = uri


                        var video_bitmap = ThumbnailUtils.createVideoThumbnail(
                            fileUri,
                            MediaStore.Video.Thumbnails.MINI_KIND
                        );


//                        ImageView imageview_mini =(ImageView) findViewById (R.id.thumbnail_mini);
//                        ImageView imageview_micro =(ImageView) findViewById (R.id.thumbnail_micro);

                        var bmThumbnail: Bitmap? = null;

//MICRO_KIND, size: 96 x 96 thumbnail
                        bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            fileUri,
                            MediaStore.Images.Thumbnails.MICRO_KIND
                        );
                        //   imageview_micro.setImageBitmap(bmThumbnail);

// MINI_KIND, size: 512 x 384 thumbnail
                        bmThumbnail =
                            ThumbnailUtils.createVideoThumbnail(
                                fileUri,
                                MediaStore.Images.Thumbnails.MINI_KIND
                            );
                        //   imageview_mini.setImageBitmap(bmThumbnail);


//                    file_upload = Utils.writeSendFileToExternalDirectory(null, "Video_" + selectedvideo.substring(selectedvideo.lastIndexOf("/") + 1), selectedvideo, "2");
//            /    thumbnail_path = getInsertedPath(video_bitmap, "", "image");


                        mediaPlayer = MediaPlayer()
                        val file = File(uri)
                        videoFIle = file
                        binding!!.parentSelectedMedia.visibility = View.VISIBLE
                        binding!!.parentAudio.visibility = View.GONE
                        var ountDownTimer = object : CountDownTimer(1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                            }

                            override fun onFinish() {
                                playVideo(uri)
                            }
                        }.start()
                    }

                    //  createVideoThumbNail(fileUri)
                }
            } catch (e: Exception) {
            }
        }

    }


//    fun createVideoThumbNail(fileUri: String?) {
//        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = this.contentResolver.query(Uri.parse(fileUri), filePathColumn, null, null, null)
//        cursor!!.moveToFirst()
//
//        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//        val picturePath = cursor.getString(columnIndex)
//        cursor!!.close()
//
//        val bitmap = ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Video.Thumbnails.MICRO_KIND)
//
//    }


//    public fun getInsertedPath(Bitmap bitmap, String path, String file_name):String {
//        var strMyImagePath = null;
////        File mFolder = new File(myDir_omorni);
//        if (!myDir_omorni.exists()) {
//            myDir_omorni.mkdir();
//        }
//        var s = file_name + ".jpg";
//        var f =  File(myDir_omorni, s);
//
//        strMyImagePath = f.absolutePath;
//        var fos = null;
//        try {
//            fos =  FileOutputStream(f);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
//            fos!!.flush;
//            fos.close();
//        } catch (e : FileNotFoundException) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (strMyImagePath == null) {
//            return path;
//        }
//        return strMyImagePath;
//    }


    fun getAbsolutePath(activity: Context, uri: Uri): String? {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            var cursor: Cursor? = null
            try {
                cursor = activity.contentResolver.query(uri, projection, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow("_data")
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: java.lang.Exception) { // Eat it
                e.printStackTrace()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }


    private fun setImagesAdapter() {
        val mLayoutManager =
            LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL, false)
        binding!!.rvImage.layoutManager = mLayoutManager
        imagesAdapter = ImageListAdapter(this@AddPostActivity!!, imagesList)
        binding!!.rvImage.adapter = imagesAdapter
    }

    fun DeleteImage(position: Int) {
        if (!imagesList!![position].imageId.equals(null) && !TextUtils.isEmpty(imagesList!![position].imageId) && !imagesList!![position].imageId.equals(
                "0"
            )
        ) {
            var deleteImageAttachement = UpdateSchoolInput.LstDeleteAttachment()
            deleteImageAttachement.deleteAttachmentId = imagesList!![position].imageId!!
            deletedImageList!!.add(deleteImageAttachement)
        }
        imagesList!!.removeAt(position)
        imagesAdapter!!.notifyDataSetChanged()
        if (imagesList!!.size == 0) {
            binding!!.parentImage.visibility = View.GONE
        }
    }

    override fun onSuccess(data: AddPostResponse) {

        baseActivity!!.hideDialog()
        if (data != null) {

            if (data.StatusCode == 200) {
                showMessage(this@AddPostActivity, data.Message!!)
                //finish()
                status = "1"
                onBackPressed()
            } else {
                showMessage(this@AddPostActivity, data.Message!!)
            }
        } else {
            showMessage(this@AddPostActivity, "Somthing went wrong")
        }
    }

    override fun onError() {
        hideDialog()
    }
}



