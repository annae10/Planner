package com.ann.planner.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ann.planner.domain.TaskItem
import com.ann.planner.domain.TaskListRepository

class TaskListRepositoryImpl(application: Application) :TaskListRepository{

    private val taskListDao = AppDatabase.getInstance(application).taskListDao()
    private val mapper = TaskListMapper()

    override suspend fun addTaskItem(taskItem: TaskItem) {
        taskListDao.addTaskItem(mapper.mapEntityToDbModel(taskItem))
    }

    override suspend fun deleteTaskItem(taskItem: TaskItem) {
        taskListDao.deleteTaskItem(taskItem.id)
    }

    override suspend fun editTaskItem(taskItem: TaskItem) {
        taskListDao.addTaskItem(mapper.mapEntityToDbModel(taskItem))
    }

    override suspend fun getTaskItem(taskItemId: Int): TaskItem {
        val dbModel = taskListDao.getTaskItem(taskItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun getTaskList(): LiveData<List<TaskItem>> = Transformations.map(
        taskListDao.getTaskList()
    ){
        mapper.mapListDbModelToListEntity(it)
    }
}