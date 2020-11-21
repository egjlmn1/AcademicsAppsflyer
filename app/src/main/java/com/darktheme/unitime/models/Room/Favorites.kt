package com.darktheme.unitime.models.Room

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFavorite(vararg favoriteList : FavoriteList)

    @Delete
    abstract fun deleteFavorite(vararg favoriteList: FavoriteList)

    @Update
    abstract fun updateFolders(vararg folders: FavoriteList)

    @Query("SELECT * FROM FavoriteTable WHERE email = :email LIMIT 1")
    abstract fun getFoldersList(email: String) :FavoriteList?
}

@Entity(tableName = "FavoriteTable")
class FavoriteList {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    @ColumnInfo(name = "email")
    var email : String = ""
    @TypeConverters(FavoriteListTypeConverters::class)
    @ColumnInfo(name = "folders")
    var folders : List<String>? = null
    constructor(id : Int, email : String, folders : List<String>?) {
        this.id = id
        this.email = email
        this.folders = folders
    }

    @Ignore
    constructor(email : String, folders : List<String>?) {
        this.email = email
        this.folders = folders
    }

    @Ignore
    override fun toString() : String {
        return email
    }

}

class FavoriteListTypeConverters {

    var gson = Gson()

    @TypeConverter
    fun stringToList(data: String?): List<String> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type =
            object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<String?>?): String {
        return gson.toJson(someObjects)
    }
}