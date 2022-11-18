package com.example.projecttodo.tagTable

import android.app.Application
import com.example.projecttodo.TaskDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TagsApplication :Application() {
    private val database by lazy { TaskDatabase.getDatabase(this, CoroutineScope(SupervisorJob())) }
    val repository by lazy {  TagsRepository(database.tagsDao()) }
}