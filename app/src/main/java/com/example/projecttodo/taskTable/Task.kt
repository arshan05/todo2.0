package com.example.projecttodo.taskTable

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    val task: String?,
    var priority: Int?,
    var date: String?,
    var isCompleted:Boolean?,
    var tag: String?,
    @PrimaryKey(autoGenerate = false) val id: Int? = null
)
