package com.seasia.prism

import android.app.Dialog
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.seasia.prism.callbacks.LogoutCallback
import com.seasia.prism.databinding.ActivityMainBinding
import com.seasia.prism.model.output.LogoutResponse
import com.seasia.prism.presenter.LogoutPresenter
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.core.auth.EmailActivity
import com.seasia.prism.core.auth.UserProfileActivity
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.core.newsfeed.displaynewsfeed.view.NewsFeedFragment


class MainActivity : BaseActivity(), LogoutCallback {
    var newsFeedFragment: NewsFeedFragment = NewsFeedFragment()
    var logoutPresenter: LogoutPresenter? = null
    var click = 0
    var binding: ActivityMainBinding? = null
    var userImage = ""
    var userId = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onResume() {
        super.onResume()
        click = 0
        try {
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)

        } catch (e: Exception) {

        }
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityMainBinding
        binding!!.includeView.ivBack.visibility = View.GONE
        binding!!.includeView.ivEditProfile.visibility = View.VISIBLE
        binding!!.includeView.ivLogout.visibility = View.VISIBLE
        binding!!.includeView.toolbatTitle.setText("Seasia Prism")
        val transaction = supportFragmentManager.beginTransaction()
        logoutPresenter = LogoutPresenter(this)
        transaction.replace(R.id.frame_layout, newsFeedFragment)
        transaction.commit()
        binding!!.includeView.ivEditProfile.setOnClickListener {

            if (click == 0) {
                var intent = Intent(this, UserProfileActivity::class.java)
                intent.putExtra("comingFrom", "editProfile")
                intent.putExtra("postedByMail", sharedPref!!.getString(PreferenceKeys.EMAIL, "")!!)
                intent.putExtra("anotherUser",sharedPref!!.getString(PreferenceKeys.USER_ID, "")!!)
                startActivityForResult(intent, 205)
                click = 1
            }

            //Toast.makeText(this,"Coming Soon",Toast.LENGTH_LONG).show()

        }
        userImage = sharedPref!!.getString(PreferenceKeys.USER_IMAGE, "")!!
        userId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")!!
        Glide.with(this).load(userImage).placeholder(R.drawable.user).error(R.drawable.user)
            .into(binding!!.includeView.ivEditProfile)


        binding!!.includeView.ivLogout.setOnClickListener {
            logoutDialog()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.frame_layout)
        fragment!!.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 205) {
            try {
                if (data != null) {
                    val bundle = data!!.getExtras();
                    var code = bundle!!.getString("imageCode")!!
                    if (code.equals("1")) {
                        /////change image
                        userImage = sharedPref!!.getString(PreferenceKeys.USER_IMAGE, "")!!
                        Glide.with(this).load(userImage).placeholder(R.drawable.user)
                            .error(R.drawable.user)
                            .into(binding!!.includeView.ivEditProfile)

                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun logoutDialog() {

        var dialog = Dialog(baseActivity!!)
        dialog.setContentView(R.layout.logout_dialog);
        dialog.setCanceledOnTouchOutside(false)
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent);
        var btnLogout = dialog.findViewById<TextView>(R.id.tv_delete)
        var btnCancel = dialog.findViewById<TextView>(R.id.tv_cancel)
        btnLogout.setOnClickListener {
            dialog.hide()
            showDialog()
            logoutPresenter!!.hitApiLogout(userId, "1")

        }
        btnCancel.setOnClickListener {
            dialog.hide()
        }
        dialog.show()
    }

    override fun onSuccess(data: LogoutResponse) {
        hideDialog()
        if (data != null) {
            if(data.StatusCode == "200" || data.StatusCode == "400" ){
                sharedPref!!.cleanPref()
                val intent = Intent(this@MainActivity, EmailActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
    }

    override fun onFailer() {
        hideDialog()
    }
}
