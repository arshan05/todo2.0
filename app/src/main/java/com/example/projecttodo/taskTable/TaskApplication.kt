package com.example.projecttodo.taskTable

import android.app.Application
import com.example.projecttodo.TaskDatabase
import com.example.projecttodo.tagTable.TagsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TaskApplication : Application() {
//    val applicationScope = CoroutineScope(SupervisorJob())
//    val database by lazy { TaskDatabase.getDatabase(this,applicationScope)}
//    val repository by lazy { TaskRepository(database.taskDao()) }

    private val database by lazy { TaskDatabase.getDatabase(this, CoroutineScope(SupervisorJob())) }
    val repository by lazy { TaskRepository(database.taskDao()) }
    val tagRepository by lazy { TagsRepository(database.tagsDao()) }


//
//    val database by lazy { TaskDatabase.getDatabase(this, CoroutineScope(SupervisorJob())) }
//    val repository by lazy { TaskRepository(database.taskDao()) }
}