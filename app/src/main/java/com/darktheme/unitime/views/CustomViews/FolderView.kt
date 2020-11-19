package com.darktheme.unitime.views.CustomViews

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.opengl.Visibility
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Room.AppDataBase
import com.darktheme.unitime.views.Activities.MainPageActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FolderView(val view: View, val path: String, val activity: MainPageActivity) {
    var favorite = false
    val star = view.findViewById<ImageView>(R.id.colored_star)

    init {
        val p = path.split('/')
        val name = p[p.size-1]
        view.findViewById<TextView>(R.id.directory_name).text = name
        isFavorite(path)
        setOnFavoriteClick()
    }

    fun setOnFavoriteClick() {
        star.setOnClickListener {
            favorite = !favorite
            setStarColor()
            //TODO save to db
        }
    }

    fun isFavorite(path: String) {
        CoroutineScope(IO).launch {
            val db = AppDataBase.getInstance(activity)
            val favorites = db.favoritesDao().getFoldersList(activity.email!!)
            if (favorites != null) {
                favorite = favorites.contains(path) //GET FROM DB
                withContext(Dispatchers.Main) {
                    setStarColor()
                }
            }
        }
    }

    fun setStarColor() {
        if (favorite) {
            star.backgroundTintList = ColorStateList.valueOf(
                Color.YELLOW)
        } else {
            star.backgroundTintList = ColorStateList.valueOf(
                Color.GRAY)
        }
    }

    fun show() {
        view.visibility = View.VISIBLE
    }

    fun hide() {
        view.visibility = View.GONE
    }
}