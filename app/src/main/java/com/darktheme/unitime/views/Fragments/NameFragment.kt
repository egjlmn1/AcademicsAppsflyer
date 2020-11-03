package com.darktheme.unitime.views.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.darktheme.unitime.R
import com.darktheme.unitime.views.Activities.StartActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class NameFragment : Fragment() {

    var continueButton: Button? = null
    var editText: TextInputEditText? = null
    var editTextLayout: TextInputLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_name, container, false)

        editText = root.findViewById(R.id.edittext1)
        editTextLayout = root.findViewById(R.id.edittext_layout)
        continueButton = root.findViewById<Button>(R.id.continue_btn)

        val activity = requireActivity() as StartActivity
        editText!!.setText(activity.name)

        editText!!.addTextChangedListener(textWatcher)

        continueButton!!.setOnClickListener {
            if (!editText!!.text.isNullOrEmpty()) {
                activity.navController!!.navigate(R.id.action_name_to_finish_register)
            } else {
                editTextLayout!!.setError("Enter your name")
            }
        }
        return root
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            (requireActivity() as StartActivity).name = s.toString()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}