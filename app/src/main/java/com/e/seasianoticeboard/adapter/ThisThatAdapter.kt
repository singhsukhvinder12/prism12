package com.e.seasianoticeboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.e.seasianoticeboard.R
import com.e.seasianoticeboard.model.input.AddChoiceInput
import com.e.seasianoticeboard.model.output.ChoiceResponse
import com.e.seasianoticeboard.util.PreferenceKeys
import com.e.seasianoticeboard.view.core.fragment.ChoiceFragment

class ThisThatAdapter(
    var context: ChoiceFragment,
    var arrayList: ArrayList<ChoiceResponse.ResultDataList>?,
    var choiceAnsArray: ArrayList<AddChoiceInput.ChoiceQuestionAnswersList>?,
    var userId: String
) : RecyclerView.Adapter<ThisThatAdapter.MyViewHolder>() {
var btnOneSelect=false
var btnTwoSelect=false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.this_that_adapter, parent, false)
        return MyViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.btnOne!!.setText(arrayList!!.get(position).Option1)
        holder.btnTwo!!.setText(arrayList!!.get(position).Option2)
        var input=AddChoiceInput.ChoiceQuestionAnswersList()
        choiceAnsArray!!.add(input)


      if(holder!!.btnOne!!.text.equals(arrayList!!.get(position).Selected)){
          btnOneSelect=true
          btnTwoSelect = false
          holder.btnOne!!.setBackgroundResource(R.drawable.gredent_color)
          holder.btnTwo!!.setBackgroundResource(R.drawable.delete_shape)
          var listInput=AddChoiceInput.ChoiceQuestionAnswersList()
          listInput.Option=arrayList!!.get(position).Option1
          listInput.ChoiceQuestionId=arrayList!!.get(position).ChoiceQuestionId
          choiceAnsArray!!.set(position,listInput)
          context.choice(choiceAnsArray!!,position,arrayList!!.get(position).Option1!!)
      }
      else if(holder!!.btnTwo!!.text.equals(arrayList!!.get(position).Selected)){
          btnTwoSelect=true
          btnOneSelect = false
          holder.btnTwo!!.setBackgroundResource(R.drawable.gredent_color)
          holder.btnOne!!.setBackgroundResource(R.drawable.delete_shape)
          var listInput=AddChoiceInput.ChoiceQuestionAnswersList()
          listInput.Option=arrayList!!.get(position).Option2
          listInput.ChoiceQuestionId=arrayList!!.get(position).ChoiceQuestionId
          choiceAnsArray!!.set(position,listInput)
          context.choice(choiceAnsArray!!, position, arrayList!!.get(position).Option2!!)
      }

        holder.btnOne!!.setOnClickListener {
            btnTwoSelect = false
            if(btnOneSelect==false) {
                btnOneSelect = true
                holder.btnOne!!.setBackgroundResource(R.drawable.gredent_color)
                holder.btnTwo!!.setBackgroundResource(R.drawable.delete_shape)
                var listInput = AddChoiceInput.ChoiceQuestionAnswersList()
                listInput.Option = arrayList!!.get(position).Option1
                listInput.ChoiceQuestionId = arrayList!!.get(position).ChoiceQuestionId
                choiceAnsArray!!.set(position, listInput)
                context.choice(choiceAnsArray!!, position, arrayList!!.get(position).Option1!!)
            } else{
                btnOneSelect = false
                holder.btnOne!!.setBackgroundResource(R.drawable.delete_shape)
                holder.btnTwo!!.setBackgroundResource(R.drawable.delete_shape)
                var listInput = AddChoiceInput.ChoiceQuestionAnswersList()
                listInput.Option = ""
                listInput.ChoiceQuestionId = arrayList!!.get(position).ChoiceQuestionId
                choiceAnsArray!!.set(position, listInput)
                context.choice(choiceAnsArray!!, position, "")
            }

        }
        holder.btnTwo!!.setOnClickListener {
            btnOneSelect = false
            if(btnTwoSelect==false) {
                btnTwoSelect = true
                holder.btnOne!!.setBackgroundResource(R.drawable.delete_shape)
                holder.btnTwo!!.setBackgroundResource(R.drawable.gredent_color)
                var listInput = AddChoiceInput.ChoiceQuestionAnswersList()
                listInput.Option = arrayList!!.get(position).Option2
                listInput.ChoiceQuestionId = arrayList!!.get(position).ChoiceQuestionId
                choiceAnsArray!!.set(position, listInput)
                context.choice(choiceAnsArray!!, position, arrayList!!.get(position).Option2!!)
            } else{
                holder.btnOne!!.setBackgroundResource(R.drawable.delete_shape)
                holder.btnTwo!!.setBackgroundResource(R.drawable.delete_shape)
                var listInput = AddChoiceInput.ChoiceQuestionAnswersList()
                listInput.Option = ""
                listInput.ChoiceQuestionId = arrayList!!.get(position).ChoiceQuestionId
                choiceAnsArray!!.set(position, listInput)
                context.choice(choiceAnsArray!!, position, "")
                btnTwoSelect = false
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    inner class MyViewHolder(
        itemView: View,
        var context: ChoiceFragment) : RecyclerView.ViewHolder(itemView) {
        var btnOne: Button? = null
        var btnTwo: Button? = null

        init {
            btnOne = itemView.findViewById(R.id.btnSelected)
            btnTwo = itemView.findViewById(R.id.btnUNSelected)

            if(context.sharedPref!!.getString(PreferenceKeys.USER_ID,"").equals(userId)){
                btnOne!!.isEnabled=true
                btnTwo!!.isEnabled=true
            }else{
                btnOne!!.isEnabled=false
                btnTwo!!.isEnabled=false
            }
        }
    }
}
