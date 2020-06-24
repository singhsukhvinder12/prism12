package com.seasia.prism.imagecroper.crop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.naver.android.helloyako.imagecropsample.imageselect.ImageSelectFragment
import com.seasia.prism.R

class CroppedActivity : AppCompatActivity() {
    var fragmentManager: FragmentManager?=null
var imagePath=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cropped)

        if(intent.getStringExtra("imagePath")!=null){
            imagePath=intent.getStringExtra("imagePath")!!
        }
        replaceFragmetn(imagePath)
    }

    fun replaceFragmetn(imagePath: String) {
        var fragment=ImageSelectFragment()
        fragment.onSuccess(imagePath)
        fragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout12, fragment, "h")
        fragmentTransaction.addToBackStack("h")
        fragmentTransaction.commit()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}