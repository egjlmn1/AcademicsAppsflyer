package com.darktheme.academics.views

import android.content.Context
import android.preference.PreferenceManager
import com.darktheme.academics.R
import com.darktheme.academics.models.Room.AppDataBase
import com.darktheme.academics.models.Room.ProfileDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Login {

    suspend fun login(context: Context, profile: ProfileDB) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean(context.getString(R.string.logged_in), true).apply()
        prefs.edit().putString(context.getString(R.string.current_email), profile.email).apply()
        prefs.edit().putString(context.getString(R.string.current_id), profile.id.toString()).apply()
        addProfile(context, profile)
    }

    fun addProfile(context: Context, profile: ProfileDB) {
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
        prefs.edit().putString(context.getString(R.string.current_id), null).apply()
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