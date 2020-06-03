package com.e.seasianoticeboard.view.core.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.seasianoticeboard.App

import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.adapter.CorridorAdapter
import com.e.seasianoticeboard.callbacks.QuestionCallback
import com.e.seasianoticeboard.model.input.AskQuestionInput
import com.e.seasianoticeboard.model.output.AskQuestionResponse
import com.e.seasianoticeboard.model.output.QuestionResponse
import com.e.seasianoticeboard.presenter.QuestionsPresenter
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.utils.PrefStore
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.view.core.auth.HobbiesActivity

class CorridorFragment : Fragment(), QuestionCallback, View.OnClickListener {
    var thisThatAdapter: CorridorAdapter?=null
    var receylerView: RecyclerView?=null
    var presenter:QuestionsPresenter?=null
    var UserId=""
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
    }

    override fun onError() {
        (activity as HobbiesActivity?)!!.progressHide()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){

            R.id.btnSubmit->{
                if(thisThatAdapter!=null) {
                    var addQuestion = AskQuestionInput()
                    addQuestion.UserId = UserId
                    addQuestion!!.QuestionAnswers = thisThatAdapter!!.getAnswer()
                    presenter!!.getData(addQuestion)
                    (activity as HobbiesActivity?)!!.progressDialog()
                    (activity as HobbiesActivity?)!!.myQuestionList(choiceAnsArray!!)
                }
            }
        }
    }
}
