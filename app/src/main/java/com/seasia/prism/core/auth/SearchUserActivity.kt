package com.seasia.prism.core.auth

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.adapter.SearchAdapter
import com.seasia.prism.callbacks.SearchUsersCallback
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.GetFeedResponse
import com.seasia.prism.core.newsfeed.displaynewsfeed.pagination.EndlessRecyclerViewScrollListenerImplementation
import com.seasia.prism.databinding.ActivitySearchUserBinding
import com.seasia.prism.model.input.SearchInput
import com.seasia.prism.model.output.SearchResponse
import com.seasia.prism.presenter.SearchPresenter
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.util.UtilsFunctions
import kotlinx.android.synthetic.main.fragment_news_feed.*

class SearchUserActivity : BaseActivity(), SearchUsersCallback,
    EndlessRecyclerViewScrollListenerImplementation.OnScrollPageChangeListener {
    var searchPresenter: SearchPresenter? = null
    var binding: ActivitySearchUserBinding? = null
    var adapter: SearchAdapter? = null
    var searchListInput: SearchInput? = null
    var endlessScrollListener: EndlessRecyclerViewScrollListenerImplementation? = null
    var searchItem=""
    var userList=ArrayList<SearchResponse.ResultDataList>()

    var searchList: ArrayList<SearchResponse.ResultDataList>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_search_user
    }

    override fun initViews() {
        binding = viewDataBinding as ActivitySearchUserBinding
        binding!!.includeView.toolbatTitle.setText("Users")
        binding!!.includeView.ivBack.setOnClickListener { finish() }
        userList= ArrayList()
        searchPresenter = SearchPresenter(this,userList!!)
        searchPresenter!!.getData(searchInput(0,"")!!)
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        showDialog()
        searchList = ArrayList()
        setAdapterData()

        binding!!.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchItem=p0.toString()
                if(searchItem.equals("")){
                    searchPresenter!!.getData(searchInput(0,searchItem!!))
                }
            }
        })

        binding!!.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                } else{
                    showDialog()
                    searchPresenter!!.getData(searchInput(0,searchItem!!))
                }
                return true
            }
        })
    }
    fun searchInput(itemCount: Int,search:String): SearchInput {
        searchListInput = SearchInput()
        searchListInput!!.Search = search
        searchListInput!!.Skip = itemCount.toString()
        searchListInput!!.PageSize = "10"
        searchListInput!!.SortColumnDir = ""
        searchListInput!!.SortColumn = ""
        searchListInput!!.ParticularId = sharedPref!!.getString(PreferenceKeys.USER_ID,"")
        searchListInput!!.SortColumn = ""
        return searchListInput!!
    }

    override fun onSuccess(body: ArrayList<SearchResponse.ResultDataList>?) {
        hideDialog()
        if (body != null) {

            if(body.size>0){
                binding!!.noRecordFound.visibility= View.GONE

            } else{
                binding!!.noRecordFound.visibility= View.VISIBLE
            }
            adapter!!.setData(body)
        }
    }

    override fun onError() {
        hideDialog()
    }

    fun setAdapterData() {
        if (searchList != null) {
            adapter = SearchAdapter(this@SearchUserActivity, searchList!!)
            val mLayoutManager = LinearLayoutManager(this)
            binding!!.rvPublic.layoutManager = mLayoutManager
            endlessScrollListener = EndlessRecyclerViewScrollListenerImplementation(
                mLayoutManager, this
            )
            endlessScrollListener?.setmLayoutManager(mLayoutManager)
            rvPublic.addOnScrollListener(endlessScrollListener!!)
            binding!!.rvPublic.adapter = adapter
        }
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        showDialog()
        searchPresenter!!.getData(searchInput(adapter!!.itemCount,""))
    }

}