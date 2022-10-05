package com.ann.planner.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ann.planner.data.TaskListRepositoryImpl
import com.ann.planner.domain.DeleteTaskItemUseCase
import com.ann.planner.domain.EditTaskItemUseCase
import com.ann.planner.domain.GetTaskListUseCase
import com.ann.planner.domain.TaskItem

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = TaskListRepositoryImpl(application)

    private val getTaskListUseCase = GetTaskListUseCase(repository)
    private val deleteTaskItemUseCase = DeleteTaskItemUseCase(repository)
    private val editTaskItemUseCase = EditTaskItemUseCase(repository)

    val taskList = getTaskListUseCase.getTaskList()


    fun deleteTaskItem(taskItem: TaskItem){
        deleteTaskItemUseCase.deleteTaskItem(taskItem)
    }

    fun changeEnableState(taskItem: TaskItem){
        val newItem = taskItem.copy(enabled = !taskItem.enabled )
        editTaskItemUseCase.editTaskItem(newItem)
    }
}