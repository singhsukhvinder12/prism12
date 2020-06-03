package com.e.seasianoticeboard.view.core.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.seasianoticeboard.App
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.adapter.ThisThatAdapter
import com.e.seasianoticeboard.callbacks.ChoiceCallback
import com.e.seasianoticeboard.model.input.AddChoiceInput
import com.e.seasianoticeboard.model.output.AddChoiceResponse
import com.e.seasianoticeboard.model.output.ChoiceResponse
import com.e.seasianoticeboard.presenter.ChoicePresenter
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.utils.PrefStore
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.view.core.auth.HobbiesActivity


class ChoiceFragment : Fragment(),ChoiceCallback, View.OnClickListener {

    var thisThatAdapter: ThisThatAdapter? = null
    var receylerView: RecyclerView? = null
    var userId = ""
    var presenter: ChoicePresenter?=null
    var btnSubmit:Button?=null
    var sharedPref: PrefStore? = null
    var dialog: Dialog? = null

    var input:AddChoiceInput?=null
    var arrayList: ArrayList<ChoiceResponse.ResultDataList>? = null
    var choiceAnsArray:ArrayList<AddChoiceInput.ChoiceQuestionAnswersList>?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_this_that, container, false)
        receylerView = view.findViewById(R.id.rvRecyclerView)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        btnSubmit!!.setOnClickListener(this)
        arrayList = ArrayList()
        sharedPref = PrefStore(this.activity!!)

        input=AddChoiceInput()
        choiceAnsArray= ArrayList()
        if( this.arguments!!.getSerializable("choiceList")!=null){
            arrayList = this.arguments!!.getSerializable("choiceList") as ArrayList<ChoiceResponse.ResultDataList>
        }
        userId=arguments!!.getString("userId")!!
        presenter=ChoicePresenter(this)
        if(sharedPref!!.getString(PreferenceKeys.USER_ID,"").equals(userId)){
            btnSubmit!!.visibility=View.VISIBLE
        } else{
            btnSubmit!!.visibility=View.GONE
        }
        setAdapter()
        return view
    }

    override fun onResume() {
        super.onResume()

    }

    fun setAdapter() {
        if(arrayList!=null) {
            val mLayoutManager = LinearLayoutManager(activity)
            receylerView!!.layoutManager = mLayoutManager
            thisThatAdapter =
                ThisThatAdapter(this@ChoiceFragment, arrayList, choiceAnsArray, userId)
            receylerView!!.adapter = thisThatAdapter
        }
    }

    fun choice(
        option: ArrayList<AddChoiceInput.ChoiceQuestionAnswersList>,
        position: Int,
        selected: String
    ) {
        input=AddChoiceInput()
        input!!.UserId=userId
        input!!.ChoiceQuestionAnswers=option
        arrayList!!.get(position).Selected=selected
        (activity as HobbiesActivity?)!!.myMethod(arrayList!!)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onSuccess(body: AddChoiceResponse) {
     Toast.makeText(activity,""+body.Message,Toast.LENGTH_LONG).show()
        (activity as HobbiesActivity?)!!.progressHide()

    }

    override fun onError() {
        (activity as HobbiesActivity?)!!.progressHide()

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnSubmit->{
                presenter!!.getData(input!!)
                (activity as HobbiesActivity?)!!.progressDialog()
            }
        }
    }

}
