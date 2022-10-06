package com.ann.planner.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ann.planner.data.TaskListRepositoryImpl
import com.ann.planner.domain.DeleteTaskItemUseCase
import com.ann.planner.domain.EditTaskItemUseCase
import com.ann.planner.domain.GetTaskListUseCase
import com.ann.planner.domain.TaskItem
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = TaskListRepositoryImpl(application)

    private val getTaskListUseCase = GetTaskListUseCase(repository)
    private val deleteTaskItemUseCase = DeleteTaskItemUseCase(repository)
    private val editTaskItemUseCase = EditTaskItemUseCase(repository)

    val taskList = getTaskListUseCase.getTaskList()


    fun deleteTaskItem(taskItem: TaskItem){
        viewModelScope.launch {
            deleteTaskItemUseCase.deleteTaskItem(taskItem)
        }
    }

    fun changeEnableState(taskItem: TaskItem){
        viewModelScope.launch {
            val newItem = taskItem.copy(enabled = !taskItem.enabled )
            editTaskItemUseCase.editTaskItem(newItem)
        }
    }
}