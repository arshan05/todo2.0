package com.example.projecttodo

import android.text.Editable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task_table")
data class Task(
    val task: String?,
    var priority: Int?,
    var date: String?,
    var isCompleted:Boolean?,
    var tag: String?,
    @PrimaryKey(autoGenerate = false) val id: Int? = null
)
