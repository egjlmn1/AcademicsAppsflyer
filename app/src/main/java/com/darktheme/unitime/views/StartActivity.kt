package com.darktheme.unitime.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.darktheme.unitime.R
import com.darktheme.unitime.databinding.ActivityStartBinding
import com.darktheme.unitime.viewModels.LoginViewModel
import com.darktheme.unitime.viewModels.RegisterViewModel


class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        val activityStartBinding: ActivityStartBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_start)
        activityStartBinding.loginViewModel = LoginViewModel(this)
        activityStartBinding.registerViewModel = RegisterViewModel(this)

        val viewHolder : LinearLayout = findViewById(R.id.card_view_holder)
        val loginView : CardView = viewHolder.findViewById(R.id.login_layout)
        val registerView : CardView = viewHolder.findViewById(R.id.register_layout)
        viewHolder.removeView(loginView)


        loginView.findViewById<TextView>(R.id.change_to_register_btn).setOnClickListener {
            viewHolder.removeView(loginView)
            viewHolder.addView(registerView)
        }
        registerView.findViewById<TextView>(R.id.change_to_login_btn).setOnClickListener {
            viewHolder.removeView(registerView)
            viewHolder.addView(loginView)
        }
    }
}