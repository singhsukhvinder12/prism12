package com.e.seasianoticeboard.view.core.auth

import android.content.Intent
import android.view.View
import com.e.seasianoticeboard.App
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.callbacks.EmailVeryfyCallback
import com.e.seasianoticeboard.databinding.ActivityEmailBinding
import com.e.seasianoticeboard.model.SignupVerificationResponse
import com.e.seasianoticeboard.presenter.EmailVerifyPresenter
import com.e.seasianoticeboard.utils.Utils
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.views.core.BaseActivity

class EmailActivity : BaseActivity(), View.OnClickListener, EmailVeryfyCallback {

    var binding: ActivityEmailBinding? = null
    var presenter: EmailVerifyPresenter? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_email
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityEmailBinding
        binding!!.btnSubmit.setOnClickListener(this)
        binding!!.includeView.ivBack.visibility = View.GONE
        binding!!.includeView.toolbatTitle.setText("Login")
        binding!!.layoutMain.setOnClickListener(this)

        presenter = EmailVerifyPresenter(this)


    }

    override fun onClick(p0: View?) {
        hideKeyboard()
        when (p0!!.id) {
            R.id.btnSubmit -> {
                if (binding!!.edEmail.text!!.isEmpty()) {
                    binding!!.edEmail.error = "Please enter email"
                    binding!!.edEmail.requestFocus()
                }

                else if ((!Utils.emailValidator(binding!!.edEmail.text.toString())) && (!Utils.emailValidatorSecond(
                        binding!!.edEmail.text.toString()
                    ))
                ) {
                    binding!!.edEmail.error = "Please check email"
                    binding!!.edEmail.requestFocus()
                }
                else {
                    showDialog()
                    if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                        UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                        return
                    }
                    presenter!!.signupVerification(binding!!.edEmail.text.toString())
//
//                    var intent = Intent(this@EmailActivity, UserProfileActivity::class.java)
//                    intent.putExtra("email", "1234")
//                    startActivity(intent)
                    // finish()
//
//                    var intent = Intent(this@EmailActivity, OtpVerifyActivity::class.java)
//                    intent.putExtra("OtpId", "")
//                    intent.putExtra("email", binding!!.edEmail.text!!.toString())
//                    binding!!.edEmail.setText("")
//                    startActivity(intent)

                }
            }
        }
    }

    override fun onSuccess(body: SignupVerificationResponse) {
        hideDialog()
        if (body.ResultData != null) {
            var intent = Intent(this@EmailActivity, OtpVerifyActivity::class.java)
            intent.putExtra("OtpId", body.ResultData)
            intent.putExtra("email", binding!!.edEmail.text!!.toString())
            binding!!.edEmail.setText("")
            startActivity(intent)
        }
    }

    override fun onError() {
        hideDialog()
    }
}