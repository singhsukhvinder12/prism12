package com.e.seasianoticeboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.view.core.auth.EmailActivity
import com.e.seasianoticeboard.views.core.BaseActivity

class SplashActivity : BaseActivity() {
    private var timerThread: Thread? = null
    private var userId = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initViews() {
        userId = sharedPref!!.getString(PreferenceKeys.USER_ID, "").toString()
        timerThread = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(3000)

                    if (TextUtils.isEmpty(userId)) {
                        val intent =
                            Intent(this@SplashActivity, EmailActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        timerThread!!.start()    }
}
