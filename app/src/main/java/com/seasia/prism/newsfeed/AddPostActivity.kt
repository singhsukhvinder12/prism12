package com.seasia.prism.newsfeed

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.adapter.PostBackgroundColor
import com.seasia.prism.adapter.TagAdapter
import com.seasia.prism.callbacks.AddPostCallback
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.core.auth.EmailActivity
import com.seasia.prism.core.ui.SearchUserActivity
import com.seasia.prism.databinding.ActivityAddPostBinding
import com.seasia.prism.imagecroper.crop.CroppedActivity
import com.seasia.prism.model.output.TagList
import com.seasia.prism.newsfeed.audio_record.RecordAudioActivity
import com.seasia.prism.newsfeed.audioplayer.AndroidBuildingMusicPlayerActivity
import com.seasia.prism.newsfeed.camera.SquareActivity
import com.seasia.prism.newsfeed.displaynewsfeed.model.AddUpdateImageInput
import com.seasia.prism.newsfeed.displaynewsfeed.model.UpdateSchoolInput
import com.seasia.prism.presenter.AddPostPresenter
import com.seasia.prism.util.CheckRuntimePermissions
import com.seasia.prism.util.FileUtils
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.util.UtilsFunctions
import com.theartofdev.edmodo.cropper.CropImage
import com.vincent.videocompressor.VideoCompress
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.api.widget.Widget
import kotlinx.android.synthetic.main.upload_document_dialog.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("INACCESSIBLE_TYPE")
class AddPostActivity : BaseActivity(), View.OnClickListener, AddPostCallback, TextWatcher {
    var binding: ActivityAddPostBinding? = null
    private var mSeekPosition = 0
    private var cachedHeight = 0
    private var mAlbumFiles = ArrayList<AlbumFile>()
    var presenter: AddPostPresenter? = null
    var status = "0"
    var doubleClick = 0
    var tempPath = ""
    var TAG_USER_CODE = 501
    var colorCode="#FFFFFF"
    var tagList = ArrayList<TagList>()

    var tagAdapter: TagAdapter? = null
    var backgroundColor: PostBackgroundColor? = null

    private val RESULT_LOAD_IMAGE = 1999
    private val CAMERA_REQUEST = 1888
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
    var outputCompressPath = ""
    var mediaController: MediaController? = null
    var audioFIle: File? = null
    var videoFIle: File? = null
    var thumbNailFile: File? = null
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

    override fun onResume() {
        super.onResume()
        doubleClick = 0
    }

    @SuppressLint("ClickableViewAccessibility")
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
        binding!!.imgUpdatePost.setOnClickListener(this)
        binding!!.parent.setOnClickListener(this)
        binding!!.parent.setOnClickListener(this)
        binding!!.btnTagUser.setOnClickListener(this)

        binding!!.textPost.addTextChangedListener(this);
        presenter = AddPostPresenter(this)
        setImagesAdapter()


//        binding!!.includeView.icUpload.visibility=View.VISIBLE
//        binding!!.btnUpload.visibility=View.GONE


        mediaPlayer = MediaPlayer()
        random = Random()
        binding!!.userName.setText(sharedPref!!.getString(PreferenceKeys.USERNAME, "").toString())

        Glide.with(this).load(sharedPref!!.getString(PreferenceKeys.USER_IMAGE, ""))
            .placeholder(R.drawable.user).error(
                R.drawable.user
            ).into(binding!!.imgLogo)

        try {
            binding!!.textPost.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    if (v.id === R.id.textPost) {
                        v.parent.requestDisallowInterceptTouchEvent(true)
                        when (event.action and MotionEvent.ACTION_MASK) {
                            MotionEvent.ACTION_UP -> v.parent
                                .requestDisallowInterceptTouchEvent(false)
                        }
                    }
                    return false
                }
            })
        } catch (e: Exception) {
        }

        setAdapterData()
        setColorAdapter()





    }


    @SuppressLint("ResourceType")
    fun setColorAdapter() {

        val colorList = ArrayList<String>()
        colorList.add("" + getResources().getString(R.color.bg0))
        colorList.add("" + getResources().getString(R.color.bg1))
        colorList.add("" + getResources().getString(R.color.bg2))
        colorList.add("" + getResources().getString(R.color.bg3))
        colorList.add("" + getResources().getString(R.color.bg4))
        colorList.add("" + getResources().getString(R.color.bg5))
        colorList.add("" + getResources().getString(R.color.bg6))
        colorList.add("" + getResources().getString(R.color.bg7))
        colorList.add("" + getResources().getString(R.color.bg8))
        colorList.add("" + getResources().getString(R.color.bg9))
        colorList.add("" + getResources().getString(R.color.bg10))
        colorList.add("" + getResources().getString(R.color.bg11))
        colorList.add("" + getResources().getString(R.color.bg12))
        colorList.add("" + getResources().getString(R.color.bg13))
        colorList.add("" + getResources().getString(R.color.bg14))
        colorList.add("" + getResources().getString(R.color.bg15))
        colorList.add("" + getResources().getString(R.color.bg16))
        colorList.add("" + getResources().getString(R.color.bg17))
        colorList.add("" + getResources().getString(R.color.bg18))
        colorList.add("" + getResources().getString(R.color.bg19))
        backgroundColor = PostBackgroundColor(this@AddPostActivity, colorList)
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding!!.rvColor.layoutManager = mLayoutManager
        binding!!.rvColor.adapter = backgroundColor
    }


    fun backgroundColor(color: String, position: Int) {

        if (position == 0) {
            binding!!.parentWhtsMind.setBackground(ContextCompat.getDrawable(this, R.drawable.rectagle_gray_shap));
            colorCode="#FFFFFF"
//            binding!!.parentWhtsMind.setBackgroundResource(R.drawable.rectangle_blue_stroke);
        } else {
            colorCode=color
            binding!!.parentWhtsMind.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
        }
    }


    fun hideColorLayout(){

        binding!!.parentWhtsMind.setBackground(ContextCompat.getDrawable(this, R.drawable.rectagle_gray_shap));
        binding!!.rvColor.visibility=View.GONE
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
                            outputCompressPath = ""
//                            if (doubleClick == 0) {
//                                selectAlbum()
//                                doubleClick = 1
//                            }

                            uploadImage()

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

                    if (doubleClick == 0) {
                        if (imagesList!!.size == 0 && fileUri.isEmpty()) {
                            //
                            outputCompressPath = ""
                            var intent = Intent(this, SquareActivity::class.java)
                            startActivityForResult(intent, Video_AUDIO);
                            doubleClick = 1
                            //  }
                        } else {
                            baseActivity!!.showMessage(this, "Please delete selected media")
                        }
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
                        outputCompressPath = ""
                        if (doubleClick == 0) {
                            var intent = Intent(this, RecordAudioActivity::class.java)
                            startActivityForResult(intent, 1)
                            doubleClick = 1
                        }
                    }
                } else {
                    baseActivity!!.showMessage(this, "Please delete selected media")
                }
            }
            R.id.deleteVideo -> {
                binding!!.rvColor.visibility=View.VISIBLE
                fileUri = ""
                videoFIle = null
                binding!!.parentSelectedMedia.visibility = View.GONE
                binding!!.videoView1.stopPlayback()
                if (thumbNailFile != null) {
                    thumbNailFile!!.delete()
                }
            }

            R.id.deleteAudio -> {
                fileUri = ""
                binding!!.rvColor.visibility=View.VISIBLE
                audioFIle = null
                binding!!.parentAudio.visibility = View.GONE
            }
            R.id.btnUpload -> {
                if (binding!!.textPost.text.toString().trim().isEmpty()) {

                    showMessage(this, "Please enter description")
                } else {
                    if (videoFIle != null) {
                        binding!!.videoView1.stopPlayback()
                        videoCompressorCustom()
                    } else {
                        showDialog()
                        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                            return
                        }
                        presenter!!.getPostData(
                            addPostData(),
                            videoFIle,
                            audioFIle,
                            imagesList,
                            thumbNailFile
                        )
                    }

                }
            }
            R.id.card_view -> {
                val intent = Intent(this, AndroidBuildingMusicPlayerActivity::class.java);
                intent.putExtra("audio", fileUri)
                startActivity(intent)
            }

            R.id.btnTagUser -> {

                var intent = Intent(this, SearchUserActivity::class.java)
                intent.putExtra("comingFrom", "userTag")
                val args = Bundle()
                args.putSerializable("TAGARRAYLIST", tagList)
                intent.putExtra("BUNDLE", args)
                startActivityForResult(intent, TAG_USER_CODE)

            }
        }
    }


    private fun selectAlbum() {

        Album.image(this)
            .multipleChoice()
            .columnCount(4)
            .selectCount(5)
            .checkedList(mAlbumFiles)
            .camera(true)
            .widget(
                Widget.newDarkBuilder(this)
                    .title(getString(R.string.app_name))
                    .build()
            )
            .onResult { result ->
                //1 image 2 video
                mAlbumFiles = result
                if (imagesList != null) {
                    imagesList!!.clear()
                }

                for (i in 0 until mAlbumFiles.size) {
                    binding!!.parentImage.visibility = View.VISIBLE
                    val selectedVideoPath = mAlbumFiles.get(i).path
                    val list = ArrayList<AddUpdateImageInput>()
                    val addUpdateImageInput = AddUpdateImageInput()
                    addUpdateImageInput.imageUrl = selectedVideoPath
                    val file = File(selectedVideoPath)
                    addUpdateImageInput.fileType = "Image"
                    addUpdateImageInput.fileUrl = file
                    addUpdateImageInput.imageFileName = selectedVideoPath
                    list.add(addUpdateImageInput)
                    imagesList!!.addAll(list)
                }
                imagesAdapter!!.notifyDataSetChanged()
            }
            .onCancel {

                if (imagesList != null) {
                    imagesList!!.clear()
                }
                for (i in 0 until mAlbumFiles.size) {
                    binding!!.parentImage.visibility = View.VISIBLE
                    val selectedVideoPath = mAlbumFiles.get(i).path
                    val list = ArrayList<AddUpdateImageInput>()
                    val addUpdateImageInput = AddUpdateImageInput()
                    addUpdateImageInput.imageUrl = selectedVideoPath
                    val file = File(selectedVideoPath)
                    addUpdateImageInput.fileType = "Image"
                    addUpdateImageInput.fileUrl = file
                    addUpdateImageInput.imageFileName = selectedVideoPath
                    list.add(addUpdateImageInput)
                    imagesList!!.addAll(list)
                }
                imagesAdapter!!.notifyDataSetChanged()
            }
            .start()
    }


    private fun videoCompressorCustom() {


        if (outputCompressPath.isEmpty()) {
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

                    outputCompressPath = outPath
                    showDialog()
                    if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                        UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                        return
                    }
                    presenter!!.getPostData(
                        addPostData(),
                        File(outPath),
                        audioFIle,
                        imagesList,
                        thumbNailFile
                    )

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
        } else {
            showDialog()
            if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                return
            }
            presenter!!.getPostData(
                addPostData(),
                File(outputCompressPath),
                audioFIle,
                imagesList,
                thumbNailFile
            )
        }


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

        var list = ArrayList<String>()

        for (i in 0..tagList.size - 1) {
            list.add(tagList.get(i).tagId!!)
        }

//        if(list.size==0){
//            list.add("0")
//        }
//
        var addPost = AddPostInput()
        addPost.Title = ""
        addPost.Description = binding!!.textPost.text.toString().trim()
        addPost.Links = "seasia.in"
        addPost.DeleteIds = "0"
        addPost.NewsLetterIds = "0"
        addPost.ParticularId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")
        addPost.TypeId = "1631"
        addPost.TagIds = list
        addPost.DeletedTagIds = "0"
        addPost.ColorCode = colorCode
        return addPost
    }


    fun playVideo(videoUri: String?) {
        mediaController = MediaController(this)
        try {
            val uri: Uri = Uri.parse(videoUri.toString())
            binding!!.videoView1.setMediaController(mediaController)
            binding!!.videoView1.setVideoURI(uri)
            binding!!.videoView1.requestFocus()
            binding!!.videoView1.start()


            binding!!.parent.getViewTreeObserver()
                .addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
                    override fun onScrollChanged() {
                        mediaController!!.hide()
                    }
                })


        } catch (e: Exception) {
        }

    }

    var imagesList: ArrayList<AddUpdateImageInput>? = ArrayList()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            try {
                val selectedImage: Uri = data!!.getData()!!
                var imageUri = FileUtils.getPath(this, selectedImage)

                if (!getFileSize(File(imageUri)).equals("0")) {
                    var intent = Intent(this, CroppedActivity::class.java)
                    intent.putExtra("imagePath", imageUri)
                    startActivityForResult(intent, 127)
                }

            } catch (e: Exception) {
                Toast.makeText(this, " Corrupted File", Toast.LENGTH_LONG).show()

            }


//            CropImage.activity(selectedImage)
//
//                .setMinCropWindowSize(50000, 700)
//                .start(this);

//            val selectedVideoPath = getAbsolutePath(this, data!!.data!!)
//            val list = ArrayList<AddUpdateImageInput>()
//            val addUpdateImageInput = AddUpdateImageInput()
//            addUpdateImageInput.imageUrl = selectedVideoPath
//            val file = File(selectedVideoPath)
//            addUpdateImageInput.fileType = "Image"
//            addUpdateImageInput.fileUrl = file
//            addUpdateImageInput.imageFileName = selectedVideoPath
//            list.add(addUpdateImageInput)
//            imagesList!!.addAll(list)
//            imagesAdapter!!.notifyDataSetChanged()
        } else if (requestCode == 127) {

            if (data != null) {
                val bundle = data.getExtras();
                if (bundle!!.getString("filePath") != null && !bundle.getString("filePath")
                        .equals("null")
                ) {
                    binding!!.parentImage.visibility = View.VISIBLE
                    val selectedVideoPath = bundle!!.getString("filePath")!!
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
                    hideColorLayout()
                }
            }
        } else if (requestCode === CAMERA_REQUEST && resultCode === Activity.RESULT_OK) {
//            val extras = data!!.getExtras();
//
//            val imageBitmap = extras!!.get("data") as Bitmap;
//            val tempUri = getImageUriPath(getApplicationContext(), imageBitmap);

            var imageUri = FileUtils.getPath(this, Uri.parse(tempPath))
            var intent = Intent(this, CroppedActivity::class.java)
            intent.putExtra("imagePath", imageUri)
            startActivityForResult(intent, 127)

//            CropImage.activity(Uri.parse(tempPath))
////                .setMinCropResultSize(  5000, 1200)
////                .setMaxCropResultSize(  5000, 1200)
//                .setMinCropWindowSize(50000, 700)
//                .start(this);


        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (result != null) {
                hideColorLayout()
                binding!!.parentImage.visibility = View.VISIBLE
                val resultUri = result.uri
                val selectedVideoPath = getAbsolutePath(this@AddPostActivity, resultUri)!!
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
            }

        } else if (requestCode == RECODE_AUDIO && resultCode == RESULT_OK) {

            if (data != null) {
                hideColorLayout()
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
                    if (bundle!!.getString("filePath") != null && !bundle.getString("filePath")
                            .equals("null")
                    ) {
                        var status = bundle.getString("onBackPress")

                        if (status.equals("1")) {
                            var uri = bundle.getString("filePath")!!
                            fileUri = uri
                            hideColorLayout()

                            try {
                                //  runOnUiThread {
                                var ountDownTimer = object : CountDownTimer(1000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                    }

                                    override fun onFinish() {
                                        try {

                                            val thumb = ThumbnailUtils.createVideoThumbnail(
                                                uri,
                                                MediaStore.Images.Thumbnails.MINI_KIND
                                            );
                                            if (thumb != null) {
                                                val tempUri =
                                                    getImageUri(getApplicationContext(), thumb!!);
                                               // val thumPath = getAbsolutePath(baseActivity!!, tempUri)!!
                                                var thumPath = FileUtils.getPath(this@AddPostActivity, tempUri)

                                                val file = File(thumPath)
                                                thumbNailFile = file
                                            } else {
                                                val file = File("")
                                                thumbNailFile = file
                                            }

                                        } catch (e: Exception) {

                                        }
                                        //  file.delete()
                                        //  deleteFiles(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath)

                                    }
                                }.start()
                                //  }
                            } catch (e: Exception) {

                            }
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
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == TAG_USER_CODE) {

            if (data != null) {
                tagList = ArrayList()
                val args: Bundle = data.getBundleExtra("BUNDLE")
                tagList = args.getSerializable("tagList") as ArrayList<TagList>
                setAdapterData()
//                    var tagModel=TagList()
//                    tagModel.tagId=bundle.getString("userId")
//                    tagModel.userName=bundle.getString("userName")
//                    tagModel.userImage=bundle.getString("imageUrl")
//                    tagList.add(tagModel)
//                    setAdapterData()
                //Toast.makeText(this@AddPostActivity,"success coming",Toast.LENGTH_LONG).show()

            }


        }

    }


    fun setAdapterData() {
        tagAdapter = TagAdapter(this@AddPostActivity, tagList)
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding!!.rvTagUsers.layoutManager = mLayoutManager
        binding!!.rvTagUsers.adapter = tagAdapter

    }

    fun removeTag(position: Int) {
        tagList.removeAt(position)
        tagAdapter!!.notifyDataSetChanged()
    }

    fun getFileSize(file: File): String? {
        //final DecimalFormat format = new DecimalFormat("#.##");
        val format = DecimalFormat("#")
        val MiB: Long = 10000
        val KiB: Long = 1000
        var data = ""

        if (!file.isFile()) {
            data = "0"
        }
        require(file.isFile) {
//            "Expected a file"

        }
        val length = file.length().toDouble()
        if (length > MiB) {
            //  data= format.format(length / MiB) + " MiB";
            data = format.format(length / MiB)
        }
        if (length > KiB) {
            //data =format.format(length / KiB) + " KiB";
            data = format.format(length / KiB)
        }
        return data
    }


    fun getImageUriPath(
        inContext: Context,
        inImage: Bitmap?
    ): Uri? {
        val OutImage =
            Bitmap.createScaledBitmap(inImage!!, 2000, 2000, false)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            OutImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    fun getPath(uri: Uri?): String? {
        val projection =
            arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor =
            this.managedQuery(uri, projection, null, null, null)
        val column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }


    public fun deleteFiles(path: String) {

        try {
            var file = File(path);

            if (file.exists()) {
                var deleteCmd = "rm -r " + path;
                var runtime = Runtime.getRuntime();

                runtime.exec(deleteCmd);

            }
        } catch (e: Exception) {
        }
    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        var bytes = ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        var path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        );
        return Uri.parse(path);
    }

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
        try {
            imagesList!!.removeAt(position)
            mAlbumFiles.removeAt(position)
        } catch (e: java.lang.Exception) {

        }

        imagesAdapter!!.notifyDataSetChanged()
        if (imagesList!!.size == 0) {
            binding!!.parentImage.visibility = View.GONE
            binding!!.rvColor.visibility=View.VISIBLE
        }
    }

    override fun onSuccess(data: AddPostResponse) {

        baseActivity!!.hideDialog()
        if (data != null) {

            if (data.StatusCode == 200) {
                showMessage(this@AddPostActivity, data.Message!!)
                //finish()
                status = "1"
                if (thumbNailFile != null) {
                    thumbNailFile!!.delete()
                }

                onBackPressed()
            } else if (data.StatusCode == 400) {
                if (thumbNailFile != null) {
                    thumbNailFile!!.delete()
                }
                sharedPref!!.cleanPref()
                val intent = Intent(this@AddPostActivity, EmailActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                showMessage(this@AddPostActivity, "Session Expire")

            }
        } else {
            showMessage(this@AddPostActivity, "Somthing went wrong")
        }
    }

    override fun onError() {
        hideDialog()
    }

    private fun uploadImage() {

//        val uploadImage = Dialog(this)
        val uploadImage = Dialog(this,R.style.Theme_Dialog);
        uploadImage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        uploadImage.setContentView(R.layout.upload_document_dialog)

        uploadImage.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        uploadImage.setCancelable(true)
        uploadImage.setCanceledOnTouchOutside(true)
        uploadImage.window!!.setGravity(Gravity.BOTTOM)

        uploadImage.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        uploadImage.tvGallery.setOnClickListener {
            uploadImage.dismiss()
            gallery()
        }
        uploadImage.tvCamera.setOnClickListener {
            uploadImage.dismiss()
            camera()
        }
        uploadImage.tv_cancel.setOnClickListener {
            uploadImage.dismiss()
        }
        uploadImage.show()
    }

    fun camera() {

        if (CheckRuntimePermissions.checkMashMallowPermissions(
                baseActivity,
                PERMISSION_READ_STORAGE, REQUEST_PERMISSIONS
            )
        ) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(this!!.packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                }
                if (photoFile != null) {
                    val builder = StrictMode.VmPolicy.Builder()
                    StrictMode.setVmPolicy(builder.build())
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                    startActivityForResult(cameraIntent, CAMERA_REQUEST)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName, // prefix
            ".jpg", // suffix
            storageDir      // directory
        )

        // Save a file: path for use with ACTION_VIEW intents
        tempPath = "file:" + image.absolutePath
        return image
    }


    fun gallery() {
//        val i = Intent(
//            Intent.ACTION_PICK,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        )
//        startActivityForResult(i, RESULT_LOAD_IMAGE)


        val intent = Intent()
        intent.setTypeAndNormalize("image/*")
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(intent, "Select Video"),
            RESULT_LOAD_IMAGE
        )


    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//      if(!binding!!.textPost.text.trim().isEmpty()){
//          binding!!.imgUpdatePostDesabled.visibility=View.GONE
//          binding!!.imgUpdatePost.visibility=View.VISIBLE
//      } else{
//          binding!!.imgUpdatePostDesabled.visibility=View.VISIBLE
//          binding!!.imgUpdatePost.visibility=View.GONE
//      }
    }

}



