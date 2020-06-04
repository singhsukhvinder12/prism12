package com.seasia.prism.core

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.core.content.res.ResourcesCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.util.PrefStore
import com.seasia.prism.util.Utils
import com.seasia.prism.util.UtilsFunctions
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.seasia.prism.core.newsfeed.displaynewsfeed.view.NewsFeedFragment
import com.wang.avi.AVLoadingIndicatorView
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseActivity : AppCompatActivity() {
    private var mFragmentManager: FragmentManager? = null

    var offset: Int? = 0
    var isLoading: Boolean? = false
    var isLastPage: Boolean? = false
    var mCurrentFragment: Fragment? = null
    var baseActivity: BaseActivity? = null
    var mSavedInstanceState: Bundle? = null
    protected var viewDataBinding: ViewDataBinding? = null
    var sharedPref: PrefStore? = null
    protected var parentLayout: View? = null
    private var progressDialog: Dialog? = null
    var dialog: Dialog? = null

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = this
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())

        sharedPref = PrefStore(this)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN )
        mSavedInstanceState = savedInstanceState

        initViews()
        mFragmentManager = supportFragmentManager

        //   Toast.makeText(this,""+sharedPref!!.getString(DEVECE_TOKEN,""),Toast.LENGTH_LONG).show()
    }


    fun showFragment(fragment: Fragment, tag: String) {
        val fm = this.getSupportFragmentManager();
        for (i in 0 until fm.getBackStackEntryCount()) {
            if (getCurrentFragment() is NewsFeedFragment) {

            } else {
                fm.popBackStack()
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }


    private fun getCurrentFragment(): BaseFragment? {
        return supportFragmentManager
            .findFragmentById(R.id.frame_layout) as BaseFragment?
    }

    fun showDialog() {
        if (UtilsFunctions.isNetworkAvailable(App.app)) {
            try {
                runOnUiThread(Thread(Runnable() {
                    run() {
                        if (dialog == null) {
                            dialog = Dialog(this)
                            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialog!!.setCancelable(false)
                            dialog!!.getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog!!.setContentView(R.layout.dialog_progress_layout)
                            dialog!!.show()
                        }
                    }
                }));

            } catch (e: java.lang.Exception) {

            }
        }
    }

    fun hideDialog() {
            try {
                runOnUiThread(Thread(Runnable() {
                    run() {
                        dialog!!.dismiss()
                        dialog = null
                    }
                }));
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

    fun hideKeyboard(){
        try {
            var imm =getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager;
            imm.hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(),  0);

        }
        catch (e:Exception){

        }
    }


    abstract fun getLayoutId(): Int

    abstract fun initViews()

    fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }

    /**
     * @return toolbar title Name
     */
//  protected abstract String getHeaderTitle();
    override fun onDestroy() {
        super.onDestroy()
        try {
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * To get android phone ID
     *
     * @return String
     */
    fun getDeviceId(): String {
        return Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    /**
     * This method is used to replace fragment .
     *
     * @param newFragment :replace an existing fragment with new fragment.
     * @param args        :pass bundle classLiveList fron one fragment to another fragment
     */
    fun replaceFragment(frameLayout: Int, newFragment: Fragment, args: Bundle?) {
        val manager = supportFragmentManager
        manager.popBackStack()
        if (args != null)
            newFragment.arguments = args
        val transaction = manager.beginTransaction()
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(frameLayout, newFragment)
        transaction.addToBackStack(null)
        // Commit the transaction
        transaction.commit()
    }

    fun replaceFragment(frameLayout: Int, carouselFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(frameLayout, carouselFragment)
            .commit()
    }

    /**
     * This method is used to replace fragment .
     *
     * @param newFragment :replace an existing fragment with new fragment.
     * @param args        :pass bundle classLiveList fron one fragment to another fragment
     */
    fun addFragment(frameLayout: Int, newFragment: Fragment, args: Bundle?) {
        val manager = supportFragmentManager
        // manager.popBackStack();
        if (args != null)
            newFragment.arguments = args
        val transaction = manager.beginTransaction()
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(frameLayout, newFragment)
        transaction.addToBackStack(null)
        // Commit the transaction
        transaction.commit()
    }


    protected open fun loadMoreItems() {
        /* Pagination */
    }


    fun getmCurrentFragment(): Fragment? {
        return mCurrentFragment
    }

    fun setmCurrentFragment(mCurrentFragment: Fragment) {
        this.mCurrentFragment = mCurrentFragment
    }


    @SuppressLint("MissingPermission")
    fun checkInternet(activity: Context): Boolean {
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return if (netInfo != null && netInfo.isConnectedOrConnecting) {
            true
        } else if (netInfo != null && (netInfo.state == NetworkInfo.State.DISCONNECTED || netInfo.state == NetworkInfo.State.DISCONNECTING || netInfo.state == NetworkInfo.State.SUSPENDED || netInfo.state == NetworkInfo.State.UNKNOWN)) {
            false
        } else {
            false
        }
    }

    fun enableLogs() {
        Utils.PRINT_LOGS = true
    }

    fun disableLogs() {
        Utils.PRINT_LOGS = false
    }

    fun printInfoLog(tag: String, message: String) {
        if (Utils.PRINT_LOGS)
            Log.i(tag, message)
    }

    fun printDebugLog(tag: String, message: String) {
        if (Utils.PRINT_LOGS)
            Log.d(tag, message)
    }

    fun printErrorLog(tag: String, message: String) {
        if (Utils.PRINT_LOGS)
            Log.e(tag, message)
    }

    //region SHOW_FRAGMENT

//endregion

    /*
     * Method to start progress dialog*/


    /*
    * Method to start progress dialog*/
    fun startProgressDialog() {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            try {
                progressDialog!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /*
     * Method to stop progress dialog*/
    fun stopProgressDialog() {
        try {
            if (progressDialog != null && progressDialog!!.isShowing)
                progressDialog!!.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLocalDate(format: String, milisec: String?, outputFormat: String?): String {
        val inputFormat = SimpleDateFormat(format, Locale.getDefault())
        val outputFormat1 = SimpleDateFormat(outputFormat, Locale.getDefault())

        //val tz = TimeZone.getTimeZone("Local")
        // inputFormat.timeZone = tz
        val date = inputFormat.parse(milisec)

        val tzLocal = TimeZone.getDefault()
        outputFormat1.timeZone = tzLocal
        return outputFormat1.format(date)


    }
}

