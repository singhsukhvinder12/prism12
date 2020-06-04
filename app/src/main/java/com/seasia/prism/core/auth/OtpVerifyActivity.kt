package com.seasia.prism.core.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.seasia.prism.App
import com.seasia.prism.MainActivity
import com.seasia.prism.R
import com.seasia.prism.callbacks.VerifyOtpCallback
import com.seasia.prism.databinding.ActivityOtpVerifyBinding
import com.seasia.prism.model.SignupVerificationResponse
import com.seasia.prism.model.VerifyEmailInput
import com.seasia.prism.model.VerifyEmailResponse
import com.seasia.prism.presenter.VerifyOtpPresenter
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.util.UtilsFunctions
import com.seasia.prism.core.BaseActivity
import com.goodiebag.pinview.Pinview


class OtpVerifyActivity : BaseActivity(), View.OnClickListener, VerifyOtpCallback {


    var OtpId = "";
    var email = "";
    var otpVerifyPresenter: VerifyOtpPresenter? = null

    var binding: ActivityOtpVerifyBinding? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_otp_verify
    }

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        binding = viewDataBinding as ActivityOtpVerifyBinding
        binding!!.btnSubmit.setOnClickListener(this)
        binding!!.resendCode.setOnClickListener(this)
        binding!!.parentId.setOnClickListener(this)
        binding!!.includeView.ivBack.setOnClickListener { finish() }
        binding!!.includeView.toolbatTitle.setText(getString(R.string.account_verification))
        otpVerifyPresenter = VerifyOtpPresenter(this)

        if (intent.getStringExtra("OtpId") != null) {
            OtpId = intent.getStringExtra("OtpId");
        }
        if (intent.getStringExtra("email") != null) {
            email = intent.getStringExtra("email")!!;
            binding!!.emailCodeTxt.setText("Please enter the code that has been sent to you at " + email)

        }
    }


    override fun onClick(p0: View?) {
        hideKeyboard()
        when (p0!!.id) {
            R.id.btnSubmit -> {
                if (binding!!.otpPin.value.length >= 6) {
                    var input = VerifyEmailInput()
                    input.Otp = binding!!.otpPin.value
                    input.OtpId = OtpId
                    showDialog()
                    if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                        UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                        return
                    }
                    otpVerifyPresenter!!.emailVarified(input)
                }
            }

            R.id.resendCode -> {
                showDialog()
                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                    return
                }
                otpVerifyPresenter!!.signupVerification(email)

            }
        }
    }


    override fun onOTPSucess(data: VerifyEmailResponse) {
        hideDialog()
        if (data != null) {
            if (data.StatusCode == "200") {
                if (data.ResultData != null) {
                    sharedPref!!.saveString(
                        PreferenceKeys.USER_ID,
                        data.ResultData!!.UserId.toString()
                    )
                    sharedPref!!.saveString(
                        PreferenceKeys.EMAIL,
                        data.ResultData!!.Email.toString()
                    )
                    sharedPref!!.saveString(
                        PreferenceKeys.USERNAME,
                        data.ResultData!!.FirstName.toString() + " " + data.ResultData!!.LastName.toString()
                    )

                    sharedPref!!.saveString(
                        PreferenceKeys.USER_IMAGE, data.ResultData!!.ImageUrl.toString())

                    var intent =
                        Intent(
                            this@OtpVerifyActivity,
                            MainActivity::class.java
                        )
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {
                    var intent = Intent(this@OtpVerifyActivity, UserProfileActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                }
            } else {
                UtilsFunctions.showToastError(data.Message)
            }
        } else {
            UtilsFunctions.showToastError("Somthing went wrong")
        }
    }

    override fun onResendOtp(data: SignupVerificationResponse) {
        hideDialog()
        if (data.ResultData != null) {
            Toast.makeText(
                this@OtpVerifyActivity,
                "OTP has been sent on your mail",
                Toast.LENGTH_LONG
            ).show()
            OtpId = data.ResultData!!

        }
    }

    override fun onFailer() {
        hideDialog()
    }
}


