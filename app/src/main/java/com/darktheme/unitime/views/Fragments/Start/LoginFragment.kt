package com.darktheme.unitime.views.Fragments.Start

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.ProfileRetrofit
import com.darktheme.unitime.views.Activities.StartActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Response


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
                Toast.makeText(requireContext(),"Logging in...", Toast.LENGTH_SHORT).show()
                continueButton!!.isEnabled = false
                (requireActivity() as StartActivity).loginRequest(editText1!!.text.toString(), editText2!!.text.toString(), loginResponse, onLoginFailure)
            }
        }

        return root
    }

    val loginResponse = { _: Call<ProfileRetrofit>?, response: Response<ProfileRetrofit>? ->
        continueButton!!.isEnabled = true
        val code = response!!.code()
        println("login2: " + code)
        if (code == 200) {
            (requireActivity() as StartActivity).loginProfile(response.body()!!, R.id.action_to_mainPageActivity)
            (requireActivity() as StartActivity).finish()
        } else {
            Toast.makeText(requireContext(),"Incorrect email or password", Toast.LENGTH_SHORT).show()
        }

    }

    val onLoginFailure = { _: Call<ProfileRetrofit>?, t: Throwable? ->
        Toast.makeText(requireContext(),"An error occurred", Toast.LENGTH_SHORT).show()
        continueButton!!.isEnabled = true
        println(t!!.message)
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