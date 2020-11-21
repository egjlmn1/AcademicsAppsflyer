package com.darktheme.academics.views.CustomViews

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.darktheme.academics.R
import com.darktheme.academics.models.Room.AppDataBase
import com.darktheme.academics.models.Room.FavoriteList
import com.darktheme.academics.views.Activities.MainPageActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FolderView(val view: View, val path: String, val activity: MainPageActivity) {
    var favorite = false
    val star = view.findViewById<ImageView>(R.id.colored_star)
    var name: String

    init {
        val p = path.split('/')
        name = p[p.size-1]
        view.findViewById<TextView>(R.id.directory_name).text = name
        isFavorite(path)
        setOnFavoriteClick()
    }

    fun setOnFavoriteClick() {
        star.setOnClickListener {
            if (activity.email == null) {
                Toast.makeText(activity,"Try again later", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            favorite = !favorite
            setStarColor()
            CoroutineScope(IO).launch {
                val db = AppDataBase.getInstance(activity)
                val favorites = db.favoritesDao().getFoldersList(activity.email!!)
                if (favorites != null) {
                    var newFolders: List<String>? = null
                    if (favorite) {
                        newFolders = favorites.folders!!.toMutableList()
                        newFolders.add(path)
                    } else {
                        newFolders = favorites.folders!!.toMutableList()
                        newFolders.remove(path)
                    }
                    favorites.folders = newFolders
                    db.favoritesDao().updateFolders(favorites)
                }

            }
        }
    }

    fun isFavorite(path: String) {
        CoroutineScope(IO).launch {
            val db = AppDataBase.getInstance(activity)
            if (activity.email != null) {
                val favorites = db.favoritesDao().getFoldersList(activity.email!!)
                if (favorites != null) {
                    favorite = favorites.folders!!.contains(path) //GET FROM DB
                } else {
                    favorite = false
                    val newFav = FavoriteList(activity.email!!, ArrayList<String>())
                    db.favoritesDao().insertFavorite(newFav)
                }
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