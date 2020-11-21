package com.darktheme.unitime.views.Adapters

import android.content.Context
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.darktheme.unitime.R

class MySpinnerAdapter(context: Context, objects: List<SpinnerCheckBox>) :
    ArrayAdapter<SpinnerCheckBox>(context, 0, objects) {
    private val mContext: Context
    private val listState: ArrayList<SpinnerCheckBox>
    private val myAdapter: MySpinnerAdapter

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val listItem = LayoutInflater.from(mContext).inflate(R.layout.spinner_item,parent,false)
        val text = listItem!!.findViewById<TextView>(R.id.text)
        text.text = listState[position].title
        text.setTextColor(mContext.getColor(R.color.colorNormalText))

        val checkBox = listItem.findViewById<CheckBox>(R.id.checkbox)
        checkBox.isChecked = listState[position].selected
        checkBox.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean(listState[position].data, b).apply()
            listState[position].selected = b
        }
        return listItem
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = TextView(mContext)
        view.text = "Flairs"
        view.setTextColor(mContext.getColor(R.color.colorNormalText))
        return view
    }

    init {
        mContext = context
        listState = objects as ArrayList<SpinnerCheckBox>
        myAdapter = this
    }
}

class SpinnerCheckBox(var title: String?, var selected: Boolean, val data: String) {
    override fun toString(): String {
        return data
    }
}