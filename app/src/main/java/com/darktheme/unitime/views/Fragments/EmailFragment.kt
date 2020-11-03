package com.darktheme.unitime.views.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.darktheme.unitime.R
import com.darktheme.unitime.views.Activities.StartActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class EmailFragment : Fragment() {

    var continueButton: Button? = null
    var editText: TextInputEditText? = null
    var editTextLayout: TextInputLayout? = null
    var valid = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_email, container, false)

        editText = root.findViewById(R.id.edittext1)
        editTextLayout = root.findViewById(R.id.edittext_layout)
        continueButton = root.findViewById<Button>(R.id.continue_btn)

        val activity = requireActivity() as StartActivity
        editTextLayout!!.setError(arguments?.getString("error"))

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    activity.navController!!.navigate(R.id.action_email_to_nav_start)
                }
            }
        activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        editText!!.setText(activity.email)
        editText!!.addTextChangedListener(textWatcher)
        continueButton!!.setOnClickListener {
            if (valid) {
                activity.navController!!.navigate(R.id.action_email_to_password)
            } else {
                editTextLayout!!.setError("Enter a valid email address")
            }
        }
        return root
    }

    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this!!).matches()

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            (requireActivity() as StartActivity).email = s.toString()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            valid = s.isValidEmail()
        }
    }

}