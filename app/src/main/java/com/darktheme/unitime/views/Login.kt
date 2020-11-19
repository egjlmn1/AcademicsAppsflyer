package com.darktheme.unitime.views

import android.content.Context
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Room.AppDataBase
import com.darktheme.unitime.models.Room.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait

class Login {

    suspend fun login(context: Context, profile: Profile) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean(context.getString(R.string.logged_in), true).apply()
        prefs.edit().putString(context.getString(R.string.current_email), profile.email).apply()
        addProfile(context, profile)
    }

    suspend fun addProfile(context: Context, profile: Profile) {
        val db = AppDataBase.getInstance(context)
        val p = db.profileDao().getProfile(profile.email)
        if (p == null) {
            // Insert profile to data base
            db.profileDao().insertProfile(profile)
        }
    }

    fun logout(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean(context.getString(R.string.logged_in), false).apply()
        val email = prefs.getString(context.getString(R.string.current_email), "")
        prefs.edit().putString(context.getString(R.string.current_email), null).apply()
        removeProfile(context, email!!)
    }

    fun removeProfile(context: Context, email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDataBase.getInstance(context)
            val profile = db.profileDao().getProfile(email)
            if (profile != null) {
                // Delete profile from data base
                db.profileDao().deleteProfile(email)
            }
        }
    }
}