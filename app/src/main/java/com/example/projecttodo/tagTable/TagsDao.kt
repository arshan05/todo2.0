package com.example.projecttodo.tagTable

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {
    @Query("SELECT * FROM tag_table")
    fun getTags(): Flow<List<Tags>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tags: Tags)

    @Delete
    suspend fun delete(tags: Tags)


    @Query("DELETE FROM tag_table")
    suspend fun deleteAll()
}