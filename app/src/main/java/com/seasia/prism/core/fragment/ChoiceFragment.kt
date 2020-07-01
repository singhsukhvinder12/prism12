package com.seasia.prism.core.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.adapter.ThisThatAdapter
import com.seasia.prism.callbacks.ChoiceCallback
import com.seasia.prism.model.input.AddChoiceInput
import com.seasia.prism.model.output.AddChoiceResponse
import com.seasia.prism.model.output.ChoiceResponse
import com.seasia.prism.presenter.ChoicePresenter
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.util.PrefStore
import com.seasia.prism.util.UtilsFunctions
import com.seasia.prism.core.ui.HobbiesActivity


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
                if (!UtilsFunctions.isNetworkAvailable(App.app)) {
                    UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
                    return
                }
                presenter!!.getData(input!!)
                (activity as HobbiesActivity?)!!.progressDialog()
            }
        }
    }

}
