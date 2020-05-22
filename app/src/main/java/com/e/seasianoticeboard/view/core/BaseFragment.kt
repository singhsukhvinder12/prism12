package com.e.seasianoticeboard.views.core

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.e.seasianoticeboard.utils.PrefStore
import com.e.seasianoticeboard.utils.Utils
import com.wang.avi.AVLoadingIndicatorView

/**
 * To initialize view,controls and replace Fragments
 * Created by jindaldipanshu on 7/2/2016.
 *
 * @version 1.0
 */
abstract class BaseFragment(var isBinding: Boolean) : Fragment() {
    var TAG = "BaseFragment"
    var baseActivity: BaseActivity? = null
    var mContext: Context? = null
    private var mView: View? = null
    protected var parentLayout: View? = null
    protected var viewDataBinding: ViewDataBinding? = null
    var sharedPref: PrefStore? = null
    companion object{
        @JvmStatic
        var dialog: Dialog? = null
    }


    var loader: AVLoadingIndicatorView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        baseActivity = context as BaseActivity
    }

    protected fun findViewById(id: Int): View {
        return mView!!.findViewById(id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromBundle(arguments)
    }

    abstract fun getLayoutId(): Int

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if (isBinding) {
            viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            sharedPref = PrefStore(mContext as Activity)
            return viewDataBinding!!.root
        } else {

            val rootView = inflater.inflate(getLayoutId(), container, false)
            sharedPref = PrefStore(mContext as Activity)
            return rootView
        }

        /* val rootView = inflater.inflate(R.layout.common_loader, container, false)*/

        //loader=   rootView.findViewById(R.id.load_more) as AVLoadingIndicatorView


    }
    
    fun showLoader() {
        //  loader!!.visibility=View.VISIBLE

    }

    fun hideLoader() {
        //  loader!!.visibility=View.GONE

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        initViews(view)
    }

    protected fun getDataFromBundle(bundle: Bundle?) {
        /* do functionality here */
    }

    protected abstract fun initViews(view: View)


    /**
     * This method is used to replace Child Fragment
     *
     * @param frameLayout
     * @param childFragment
     * @param bundle
     */
    fun replaceChildFragment(frameLayout: Int, childFragment: Fragment, bundle: Bundle?) {

        val transaction = childFragmentManager.beginTransaction()
        if (bundle != null) {
            childFragment.arguments = bundle
        }
        transaction.addToBackStack(null)
        try {
            transaction.replace(frameLayout, childFragment).commit()//R.ID.fragment_mainLayout
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    abstract fun getQuery(p0: String?)
}

