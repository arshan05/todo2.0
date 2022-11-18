package com.example.projecttodo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag_table")
data class Tags(val tag:String?, @PrimaryKey(autoGenerate = false) val id: Int? = null)
