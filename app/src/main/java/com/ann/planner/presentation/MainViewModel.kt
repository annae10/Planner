package com.ann.planner.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ann.planner.data.TaskListRepositoryImpl
import com.ann.planner.domain.DeleteTaskItemUseCase
import com.ann.planner.domain.EditTaskItemUseCase
import com.ann.planner.domain.GetTaskListUseCase
import com.ann.planner.domain.TaskItem

class MainViewModel: ViewModel() {

    private val repository = TaskListRepositoryImpl

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