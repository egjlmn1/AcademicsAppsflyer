package com.darktheme.unitime.models.Room

import androidx.room.*

@Dao
abstract class ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSuggestions(vararg profile : Profile)

    @Delete
    abstract fun deleteSuggestions(vararg profile: Profile)

    @Query("SELECT * FROM SuggestionTable LIMIT 1")
    abstract fun getSuggestionList() : Profile
}

@Entity(tableName = "ProfileTable")
class Profile {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    @ColumnInfo(name = "email")
    var email : String = ""
    @ColumnInfo(name = "password")
    var password : String = ""
    @ColumnInfo(name = "name")
    var name : String = ""

    constructor(id : Int, email : String, password : String, name : String) {
        this.id = id
        this.email = email
        this.password = password
        this.name = name
    }

    @Ignore
    constructor(email : String, password : String, name : String) {
        this.email = email
        this.password = password
        this.name = name    }

    @Ignore
    override fun toString() : String {
        return email
    }

}