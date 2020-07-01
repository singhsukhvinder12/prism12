package com.seasia.prism.newsfeed.displaynewsfeed.view

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.adapter.MentionAdapter
import com.seasia.prism.callbacks.SearchUsersCallback
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.databinding.ActivityCommentBinding
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
    var charactorCountString = ""
    var postId = ""
    var searchPresenter: MentionUsersPresenter? = null

    var commentCount = ""
    var searchUsers: ArrayList<SearchResponse.ResultDataList>? = null
    var deleteCommentPosition = 0
    var commentPresenter: CommentPresenter? = null
    var position = "";
    var tagArrayList: ArrayList<String>? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_comment
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityCommentBinding
        binding!!.btnSend.setOnClickListener(this)
        binding!!.includeView.toolbatTitle.setText("Comment")
        binding!!.includeView.ivBack.setOnClickListener(this)
        userId = sharedPref!!.getString(PreferenceKeys.USER_ID, "")!!
        searchUsers = ArrayList()
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
            commentPresenter!!.getComment(postId!!)

        }
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


        var list = ArrayList<String>()

        for (i in 0..tagArrayList!!.size - 1) {
            list.add(tagArrayList!!.get(i)!!)
        }


//        if (list.size == 0) {
//            list.add("0")
//        }

        commentInput = CommentInput()
        commentInput!!.CommentId = ""
        commentInput!!.PostId = postId
        commentInput!!.CommentedBy = userId
        commentInput!!.Comment = binding!!.etComment.text.toString().trim()
        commentInput!!.AllFiles = ""
        commentInput!!.TagIds = list
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
                }

                else {

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
            commentCount = sendComment.ResultData!!
            showDialog()
            commentPresenter!!.getComment(postId)
        }
    }

    override fun onSuccess(body: ArrayList<SearchResponse.ResultDataList>?) {
        setUserList(body)
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


                if (!TextUtils.isEmpty(comment)) {

                    val start: Int = binding!!.etComment.getSelectionStart()
                    val lastSpace: Int = binding!!.etComment.getText().lastIndexOf(" ", start)

                    var singleWord = ""
                    val myText: String = binding!!.etComment.getText().toString()
                    if (lastSpace == -1) {
                        singleWord = myText.substring(0, start)
                    } else {
                        singleWord = myText.substring(lastSpace, start)
                    }

                    if (singleWord.contains("@")) {
                        if (singleWord.length > 1) {
                            charactorCountString = singleWord

                            //     Toast.makeText(this@CommentActivity, "text:  "+singleWord, Toast.LENGTH_LONG).show()

//                            val index = comment.indexOf("@")
//                            Log.e("index--", index.toString() + "")
//                            val stringTokenizer =
//                                StringTokenizer(comment, "@")
//                            var token: String? = null
//                            while (stringTokenizer.hasMoreTokens()) {
//                                token = stringTokenizer.nextToken()
//                             }
//                            val index = singleWord.indexOf("@")
//                            Log.e("index--", index.toString() + "")
                            val stringTokenizer = StringTokenizer(singleWord, "@")
                            var token: String? = null
                            while (stringTokenizer.hasMoreTokens()) {
                                token = stringTokenizer.nextToken()
                            }

                            if (token != null && !token!!.contains(" ")) {
                                searchPresenter = MentionUsersPresenter(this@CommentActivity)
                                searchPresenter!!.getData(searchInput(0, token))
                            } else {
                                binding!!.mentionRecyclerview.visibility = View.GONE
                            }
                        }
                    } else {
                        binding!!.mentionRecyclerview.visibility = View.GONE
                    }
                } else {
                    binding!!.mentionRecyclerview.visibility = View.GONE
                }
            }
        })
    }


    fun setUserList(body: ArrayList<SearchResponse.ResultDataList>?) {

//        var arrayList = ArrayList<String>()
//        for (i in 0..body!!.size - 1) {
//            arrayList.add(body.get(i).UserName!!)
//        }
        setMentionAdapterData(body)

    }


    fun setMentionAdapterData(arrayList: ArrayList<SearchResponse.ResultDataList>?) {
        if (arrayList!!.size > 0) {
            binding!!.mentionRecyclerview.visibility = View.VISIBLE
            val mLayoutManager = LinearLayoutManager(this)
            mLayoutManager.setStackFromEnd(true);
            binding!!.mentionRecyclerview.layoutManager = mLayoutManager
            mentionAdapter = MentionAdapter(this@CommentActivity, arrayList)
            binding!!.mentionRecyclerview.adapter = mentionAdapter
        } else {
            binding!!.mentionRecyclerview.visibility = View.GONE
        }
    }


    fun selectedText(text: String, userId: String?) {
//        var currentText=binding!!.etComment.getText().toString()
//
//        var lastIndex = currentText.lastIndexOf(" ");
//        currentText = currentText.substring(0, lastIndex);
//
//        binding!!.etComment.setText(currentText+" "+text)
//
//        binding!!.etComment.setSelection(binding!!.etComment.length());


        //   val start1: Int = binding!!.etComment.getSelectionStart()
//        val myText: String = binding!!.etComment.getText().toString()
//        val singleWord = myText.substring(pp1, start1)
        //binding!!.etComment.getText().toString().replace(singleWord, "")


        tagArrayList!!.add(userId!!)


        val start: Int = binding!!.etComment.getSelectionStart()
        val lastSpace: Int = binding!!.etComment.getText().lastIndexOf(" ", start)
        val endPo: Int = binding!!.etComment.getSelectionStart()

//            binding!!.etComment.getText().insert(start-charactorCount, text);

        var len = text.length
        var finalLen = lastSpace + len

        var text =
            binding!!.etComment.getText().toString().replace(charactorCountString, " @" + text)
//            binding!!.etComment.getText().toString().replace(charactorCountString, "<font color='#000000'>"+" @" +text+"</font>")
        binding!!.etComment.setText(text)


        binding!!.etComment.setSelection(finalLen + 2);


//        Toast.makeText(this@CommentActivity, "" + start+"  "+endPo, Toast.LENGTH_LONG).show()


//        val start: Int = binding!!.etComment.getSelectionStart()
//        val myText: String = binding!!.etComment.getText().toString()
//        val singleWord = myText.substring(pp1, start)
//
//
//        var finalText = binding!!.etComment.getText().toString().replace(singleWord, "")
//
//
//        binding!!.etComment.setText(finalText);
//
//        binding!!.mentionRecyclerview.visibility = View.GONE


//        val end: Int = binding!!.etComment.getSelectionEnd()
//        val selectedStr: String = binding!!.etComment.getText().toString().substring(start, end)
//        binding!!.etComment.setText(
//            binding!!.etComment.getText().toString().replace(selectedStr, "")
//        );

        // Toast.makeText(this@CommentActivity, "" + start, Toast.LENGTH_LONG).show()

        //   binding!!.mentionRecyclerview.visibility = View.GONE


    }


}


