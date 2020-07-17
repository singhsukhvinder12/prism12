package com.seasia.prism.newsfeed.displaynewsfeed.view

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.adapter.MentionAdapter
import com.seasia.prism.callbacks.SearchUsersCallback
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.databinding.ActivityCommentBinding
import com.seasia.prism.mention.Tokenizer
import com.seasia.prism.model.input.SearchInput
import com.seasia.prism.model.output.SearchResponse
import com.seasia.prism.newsfeed.displaynewsfeed.adapter.CommentAdapter
import com.seasia.prism.newsfeed.displaynewsfeed.callback.CommentCallback
import com.seasia.prism.newsfeed.displaynewsfeed.model.*
import com.seasia.prism.newsfeed.displaynewsfeed.presenter.CommentPresenter
import com.seasia.prism.presenter.MentionUsersPresenter
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.util.UtilsFunctions
import java.util.*
import kotlin.collections.ArrayList


class CommentActivity : BaseActivity(), View.OnClickListener,
    CommentCallback, SearchUsersCallback {
    var commentInput: CommentInput? = null
    var binding: ActivityCommentBinding? = null
    lateinit var adapter: CommentAdapter
    lateinit var mentionAdapter: MentionAdapter
    var commentList: ArrayList<GetCommentResponse.ResultDataModel.LstgetCommentViewModelList>? =
        null
    var postedById = ""
    var userId = ""
    var charactorCount = 0
    var tokenizer: Tokenizer? = null

    var charactorCountString = ""
    var postId = ""
    var singleSelection = "false"
    var searchPresenter: MentionUsersPresenter? = null

    var commentCount = ""
    var searchUsers: ArrayList<SearchResponse.ResultDataList>? = null
    var deleteCommentPosition = 0
    var commentPresenter: CommentPresenter? = null
    var position = "";
    var userList = ArrayList<SearchResponse.ResultDataList>()
    var tagArrayList: ArrayList<String>? = null
    var mLayoutManager:LinearLayoutManager?=null
    override fun getLayoutId(): Int {
        return R.layout.activity_comment
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityCommentBinding
        binding!!.btnSend.setOnClickListener(this)
        binding!!.includeView.toolbatTitle.setText("Comments")
        binding!!.includeView.ivBack.setOnClickListener(this)
        userId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")!!
        searchUsers = ArrayList()
        setMentionAdapterData(searchUsers)

        tagArrayList = ArrayList()
        commentPresenter =
            CommentPresenter(
                this
            )
        if (intent.getStringExtra("postId") != null) {
            postedById = intent.getStringExtra("PostedById")!!
            postId = intent.getStringExtra("postId")!!
            position = intent.getStringExtra("position")!!
            //
            if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                return
            }
            showDialog()
            commentPresenter!!.getComment(postId)

        }
         mLayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding!!.mentionRecyclerview.layoutManager = mLayoutManager
        setAdapterData()
        searchUer()
    }


    fun searchInput(itemCount: Int, search: String): SearchInput {
        var searchListInput = SearchInput()
        searchListInput.Search = search
        searchListInput.Skip = itemCount.toString()
        searchListInput.PageSize = "10"
        searchListInput.SortColumnDir = ""
        searchListInput.SortColumn = ""
        searchListInput.ParticularId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")
        searchListInput.SortColumn = ""
        return searchListInput
    }


    override fun onBackPressed() {
        var intent = Intent()
        intent.putExtra("position", position)
        intent.putExtra("updatedCount", commentCount.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun getImputData(): CommentInput {

//
//        var list = ArrayList<String>()
//
//        for (i in 0..tagArrayList!!.size - 1) {
//            list.add(tagArrayList!!.get(i)!!)
//        }

        if (!binding!!.etComment.text.toString().trim().contains("@" + mentionUser)) {
            tagArrayList = ArrayList()
        }

        commentInput = CommentInput()
        commentInput!!.CommentId = ""
        commentInput!!.PostId = postId
        commentInput!!.CommentedBy = userId
        commentInput!!.Comment = binding!!.etComment.text.toString().trim()
        commentInput!!.AllFiles = ""

        if (tagArrayList!!.size > 0) {
            commentInput!!.TagIds = tagArrayList
        }
        commentInput!!.DeletedTagIds = "0"
        return commentInput!!
    }

    fun setAdapterData() {
        if (commentList != null) {
            val mLayoutManager = LinearLayoutManager(this)
            mLayoutManager.setStackFromEnd(true);
            binding!!.rvPublic.layoutManager = mLayoutManager
            adapter = CommentAdapter(this@CommentActivity, commentList!!)
            binding!!.rvPublic.adapter = adapter
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnSend -> {
                if (binding!!.etComment.text.trim().isEmpty()) {
                    Toast.makeText(this, "Please enter your comment", Toast.LENGTH_LONG).show()
                } else {

                    if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                        UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                        return
                    }
                    showDialog()
                    commentPresenter!!.sendComment(getImputData()!!)

                    if (tagArrayList!!.size > 0) {
                        tagArrayList!!.clear()
                    }

                    binding!!.etComment.setText("")
                }
            }
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }


    fun deleteComment(commentId: String?, position: Int, userId: String?) {
        var input = DeleteCommentInput()
        input.CommentId = commentId
        input.PostId = postId
        input.UserId = userId
        showDialog()
        deleteCommentPosition = position
        commentPresenter =
            CommentPresenter(
                this
            )
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        commentPresenter!!.deleteComment(input!!)

    }

    override fun getCommentList(commentListData: GetCommentResponse) {
        hideDialog()
        if (commentListData.ResultData != null) {
            commentList = commentListData.ResultData!!.lstgetCommentViewModel!!
            setAdapterData()
        }
    }

    override fun deleteComment(deleteComment: DeleteCommentResponse) {
        hideDialog()
        commentList!!.removeAt(deleteCommentPosition)
        adapter.notifyDataSetChanged()

        if (deleteComment != null) {
            commentCount = deleteComment.ResultData!!
        }
    }

    override fun sendComment(sendComment: CommentResponse) {
        hideDialog()
        if (sendComment.ResultData != null) {
            singleSelection = "false"
            commentCount = sendComment.ResultData!!
            showDialog()
            commentPresenter!!.getComment(postId)
        }
    }

    override fun onSuccess(body: ArrayList<SearchResponse.ResultDataList>?) {
          // setMentionAdapterData(body)
     //   if(body!!.size>0){
            mentionAdapter.setData(body!!)
//        }else{
//            binding!!.mentionRecyclerview.visibility = View.GONE
//        }

    }

    override fun onError() {
        hideDialog()
    }


    fun searchUer() {
        binding!!.etComment.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                val comment: String = binding!!.etComment.getText().toString()

                if (!comment.contains("@")) {
                    singleSelection = "false"
                }


                if (singleSelection.equals("false")) {

                    if (!TextUtils.isEmpty(comment)) {

                        val start: Int = binding!!.etComment.getSelectionStart()

                        tokenizer = Tokenizer()

                        tokenizer!!.findTokens(p0.toString(), start)
                        var searchText = tokenizer!!.getCurrentWord(binding!!.etComment.text, start)


                        if (searchText.contains("@")) {
                            if (searchText.length > 1) {
                                charactorCountString = searchText

                                val stringTokenizer = StringTokenizer(searchText, "@")
                                var token: String? = null
                                while (stringTokenizer.hasMoreTokens()) {
                                    token = stringTokenizer.nextToken()
                                }

                                if (token != null && !token!!.contains(" ")) {
                                    binding!!.mentionRecyclerview.visibility = View.VISIBLE
                                    searchPresenter =
                                        MentionUsersPresenter(this@CommentActivity, userList)
                                    searchPresenter!!.getData(searchInput(0, token))
                                } else {
                                        binding!!.mentionRecyclerview.visibility = View.GONE
                                }
                            } else {
                                binding!!.mentionRecyclerview.visibility = View.VISIBLE
                                charactorCountString = searchText
                                searchPresenter = MentionUsersPresenter(this@CommentActivity, userList)
                                searchPresenter!!.getData(searchInput(0, ""))
                            }
                        } else {
                            runOnUiThread {
                                binding!!.mentionRecyclerview.visibility = View.GONE
                            }                        }
                    } else {
                        runOnUiThread {
                            binding!!.mentionRecyclerview.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }


    fun setMentionAdapterData(arrayList: ArrayList<SearchResponse.ResultDataList>?) {

        //   if (arrayList!!.size > 0) {


         runOnUiThread {

             //   mLayoutManager.setStackFromEnd(true);

             mentionAdapter = MentionAdapter(this@CommentActivity, arrayList)
             binding!!.mentionRecyclerview.adapter = mentionAdapter
         }

        //mentionAdapter.notifyDataSetChanged()

    }

    var mentionUser = ""
    fun selectedText(text: String, userId: String?) {
        mentionUser = text
        tagArrayList = ArrayList()
        tagArrayList!!.add(userId!!)
        singleSelection = "true"
        binding!!.mentionRecyclerview.visibility = View.GONE

        val start: Int = binding!!.etComment.getSelectionStart()
        val lastSpace: Int = binding!!.etComment.getText().lastIndexOf(" ", start)

        var text =
//            binding!!.etComment.getText().toString().replace(charactorCountString, " @" + text)

            binding!!.etComment.getText().toString().replace(charactorCountString, "<font color='#000000'><b>" + " @" + text + "</b></font>")


        binding!!.etComment.setText(Html.fromHtml(text))

        binding!!.etComment.setSelection(start);


    }


}


