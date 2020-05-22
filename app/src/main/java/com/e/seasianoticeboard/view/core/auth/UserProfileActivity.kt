package com.e.seasianoticeboard.view.core.auth

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.bumptech.glide.Glide
import com.e.seasianoticeboard.App
import com.e.seasianoticeboard.MainActivity
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.callbacks.UserProfileCallback
import com.e.seasianoticeboard.databinding.ActivityUserProfileBinding
import com.e.seasianoticeboard.model.*
import com.e.seasianoticeboard.presenter.UpdateUserProfilePresenter
import com.e.seasianoticeboard.util.CheckRuntimePermissions
import com.e.seasianoticeboard.util.DateTimeUtil
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.utils.UtilsFunctions.showToast
import com.e.seasianoticeboard.views.core.BaseActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class UserProfileActivity : BaseActivity(), View.OnClickListener, UserProfileCallback,
    RadioGroup.OnCheckedChangeListener {
    var binding: ActivityUserProfileBinding? = null
    var strDOB = ""
    var gender = ""
    var fileUri = ""
    var imageFile: File? = null
    var userId = "0"
    var emailId = ""
    var updateUser = false
    var presenter: UpdateUserProfilePresenter? = null
    var status = "0"
    private val REQUEST_PERMISSIONS = 1

    val PERMISSION_READ_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    var email = ""
    private val CAMERA_REQUEST = 1888
    private val RESULT_LOAD_IMAGE = 1999

    var mImageFile: File? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_user_profile
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityUserProfileBinding
        binding!!.includeView.ivBack.setOnClickListener { finish() }

        binding!!.etDob.setOnClickListener(this)
        binding!!.btSubmit.setOnClickListener(this)
        binding!!.ivProfile.setOnClickListener(this)
        binding!!.radioGroupGender.setOnCheckedChangeListener(this)
        presenter = UpdateUserProfilePresenter(this)
        if (intent.getStringExtra("comingFrom") != null) {
            binding!!.includeView.toolbatTitle.text = "Edit Profile"
            userId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")!!
            emailId = sharedPref!!.getString(PreferenceKeys.EMAIL, "")!!
            updateUser = true
            getUserProfile()
        } else {
            binding!!.includeView.toolbatTitle.setText("Signup")
        }

        if (intent.getStringExtra("email") != null) {
            email = intent.getStringExtra("email")!!;
            binding!!.etEmail.setText(email)
        }
    }

    fun getUserProfile() {
        var input = GetUserProfileInput()
        input.Email = emailId
        input.Phone = ""
        showDialog()
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        presenter!!.getUserProfile(input)

    }

    fun selectDatePicker() {
        val calendar = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            if (DateTimeUtil.checkFutureData(
                    calendar2,
                    calendar
                )
            ) {
                showToast("Please select past date")
            } else {
                strDOB = sdf.format(calendar.time)
                binding!!.etDob.setText(strDOB)
            }
        }
        val dpDialog = DatePickerDialog(
            this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dpDialog.show()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.et_dob -> {
                selectDatePicker()
            }
            R.id.bt_submit -> {
                validations()
            }
            R.id.iv_profile -> {


                if (CheckRuntimePermissions.checkMashMallowPermissions(
                        baseActivity,
                        PERMISSION_READ_STORAGE, REQUEST_PERMISSIONS
                    )
                ) {
                    selectImage()
                }
            }
        }
    }


    fun selectImage() {

        val builder1: AlertDialog.Builder = AlertDialog.Builder(this)
        builder1.setMessage("Select Image.")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "Camera",
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id: Int) {
                    camera()
                    dialog.cancel()
                }
            })

        builder1.setNegativeButton(
            "Gallery",
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id: Int) {
                    gallery()
                    dialog.cancel()
                }
            })

        val alert11: AlertDialog = builder1.create()
        alert11.show()
    }


    fun camera() {

        if (CheckRuntimePermissions.checkMashMallowPermissions(
                baseActivity,
                PERMISSION_READ_STORAGE, REQUEST_PERMISSIONS
            )
        ) {
            val cameraIntent =
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)

        }
    }


    fun gallery() {
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(i, RESULT_LOAD_IMAGE)
    }


    fun validations() {

        if (TextUtils.isEmpty(binding!!.etFirstName.text.toString())) {
            showToast(getString(R.string.enter_first_name))
        } else if (TextUtils.isEmpty(binding!!.etLastName.text.toString())) {
            showToast(getString(R.string.enter_last_name))
        } else if (TextUtils.isEmpty(binding!!.etPhone.text.toString())) {
            showToast(getString(R.string.enter_phone_number))
        } else if (binding!!.etPhone.text.toString().length < 10) {
            showToast(getString(R.string.phone_length))
        } else if (TextUtils.isEmpty(gender)) {
            showToast(getString(R.string.select_gender))
        } else if (TextUtils.isEmpty(binding!!.etDob.text.toString())) {
            showToast(getString(R.string.select_dob))
        } else {

            if (updateUser == true) {
                updateUser()
            } else {
                signupUser()
            }
        }
    }


    fun signupUser() {
        var input = UserProfileInput()
        input.FirstName = binding!!.etFirstName.text.toString()
        input.LastName = binding!!.etLastName.text.toString()
        input.DOB = binding!!.etDob.text.toString()
        input.Gender = gender
        input.Email = binding!!.etEmail.text.toString()
        input.UserId = "0"
        input.ImageUrl = fileUri
        input.IFile = ""
        input.Address = ""
        input.imageFile = imageFile
        input.PhoneNo = binding!!.etPhone.text.toString()
        input.ImageUrl = fileUri
        showDialog()
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        presenter!!.signupUser(input)
    }

    fun updateUser() {
        var input = UserProfileInput()
        input.FirstName = binding!!.etFirstName.text.toString()
        input.LastName = binding!!.etLastName.text.toString()
        input.DOB = binding!!.etDob.text.toString()
        input.Gender = gender
        input.Email = binding!!.etEmail.text.toString()
        input.UserId = userId
        input.IFile = ""
        input.imageFile = imageFile
        input.Address = ""
        input.PhoneNo = binding!!.etPhone.text.toString()
        input.ImageUrl = fileUri

        showDialog()
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        presenter!!.updateUser(input)

    }


    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val checkedRadioButton = group!!.findViewById(checkedId) as RadioButton
        when (checkedRadioButton.text.toString()) {
            "Male" -> {
                gender = "m"
            }
            "Female" -> {
                gender = "f"
            }
            "NA" -> {
                gender = "o"
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === CAMERA_REQUEST && resultCode === Activity.RESULT_OK) {
            val extras = data!!.getExtras();
            val imageBitmap = extras!!.get("data") as Bitmap;
            val tempUri = getImageUri(getApplicationContext(), imageBitmap);
            fileUri = getAbsolutePath(this@UserProfileActivity, tempUri)!!
            val file = File(fileUri)
            imageFile = file
            Glide.with(this).load(tempUri).placeholder(R.drawable.user).error(R.drawable.user)
                .into(binding!!.ivProfile)
        } else if (requestCode === RESULT_LOAD_IMAGE && resultCode === Activity.RESULT_OK && null != android.R.attr.data) {
            val selectedImage: Uri = data!!.getData()!!
            fileUri = getAbsolutePath(this, selectedImage)!!
            val file = File(fileUri)
            imageFile = file
            Glide.with(this).load(fileUri).placeholder(R.drawable.user).error(R.drawable.user)
                .into(binding!!.ivProfile)
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


    fun getAbsolutePath(
        activity: Context,
        uri: Uri
    ): String? {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            var cursor: Cursor? = null
            try {
                cursor = activity.contentResolver.query(uri, projection, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow("_data")
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) { // Eat it
                e.printStackTrace()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    override fun onBackPressed() {
        // super.onBackPressed()
        setResult(Activity.RESULT_OK, intent.putExtra("imageCode", status))
        finish()
    }

    override fun onSignupUser(data: UserProfileResponse) {
        hideDialog()

        if (data != null) {

            sharedPref!!.saveString(
                PreferenceKeys.USER_ID,
                data.ResultData!!.UserId.toString()
            )
            sharedPref!!.saveString(
                PreferenceKeys.USERNAME,
                data.ResultData!!.FirstName.toString() + " " + data.ResultData!!.LastName.toString()
            )
            sharedPref!!.saveString(
                PreferenceKeys.EMAIL,
                data.ResultData!!.Email.toString()
            )

            sharedPref!!.saveString(
                PreferenceKeys.USER_IMAGE,
                data.ResultData!!.ImageUrl.toString()
            )

            var intent = Intent(this@UserProfileActivity, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    override fun onUpdateUser(data: UserProfileResponse) {
        hideDialog()
        if (data != null) {
            showToast(data.Message)
            status = "1"
            sharedPref!!.saveString(
                PreferenceKeys.USER_IMAGE,
                data.ResultData!!.ImageUrl.toString()
            )
            onBackPressed()

        }
    }

    override fun onGetUserProfile(data: GetUserProfileResponse) {

        hideDialog()
        if (data != null && data!!.ResultData != null) {
            binding!!.etFirstName.setText(data.ResultData!!.FirstName)
            binding!!.etLastName.setText(data.ResultData!!.LastName)
            binding!!.etEmail.setText(data.ResultData!!.Email)
            binding!!.etPhone.setText(data.ResultData!!.PhoneNo)
            // binding!!.etDob.setText(data.ResultData.StrDOB)
            binding!!.etDob.setText(
                getLocalDate(
                    "yyyy-MM-dd'T'HH:mm:ss",
                    data.ResultData!!.DOB,
                    "yyyy-MM-dd"
                )
            )
            userId = data!!.ResultData!!.UserId!!
            gender = data!!.ResultData!!.Gender!!
            if (!data!!.ResultData!!.ImageUrl!!.isEmpty()) {

                Glide.with(this@UserProfileActivity).load(data!!.ResultData!!.ImageUrl)
                    .placeholder(R.drawable.user).error(R.drawable.user)
                    .into(binding!!.ivProfile!!)
                sharedPref!!.saveString(
                    PreferenceKeys.USER_IMAGE,
                    data!!.ResultData!!.ImageUrl.toString()
                )

            }
            if (gender.equals("M", true)) {
                binding!!.rbMale.isChecked = true
            } else if (gender.equals("F", true)) {
                binding!!.rbFemale.isChecked = true
            } else if (gender.equals("O", true)) {
                binding!!.rbNa.isChecked = true
            }
        }
    }
    override fun onError() {
        hideDialog()
    }
}
