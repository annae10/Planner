package com.ann.planner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ann.planner.domain.DeleteTaskItemUseCase
import com.ann.planner.domain.EditTaskItemUseCase
import com.ann.planner.domain.GetTaskListUseCase
import com.ann.planner.domain.TaskItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val getTaskListUseCase: GetTaskListUseCase,
        private val deleteTaskItemUseCase: DeleteTaskItemUseCase,
        private val editTaskItemUseCase: EditTaskItemUseCase
): ViewModel() {

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