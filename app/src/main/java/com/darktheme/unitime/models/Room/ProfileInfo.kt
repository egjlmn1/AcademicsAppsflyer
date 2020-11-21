//package com.darktheme.unitime.models.Room
//
//import androidx.room.*
//
//@Dao
//abstract class ProfileInfoDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract fun insertProfileInfo(vararg profile : ProfileInfo)
//
//    @Query("DELETE FROM ProfileInfoTable WHERE email = :email")
//    abstract fun deleteProfileInfo(email: String)
//
//    @Query("SELECT * FROM ProfileInfoTable WHERE email = :email")
//    abstract fun getProfileInfo(email: String) : Profile?
//}
//
//@Entity(tableName = "ProfileInfoTable")
//class ProfileInfo{
//    @PrimaryKey(autoGenerate = true)
//    var id : Int = 0
//    @ColumnInfo(name = "email")
//    var email : String = ""
//    @ColumnInfo(name = "credits")
//    var credits : Int = 0
//
//    @Ignore
//    override fun toString() : String {
//        return email
//    }
//
//}