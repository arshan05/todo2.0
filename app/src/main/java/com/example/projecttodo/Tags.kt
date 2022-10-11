package com.example.projecttodo

import android.text.Editable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tag_table")
data class Tags(val tag:String?, @PrimaryKey(autoGenerate = false) val id: Int? = null)
