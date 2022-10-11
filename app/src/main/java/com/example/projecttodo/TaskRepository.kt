package com.example.projecttodo

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    fun allTasks() : Flow<List<Task>> = taskDao.getTask()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(task: Task){
        taskDao.insert(task)
    }
    suspend fun delete(task:Task){
        taskDao.delete(task)
    }
    suspend fun update(task: Task){
        taskDao.update(task)
    }
}