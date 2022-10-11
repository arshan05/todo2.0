package com.example.projecttodo

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TagsApplication :Application() {
    val database by lazy { TaskDatabase.getDatabase(this, CoroutineScope(SupervisorJob())) }
}