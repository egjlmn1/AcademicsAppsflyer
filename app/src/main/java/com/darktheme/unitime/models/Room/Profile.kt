package com.darktheme.unitime.models.Room

import androidx.room.*

@Dao
abstract class ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProfile(vararg profile : Profile)

    @Query("DELETE FROM ProfileTable WHERE email = :email")
    abstract fun deleteProfile(email: String)

    @Query("SELECT * FROM ProfileTable")
    abstract fun getProfilesList() : List<Profile>

    @Query("SELECT * FROM ProfileTable WHERE email = :email")
    abstract fun getProfile(email: String) : Profile?
}

@Entity(tableName = "ProfileTable")
class Profile{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
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
        this.name = name
    }

    @Ignore
    override fun toString() : String {
        return email
    }

}