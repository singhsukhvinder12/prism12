package com.seasia.prism.core.auth

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.seasia.prism.App
import com.seasia.prism.R
import com.seasia.prism.callbacks.HobbiesCallback
import com.seasia.prism.core.BaseActivity
import com.seasia.prism.core.fragment.ChoiceFragment
import com.seasia.prism.core.fragment.CorridorFragment
import com.seasia.prism.databinding.ActivityHobbiesBinding
import com.seasia.prism.model.output.ChoiceResponse
import com.seasia.prism.model.output.QuestionResponse
import com.seasia.prism.presenter.HobbiesPresenter
import com.seasia.prism.util.UtilsFunctions


class HobbiesActivity : BaseActivity(), View.OnClickListener, HobbiesCallback {

    var binding: ActivityHobbiesBinding? = null
    var tab1 = false
    var tab2 = false
    var leftIn = "2"
    var userId=""
    var fragmentManager:FragmentManager?=null
    var hobbiesPresenter: HobbiesPresenter? = null
    var choiceQuestion:ArrayList<ChoiceResponse.ResultDataList>?=null
    var questionList:ArrayList<QuestionResponse.ResultDataList>?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_hobbies
    }

    override fun initViews() {

        binding = viewDataBinding as ActivityHobbiesBinding
        binding!!.includeView.toolbatTitle.setText("Corridor Conversations")
        binding!!.includeView.ivBack.setOnClickListener { finish() }
        binding!!.btnCoridor.setOnClickListener(this)
        binding!!.btnThis.setOnClickListener(this)
        if(intent.getStringExtra("userId")!=null){
            userId=  intent.getStringExtra("userId")
        }
        fragmentManager = supportFragmentManager
        tab1 = true
        choiceQuestion= ArrayList()
        hobbiesPresenter = HobbiesPresenter(this)
        if (!UtilsFunctions.isNetworkAvailable(App.app)) {
            UtilsFunctions.showToastError(App.app.getString(R.string.internet_error))
            return
        }
        showDialog()
        hobbiesPresenter!!.getData(userId)
        hobbiesPresenter!!.getQuestionData(userId)

    }



    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btn_this -> {
                if (tab1 == false) {
                    var fragment = ChoiceFragment()
                    leftIn = "0"
                    replaceFragmetn(fragment)
                    tab1 = true
                    tab2 = false
                    binding!!.btnThis.setBackgroundResource(R.color.colorPrimaryDarkMore)
                    binding!!.btnCoridor.setBackgroundResource(R.color.colorPrimary)
                }
            }
            R.id.btn_coridor -> {
                if (tab2 == false) {
                    leftIn = "1"
                    var fragment = CorridorFragment()
                    replaceFragmetn(fragment)
                    tab2 = true
                    tab1 = false
                    binding!!.btnThis.setBackgroundResource(R.color.colorPrimary)
                    binding!!.btnCoridor.setBackgroundResource(R.color.colorPrimaryDarkMore)
                }
            }
        }
    }

    fun replaceFragmetn(fragment: Fragment) {

        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()

        val bundle = Bundle()

        if(choiceQuestion!=null){
            bundle.putSerializable("choiceList", choiceQuestion)
        }

        bundle.putString("userId",userId)

        fragment.setArguments(bundle)
        if (leftIn.equals("0")) {
            if(choiceQuestion!=null){
                bundle.putSerializable("choiceList", choiceQuestion)
            }
            bundle.putString("userId",userId)
            fragmentTransaction.setCustomAnimations(
                R.anim.slide_left_in, R.anim.slide_right_out,
                R.anim.slide_left_in, R.anim.slide_right_out
            );
        } else if(leftIn.equals("1")){
            if(questionList!=null){
                bundle.putSerializable("questionListt", questionList)
            }
            bundle.putString("userId",userId)
            fragmentTransaction.setCustomAnimations(
                R.anim.slide_right_in, R.anim.slide_left_out,
                R.anim.slide_left_in, R.anim.slide_right_out
            );
        }
        fragmentTransaction.replace(R.id.frame_layout, fragment, "h")
        fragmentTransaction.addToBackStack("h")
        fragmentTransaction.commit()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSuccess(body: ChoiceResponse) {
        hideDialog()
        if(body.ResultData!=null){
            choiceQuestion=body.ResultData
            replaceFragmetn(ChoiceFragment())
        }
    }

    override fun onError() {
        hideDialog()
    }

    override fun onSuccessQuestion(body: QuestionResponse) {
        questionList=body.ResultData!!
        hideDialog()
    }

    fun myMethod(arrayList: ArrayList<ChoiceResponse.ResultDataList>) {
        choiceQuestion=arrayList
    }


    fun progressDialog() {
        showDialog()
    }
    fun progressHide() {
        hideDialog()
    }


    fun myQuestionList(arrayList: ArrayList<QuestionResponse.ResultDataList>) {
        questionList=arrayList
    }


}