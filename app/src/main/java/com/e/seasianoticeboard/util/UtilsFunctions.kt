package com.e.seasianoticeboard.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.e.seasianoticeboard.App
import com.e.seasianoticeboard.R


object UtilsFunctions {


    public var TOKEN=""
    @JvmStatic
    fun showToastError(message: String?) {

        val toast = Toast.makeText(App.app, message, LENGTH_LONG) as Toast
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        val view = toast.view
        val group = toast.view as ViewGroup
        val messageTextView = group.getChildAt(0) as TextView
        messageTextView.textSize = 15.0f
        messageTextView.gravity = Gravity.CENTER
        toast.show()
    }

    @JvmStatic
    fun showToast(message: String?) {

        val toast = Toast.makeText(App.app, message, LENGTH_LONG) as Toast
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        val view = toast.view
        val group = toast.view as ViewGroup
        val messageTextView = group.getChildAt(0) as TextView
        messageTextView.textSize = 15.0f
        messageTextView.gravity = Gravity.CENTER
        toast.show()
    }

    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager
            .activeNetworkInfo.isConnectedOrConnecting
    }

    fun isNetworkConnected(): Boolean {
        val cm = App.app.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?
        activeNetwork = cm.activeNetworkInfo

        return if (activeNetwork != null && activeNetwork.isConnected) {
            activeNetwork.isConnected
            true
        } else {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            // Toast.makeText(MyApplication.getInstance().getApplicationContext(), R.string.internet_connection, Toast.LENGTH_SHORT).show();
            false
        }
    }

    @JvmStatic
    fun hideKeyBoard(view: View) {
        val imm = App.app
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
