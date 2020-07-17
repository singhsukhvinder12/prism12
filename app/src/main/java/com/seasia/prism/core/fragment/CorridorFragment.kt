package com.seasia.prism.core.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seasia.prism.App
import com.seasia.prism.MainActivity

import com.seasia.prism.R
import com.seasia.prism.adapter.CorridorAdapter
import com.seasia.prism.callbacks.QuestionCallback
import com.seasia.prism.model.input.AskQuestionInput
import com.seasia.prism.model.output.AskQuestionResponse
import com.seasia.prism.model.output.QuestionResponse
import com.seasia.prism.presenter.QuestionsPresenter
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.util.PrefStore
import com.seasia.prism.util.UtilsFunctions
import com.seasia.prism.core.ui.HobbiesActivity

class CorridorFragment : Fragment(), QuestionCallback, View.OnClickListener {
    var thisThatAdapter: CorridorAdapter?=null
    var receylerView: RecyclerView?=null
    var presenter:QuestionsPresenter?=null
    var UserId=""
    var btnPress=""
    var choiceAnsArray:ArrayList<QuestionResponse.ResultDataList>?=null
    var sharedPref: PrefStore? = null
    var btnSubmit:Button?=null
    var addAnswer: ArrayList<AskQuestionInput.QuestionAnswersList>?=null
    var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view=inflater.inflate(R.layout.fragment_corridor, container, false)

        if(this.arguments!!.getSerializable("questionListt")!=null){
            choiceAnsArray = this.arguments!!.getSerializable("questionListt") as ArrayList<QuestionResponse.ResultDataList>

        }
        sharedPref = PrefStore(this.activity!!)
        btnSubmit=view.findViewById(R.id.btnSubmit)
        btnSubmit!!.setOnClickListener(this)
        addAnswer= ArrayList()
        UserId=arguments!!.getString("userId")!!
        if(sharedPref!!.getString(PreferenceKeys.USER_ID,"").equals(UserId)){
            btnSubmit!!.visibility=View.VISIBLE
        } else{
            btnSubmit!!.visibility=View.GONE
        }

        presenter= QuestionsPresenter(this)
        receylerView=view.findViewById(R.id.rvRecyclerView)

        setAdapter()
        return view
    }

    fun setAdapter(){
        if(choiceAnsArray!=null) {
            val mLayoutManager = LinearLayoutManager(activity)
            receylerView!!.layoutManager = mLayoutManager
            thisThatAdapter =
                CorridorAdapter(this@CorridorFragment, choiceAnsArray, addAnswer, UserId)
            receylerView!!.adapter = thisThatAdapter
        }
    }


    override fun onSuccess(body: AskQuestionResponse) {
        (activity as HobbiesActivity?)!!.progressHide()
        Toast.makeText(activity,""+body.Message,Toast.LENGTH_LONG).show()
        if(btnPress.equals("yes")){
            var intent = Intent(activity, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            (activity as HobbiesActivity?)!!.finish()
        }

    }

    override fun onError() {
        (activity as HobbiesActivity?)!!.progressHide()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){

            R.id.btnSubmit->{
                if(thisThatAdapter!=null) {
                    memoriytDialog()
                }
            }
        }
    }

    fun memoriytDialog() {

        var dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.memory_dialog);
        dialog.setCanceledOnTouchOutside(false)
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent);
        val btnLogout = dialog.findViewById<TextView>(R.id.tv_delete)
        val btnCancel = dialog.findViewById<TextView>(R.id.tv_cancel)
        btnLogout.setOnClickListener {
            dialog.dismiss()
            val addQuestion = AskQuestionInput()
            addQuestion.UserId = UserId
            addQuestion.TypeId = "1"
            addQuestion.QuestionAnswers = thisThatAdapter!!.getAnswer()
            if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))

            } else{
                presenter!!.getData(addQuestion)
                (activity as HobbiesActivity?)!!.progressDialog()
                (activity as HobbiesActivity?)!!.myQuestionList(choiceAnsArray!!)
            }
            btnPress="yes"

        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
            val addQuestion = AskQuestionInput()
            addQuestion.UserId = UserId
            addQuestion.TypeId = "0"
            addQuestion.QuestionAnswers = thisThatAdapter!!.getAnswer()
            if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))

            } else{
                presenter!!.getData(addQuestion)
                (activity as HobbiesActivity?)!!.progressDialog()
                (activity as HobbiesActivity?)!!.myQuestionList(choiceAnsArray!!)
            }
            btnPress="no"

        }
        dialog.show()
    }
}
