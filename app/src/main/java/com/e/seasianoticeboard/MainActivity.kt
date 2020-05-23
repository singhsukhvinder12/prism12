package com.e.seasianoticeboard

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.e.seasianoticeboard.databinding.ActivityMainBinding
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.view.core.auth.UserProfileActivity
import com.e.seasianoticeboard.views.core.BaseActivity
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.pagination.NewsFeedFragment
import java.lang.Exception


class MainActivity : BaseActivity() {
    var newsFeedFragment: NewsFeedFragment = NewsFeedFragment()

    var binding: ActivityMainBinding? = null
    var userImage = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onResume() {
        super.onResume()
        try {
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)

        } catch (e: Exception) {

        }
    }
    override fun initViews() {
        binding = viewDataBinding as ActivityMainBinding
        binding!!.includeView.ivBack.visibility = View.GONE
        binding!!.includeView.ivEditProfile.visibility = View.VISIBLE
        binding!!.includeView.toolbatTitle.setText("Notice Board")
        val transaction = supportFragmentManager.beginTransaction()
        //  transaction.addToBackStack(null)
        transaction.replace(R.id.frame_layout, newsFeedFragment)
        transaction.commit()
        binding!!.includeView.ivEditProfile.setOnClickListener {

            var intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra("comingFrom", "editProfile")
            intent.putExtra("postedByMail", sharedPref!!.getString(PreferenceKeys.EMAIL, "")!!)
            startActivityForResult(intent, 205)

            //Toast.makeText(this,"Coming Soon",Toast.LENGTH_LONG).show()

        }
        userImage = sharedPref!!.getString(PreferenceKeys.USER_IMAGE, "")!!
        Glide.with(this).load(userImage).placeholder(R.drawable.user).error(R.drawable.user)
            .into(binding!!.includeView.ivEditProfile)
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
