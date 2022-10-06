package com.ann.planner.domain

import androidx.lifecycle.LiveData

interface TaskListRepository {

    suspend fun addTaskItem(taskItem: TaskItem)
    suspend fun deleteTaskItem(taskItem: TaskItem)
    suspend fun editTaskItem(taskItem: TaskItem)
    suspend fun getTaskItem(taskItemId: Int): TaskItem
    fun getTaskList(): LiveData<List<TaskItem>>

}