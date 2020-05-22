package com.e.seasianoticeboard.view.core.auth

import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import com.e.seasianoticeboard.App
import com.e.seasianoticeboard.MainActivity
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.callbacks.VerifyOtpCallback
import com.e.seasianoticeboard.databinding.ActivityOtpVerifyBinding
import com.e.seasianoticeboard.model.SignupVerificationResponse
import com.e.seasianoticeboard.model.VerifyEmailInput
import com.e.seasianoticeboard.model.VerifyEmailResponse
import com.e.seasianoticeboard.presenter.VerifyOtpPresenter
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.views.core.BaseActivity


class OtpVerifyActivity : BaseActivity(), View.OnClickListener, VerifyOtpCallback {


    var OtpId = "";
    var email = "";
    var otpVerifyPresenter:VerifyOtpPresenter?=null

    var binding: ActivityOtpVerifyBinding? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_otp_verify
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityOtpVerifyBinding
        binding!!.btnSubmit.setOnClickListener(this)
        binding!!.resendCode.setOnClickListener(this)
        binding!!.includeView.ivBack.setOnClickListener { finish() }
        binding!!.includeView.toolbatTitle.setText("Verify OTP")
        otpVerifyPresenter= VerifyOtpPresenter(this)
        if (intent.getStringExtra("OtpId") != null) {
            OtpId = intent.getStringExtra("OtpId");
        }
        if (intent.getStringExtra("email") != null) {
            email = intent.getStringExtra("email")!!;
            binding!!.emailCodeTxt.setText("Please enter the code that has been sent to you at " +email)

        }
     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding!!.viewTimer.isCountDown = true
        }
        binding!!.viewTimer.base = SystemClock.elapsedRealtime() + 20000
        binding!!.viewTimer.start()*/
    }

    override fun onClick(p0: View?) {
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

            R.id.resendCode->{
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
                    sharedPref!!.saveString(PreferenceKeys.USER_ID, data.ResultData!!.UserId.toString())
                    sharedPref!!.saveString(PreferenceKeys.EMAIL, data.ResultData!!.Email.toString())
                    sharedPref!!.saveString(PreferenceKeys.USERNAME, data.ResultData!!.FirstName.toString() + " " + data.ResultData!!.LastName.toString())
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
                }
            } else {
                UtilsFunctions.showToastError(data.Message)
            }
        } else
        {
            UtilsFunctions.showToastError("Somthing went wrong")
        }
    }

    override fun onResendOtp(data: SignupVerificationResponse) {
        hideDialog()
        if(data!=null){
            Toast.makeText(this@OtpVerifyActivity, "OTP has been sent on your mail", Toast.LENGTH_LONG).show()
        }
    }
    override fun onFailer() {
        hideDialog()
    }
}


