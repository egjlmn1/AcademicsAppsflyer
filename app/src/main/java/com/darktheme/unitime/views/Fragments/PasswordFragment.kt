package com.darktheme.unitime.views.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
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


class PasswordFragment : Fragment() {

    var continueButton: Button? = null
    var editText1: TextInputEditText? = null
    var editText2: TextInputEditText? = null
    var editTextLayout1: TextInputLayout? = null
    var editTextLayout2: TextInputLayout? = null

    var valid = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_password, container, false)
        editText1 = root.findViewById<TextInputEditText>(R.id.edittext1)
        editText2 = root.findViewById<TextInputEditText>(R.id.edittext2)

        editTextLayout1 = root.findViewById(R.id.edittext1_layout)
        editTextLayout2 = root.findViewById(R.id.edittext2_layout)

        val activity = requireActivity() as StartActivity
        editText1!!.setText(activity.password1)
        editText2!!.setText(activity.password2)


        editText1!!.addTextChangedListener(textWatcher)
        editText2!!.addTextChangedListener(textWatcher)

        continueButton = root.findViewById<Button>(R.id.continue_btn)
        root.findViewById<Button>(R.id.continue_btn).setOnClickListener {
            if (valid) {
                activity.navController!!.navigate(R.id.action_password_to_name)
            } else {
                if (!editText1!!.text.isValidPassword()) {
                    editTextLayout1!!.setError("Password must be 6-20 characters")
                } else {
                    editTextLayout1!!.setError("")
                }
                if (!equals(editText1!!.text.toString(), editText2!!.text.toString())) {
                    editTextLayout2!!.setError("Enter the same password")
                } else {
                    editTextLayout2!!.setError("")
                }
            }
        }

        return root
    }
    fun CharSequence?.isValidPassword() = !isNullOrEmpty() && (this!!.length <= 20) && (this.length >= 6)

    private fun equals(txt1: String, txt2: String): Boolean {
        return (txt1.isEmpty() && txt2.isEmpty()) || txt1.equals(txt2)
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            clearErrors()
            (requireActivity() as StartActivity).password1 = editText1!!.text.toString()
            (requireActivity() as StartActivity).password2 = editText2!!.text.toString()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            valid = s.isValidPassword() && equals(editText1!!.text.toString(), editText2!!.text.toString())
        }
    }

    private fun clearErrors() {
        if (editText1!!.text.isValidPassword()) {
            editTextLayout1!!.setError("")
        }
        if (equals(editText1!!.text.toString(), editText2!!.text.toString())) {
            editTextLayout2!!.setError("")
        }
    }
}