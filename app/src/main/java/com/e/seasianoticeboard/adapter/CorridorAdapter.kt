package com.e.seasianoticeboard.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.model.input.AskQuestionInput
import com.e.seasianoticeboard.model.output.QuestionResponse
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.view.core.fragment.CorridorFragment

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
        var num=position+1
        holder.tvQuestion!!.setText("Q."+num+" "+ choiceAnsArray!!.get(position).Question)
        holder.tvAnswer!!.setText(choiceAnsArray!!.get(position).Answer)
        holder.tvAnswerAnouter!!.setText("Ans. "+choiceAnsArray!!.get(position).Answer)

//        holder.tvAnswer!!.visibility=View.VISIBLE
//        holder.tvAnswerAnouter!!.visibility=View.GONE


        if(context.sharedPref!!.getString(PreferenceKeys.USER_ID,"").equals(userId)){
            holder.tvAnswer!!.visibility=View.VISIBLE
            holder.tvAnswerAnouter!!.visibility=View.GONE
        } else{
            holder.tvAnswer!!.visibility=View.GONE
            holder.tvAnswerAnouter!!.visibility=View.VISIBLE
        }

        holder!!.tvAnswer!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                choiceAnsArray!!.get(position).Answer=  holder!!.tvAnswer!!.text.toString()
            }
        })

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
        var tvAnswerAnouter: TextView? = null

        init {
            tvQuestion = itemView.findViewById(R.id.txtQuestion)
            tvAnswer = itemView.findViewById(R.id.et_ansswer)
            tvAnswerAnouter = itemView.findViewById(R.id.et_another_user)
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
