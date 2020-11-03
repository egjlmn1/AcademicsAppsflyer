package com.darktheme.unitime.views.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.darktheme.unitime.R
import com.darktheme.unitime.views.Activities.StartActivity
import com.darktheme.unitime.views.Login
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class LoginFragment : Fragment() {

    var continueButton: Button? = null
    var editText1: TextInputEditText? = null
    var editText2: TextInputEditText? = null
    var editTextLayout1: TextInputLayout? = null
    var editTextLayout2: TextInputLayout? = null

    var valid = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        editText1 = root.findViewById<TextInputEditText>(R.id.edittext1)
        editText2 = root.findViewById<TextInputEditText>(R.id.edittext2)

        editTextLayout1 = root.findViewById(R.id.edittext1_layout)
        editTextLayout2 = root.findViewById(R.id.edittext2_layout)

        val activity = requireActivity() as StartActivity

        editText1!!.addTextChangedListener(textWatcher)
        editText2!!.addTextChangedListener(textWatcher)

        continueButton = root.findViewById<Button>(R.id.login_btn)
        continueButton!!.setOnClickListener {
            valid = true
            if (!editText1!!.text.isValidEmail()) {
                editTextLayout1!!.setError("Enter a valid email")
                valid = false
            }
            if (!editText2!!.text.isValidPassword()) {
                editTextLayout2!!.setError("Enter a valid password")
                valid = false
            }
            if (valid) {
                // TODO send login request to server
                Login().login(activity)

                activity.finish()
                activity.navController!!.navigate(R.id.action_login_to_mainPageActivity)
            }
        }

        return root
    }
    fun CharSequence?.isValidPassword() = !isNullOrEmpty() && (this!!.length <= 20) && (this.length >= 6)
    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this!!).matches()

    private fun equals(txt1: String, txt2: String): Boolean {
        return (txt1.isEmpty() && txt2.isEmpty()) || txt1.equals(txt2)
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            clearErrors()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    private fun clearErrors() {
        if (editText1!!.text.isValidEmail()) {
            editTextLayout1!!.setError("")
        }
        if (editText2!!.text.isValidPassword()) {
            editTextLayout2!!.setError("")
        }
    }
}