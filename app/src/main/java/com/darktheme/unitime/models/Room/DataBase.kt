package com.darktheme.unitime.models.Room

import android.content.Context
import androidx.room.*


@Database(exportSchema = false, version = 11, entities = [Suggestion::class, FavoriteList::class, ProfileDB::class])
abstract class AppDataBase : RoomDatabase() {

    abstract fun suggestionDao() : SuggestionDao
    abstract fun profileDao() : ProfileDao
    abstract fun favoritesDao() : FavoritesDao

    companion object {
        private val DB_NAME = "db_name"
        var instance : AppDataBase? = null

        fun getInstance(context: Context) : AppDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, DB_NAME).fallbackToDestructiveMigration().build()
            }
            return instance!!
        }
    }
}