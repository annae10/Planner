package com.ann.planner.data

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskListDao {

    @Query("SELECT * FROM task_items")
    fun getTaskList() : LiveData<List<TaskItemDbModel>>

    @Query("SELECT * FROM task_items")
    fun getTaskListCursor(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTaskItem(taskItemDbModel: TaskItemDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTaskItemSync(taskItemDbModel: TaskItemDbModel)

    @Query("DELETE FROM task_items WHERE id=:taskItemId")
    suspend fun deleteTaskItem(taskItemId: Int)

    @Query("DELETE FROM task_items WHERE id=:taskItemId")
    fun deleteTaskItemSync(taskItemId: Int):Int

    @Query("SELECT * FROM task_items WHERE id=:taskItemId LIMIT 1")
    suspend fun getTaskItem(taskItemId: Int): TaskItemDbModel
}