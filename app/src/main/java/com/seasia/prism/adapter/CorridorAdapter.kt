package com.seasia.prism.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seasia.prism.R
import com.seasia.prism.core.fragment.CorridorFragment
import com.seasia.prism.model.input.AskQuestionInput
import com.seasia.prism.model.output.QuestionResponse
import com.seasia.prism.util.PreferenceKeys


class CorridorAdapter(
    var context: CorridorFragment,
    var choiceAnsArray: ArrayList<QuestionResponse.ResultDataList>?,
    var addAnswer: ArrayList<AskQuestionInput.QuestionAnswersList>?,
    var userId: String
) : RecyclerView.Adapter<CorridorAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.corridor_adapter_layout, parent, false)
        return MyViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {


        var num=position+1
        holder.tvQuestion!!.setText("Q:"+num+" "+ choiceAnsArray!!.get(position).Question)
        holder.tvAnswer!!.setText(choiceAnsArray!!.get(position).Answer)
        holder.tvAnswerAnouter!!.setText("Ans: "+choiceAnsArray!!.get(position).Answer)

            //holder.tvAnswer!!.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

            try {
                context.activity!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                context.activity!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            }catch (e:Exception){

            }

        holder.tvAnswer!!.visibility=View.VISIBLE
        holder.tvAnswerAnouter!!.visibility=View.GONE


        if(context.sharedPref!!.getString(PreferenceKeys.USER_ID,"").equals(userId)){
            holder.ansLayout!!.visibility=View.VISIBLE
            holder.tvAnswerAnouter!!.visibility=View.GONE
        } else{
            holder.ansLayout!!.visibility=View.GONE
            holder.tvAnswerAnouter!!.visibility=View.VISIBLE
        }

        holder!!.tvAnswer!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                choiceAnsArray!!.get(position).Answer=  holder!!.tvAnswer!!.text.toString().trim()
            }
        })




//            holder.rl_main!!.setOnTouchListener(object : View.OnTouchListener {
//
//                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                    var action = event!!.getAction();
//                    when (action) {
//                        MotionEvent.ACTION_DOWN -> {
//                            holder.rl_main!!.requestDisallowInterceptTouchEvent(true);
//                            // Disable touch on transparent view
//                            return false
//                        }
//
//                        MotionEvent.ACTION_UP -> {
//                            holder.rl_main!!.requestDisallowInterceptTouchEvent(false);
//                            return true
//                        }
//
//                        MotionEvent.ACTION_MOVE -> {
//                            holder.rl_main!!.requestDisallowInterceptTouchEvent(true);
//                            return false
//                        }
//                    }
//                    return true
//                }
//
//
//            });


           holder.tvAnswer!!.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    if (v.id ===   R.id.et_ansswer) {
                        v.parent.requestDisallowInterceptTouchEvent(true)
                        when (event.action and MotionEvent.ACTION_MASK) {
                            MotionEvent.ACTION_UP -> v.parent
                                .requestDisallowInterceptTouchEvent(false)
                        }
                    }
                    return false
                }
            })

        }catch (e:Exception){

        }

    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return choiceAnsArray!!.size
    }

    inner class MyViewHolder(
        itemView: View,
        var context: CorridorFragment
    ) : RecyclerView.ViewHolder(itemView) {
        var tvQuestion: TextView? = null
        var tvAnswer: EditText? = null
        var ansLayout: RelativeLayout? = null
        var tvAnswerAnouter: TextView? = null

        init {
            tvQuestion = itemView.findViewById(R.id.txtQuestion)
            tvAnswer = itemView.findViewById(R.id.et_ansswer)
            tvAnswerAnouter = itemView.findViewById(R.id.et_another_user)
            ansLayout = itemView.findViewById(R.id.ans_parent)
        }
    }


    fun getAnswer():ArrayList<AskQuestionInput.QuestionAnswersList>{

        addAnswer= ArrayList()
        for (i in 0..choiceAnsArray!!.size-1){
            var input=AskQuestionInput.QuestionAnswersList()
            input.Answer=choiceAnsArray!!.get(i).Answer

            input.QuestionId=choiceAnsArray!!.get(i).QuestionId
            addAnswer!!.add(input)
        }

        return addAnswer!!
    }
}
