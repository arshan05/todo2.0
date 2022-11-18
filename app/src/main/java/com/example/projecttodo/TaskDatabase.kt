package com.example.projecttodo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Task::class, Tags::class], version = 1)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun tagsDao(): TagsDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                )
                    .addCallback(TaskDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class TaskDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.taskDao(), database.tagsDao())
                    }
                }
            }

            suspend fun populateDatabase(taskDao: TaskDao,tagsDao: TagsDao){

                taskDao.deleteAll()
                taskDao.deleteAll()
                tagsDao.insert(Tags("work"))
                tagsDao.insert(Tags("personal"))
            }

        }
    }
}