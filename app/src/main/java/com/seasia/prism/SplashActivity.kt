package com.seasia.prism

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.seasia.prism.util.UtilsFunctions
import com.seasia.prism.core.auth.EmailActivity
import com.seasia.prism.core.auth.HobbiesActivity
import com.seasia.prism.core.BaseActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.seasia.prism.util.PreferenceKeys as PreferenceKeys1

class SplashActivity : BaseActivity() {
    private var timerThread: Thread? = null
    private var userId = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initViews() {
        userId = sharedPref!!.getString(PreferenceKeys1.USER_ID, "").toString()


        try {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
//Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

// Get new Instance ID token
                    var token = task.result?.token
                    var myToken= token!!.replace("\"", "");

                    sharedPref!!.save(PreferenceKeys1.DEVECE_TOKEN,myToken)


                    UtilsFunctions.TOKEN=token

                //   var str = token.replace("","");
                    Log.e("device_token",token);


// Log and toast
// Log.d(TAG, msg)
// Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })
        } catch (e: Exception) {
        }

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
