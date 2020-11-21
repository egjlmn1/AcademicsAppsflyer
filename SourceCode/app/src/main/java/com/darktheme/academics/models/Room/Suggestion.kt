package com.darktheme.academics.models.Room

import androidx.room.*

@Dao
abstract class SuggestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSuggestions(vararg suggestion : Suggestion)

    @Delete
    abstract fun deleteSuggestions(vararg suggestion: Suggestion)

    @Query("SELECT * FROM SuggestionTable")
    abstract fun getSuggestionList() : List<Suggestion>
}

@Entity(tableName = "SuggestionTable")
class Suggestion {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    @ColumnInfo(name = "suggestion")
    var suggestion : String = ""

    constructor(id : Int, suggestion : String) {
        this.id = id
        this.suggestion = suggestion
    }

    @Ignore
    override fun toString() : String {
        return suggestion
    }

}