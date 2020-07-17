package com.seasia.prism.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.seasia.prism.R
import com.seasia.prism.model.input.AddChoiceInput
import com.seasia.prism.model.output.ChoiceResponse
import com.seasia.prism.util.PreferenceKeys
import com.seasia.prism.core.fragment.ChoiceFragment

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
        input.Option=""
        input.ChoiceQuestionId=arrayList!!.get(position).ChoiceQuestionId
        choiceAnsArray!!.add(input)


      if(holder!!.btnOne!!.text.equals(arrayList!!.get(position).Selected)){
          arrayList!!.get(position).selectOne=true
          arrayList!!.get(position).selectTwo =
              false
          holder.btnOne!!.setBackgroundResource(R.drawable.gredent_color)
          holder.btnTwo!!.setBackgroundResource(R.drawable.delete_shape)
          var listInput=AddChoiceInput.ChoiceQuestionAnswersList()
          listInput.Option=arrayList!!.get(position).Option1
          listInput.ChoiceQuestionId=arrayList!!.get(position).ChoiceQuestionId
          choiceAnsArray!!.set(position,listInput)
          context.choice(choiceAnsArray!!,position,arrayList!!.get(position).Option1!!)
      }
      else if(holder!!.btnTwo!!.text.equals(arrayList!!.get(position).Selected)){
          arrayList!!.get(position).selectTwo=true
          arrayList!!.get(position).selectOne = false
          holder.btnTwo!!.setBackgroundResource(R.drawable.gredent_color)
          holder.btnOne!!.setBackgroundResource(R.drawable.delete_shape)
          var listInput=AddChoiceInput.ChoiceQuestionAnswersList()
          listInput.Option=arrayList!!.get(position).Option2
          listInput.ChoiceQuestionId=arrayList!!.get(position).ChoiceQuestionId
          choiceAnsArray!!.set(position,listInput)
          context.choice(choiceAnsArray!!, position, arrayList!!.get(position).Option2!!)
      }

        holder.btnOne!!.setOnClickListener {
            arrayList!!.get(position).selectTwo = false
            if(arrayList!!.get(position).selectOne==false) {
                arrayList!!.get(position).selectOne = true
                holder.btnOne!!.setBackgroundResource(R.drawable.gredent_color)
                holder.btnTwo!!.setBackgroundResource(R.drawable.delete_shape)
                var listInput = AddChoiceInput.ChoiceQuestionAnswersList()
                listInput.Option = arrayList!!.get(position).Option1
                listInput.ChoiceQuestionId = arrayList!!.get(position).ChoiceQuestionId
                choiceAnsArray!!.set(position, listInput)
                context.choice(choiceAnsArray!!, position, arrayList!!.get(position).Option1!!)
            } else{
                arrayList!!.get(position).selectOne = false
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
            arrayList!!.get(position).selectOne = false
            if(arrayList!!.get(position).selectTwo==false) {
                arrayList!!.get(position).selectTwo = true
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
                arrayList!!.get(position).selectTwo = false
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
