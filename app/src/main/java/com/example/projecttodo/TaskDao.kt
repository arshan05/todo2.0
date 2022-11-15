package com.example.projecttodo

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table ORDER BY priority ASC")
    fun getTask(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task:Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM task_table where tag IN(:tagIn) ORDER BY priority ASC")
    fun getParticularTag(tagIn:String) : Flow<List<Task>>

    @Query("SELECT * FROM task_table where date IN(:dateIn) ORDER BY priority ASC")
    fun getTodayTask(dateIn: String) : Flow<List<Task>>

}