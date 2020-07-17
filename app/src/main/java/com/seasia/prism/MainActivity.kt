package com.seasia.prism

import android.app.Dialog
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
import com.seasia.prism.core.auth.ProfileActivity
import com.seasia.prism.core.ui.SearchUserActivity
import com.seasia.prism.newsfeed.displaynewsfeed.view.NewsFeedFragment


class MainActivity : BaseActivity() {
    var newsFeedFragment: NewsFeedFragment = NewsFeedFragment()
    var click = 0
    var binding: ActivityMainBinding? = null
    var userImage = ""
    var userId = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onResume() {
        super.onResume()
            Glide.with(this)
                .load(sharedPref!!.getString(PreferenceKeys.USER_IMAGE, ""))
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(binding!!.includeView.ivEditProfile)

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
        binding!!.includeView.ivSearch.visibility = View.VISIBLE
        binding!!.includeView.toolbatTitle.setText("Seasia Prism")
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, newsFeedFragment)
        transaction.commit()
        binding!!.includeView.ivEditProfile.setOnClickListener {

            if (click == 0) {
               // var intent = Intent(this, UserProfileActivity::class.java)
                var intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("comingFrom", "editProfile")
                intent.putExtra("postedByMail", sharedPref!!.getString(PreferenceKeys.EMAIL, "")!!)
                intent.putExtra("anotherUser",sharedPref!!.getString(PreferenceKeys.USER_ID, "")!!)
                intent.putExtra("profileImage",sharedPref!!.getString(PreferenceKeys.USER_IMAGE, "")!!)
                intent.putExtra("userName",sharedPref!!.getString(PreferenceKeys.USERNAME, "")!!)
                intent.putExtra("status",sharedPref!!.getString(PreferenceKeys.BIO, "")!!)
                startActivityForResult(intent, 205)
                click = 1
            }

            //Toast.makeText(this,"Coming Soon",Toast.LENGTH_LONG).show()

        }
        userImage = sharedPref!!.getString(PreferenceKeys.USER_IMAGE, "")!!
        userId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")!!
        Glide.with(this).load(userImage).placeholder(R.drawable.user).error(R.drawable.user)
            .into(binding!!.includeView.ivEditProfile)


        binding!!.includeView.ivSearch.setOnClickListener {
         //   logoutDialog()

            val intent = Intent(this, SearchUserActivity::class.java)
                startActivity(intent)
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

}
