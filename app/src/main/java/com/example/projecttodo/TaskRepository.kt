package com.example.projecttodo

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TaskRepository(private val taskDao: TaskDao) {
    fun allTasks() : Flow<List<Task>> = taskDao.getTask()
    fun getParticularTag(tagIn:String) =  taskDao.getParticularTag(tagIn)
    fun getTodayTask(dateIn:String) = taskDao.getTodayTask(dateIn)
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