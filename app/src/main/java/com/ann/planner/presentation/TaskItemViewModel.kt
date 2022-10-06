package com.ann.planner.presentation

import android.app.Application
import androidx.lifecycle.*
import com.ann.planner.data.TaskListRepositoryImpl
import com.ann.planner.domain.AddTaskItemUseCase
import com.ann.planner.domain.EditTaskItemUseCase
import com.ann.planner.domain.GetTaskItemUseCase
import com.ann.planner.domain.TaskItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskItemViewModel @Inject constructor(
    private val getTaskListUseCase:GetTaskItemUseCase,
    private val addTaskItemUseCase:AddTaskItemUseCase,
    private val editTaskItemUseCase:EditTaskItemUseCase

): ViewModel() {

    private val _errorInputTitle = MutableLiveData<Boolean>()
    val errorInputTitle: LiveData<Boolean>
    get() = _errorInputTitle

    private val _taskItem = MutableLiveData<TaskItem>()
    val taskItem: LiveData<TaskItem>
    get() = _taskItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
    get() = _shouldCloseScreen

    fun getTaskItem(taskItemId: Int){
        viewModelScope.launch {
            val item = getTaskListUseCase.getTaskItem(taskItemId)
            _taskItem.value = item
        }
    }

    fun addTaskItem(inputTitle: String?){
        val title = parseTitle(inputTitle)
        val fieldsValid = validateInput(title)
        if(fieldsValid){
            viewModelScope.launch {
                val taskItem = TaskItem(title, true)
                addTaskItemUseCase.addTaskItem(taskItem)
                finishWork()
            }
        }
    }

    fun editTaskItem(inputTitle: String?){
        val title = parseTitle(inputTitle)
        val fieldsValid = validateInput(title)
        if(fieldsValid){
            _taskItem.value?.let {
                viewModelScope.launch {
                    val item = it.copy(title = title)
                    editTaskItemUseCase.editTaskItem(item)
                    finishWork()
                }
            }
        }
    }

    private fun parseTitle(inputTitle: String?):String{
        return inputTitle?:""
    }

    private fun validateInput(title: String):Boolean{
        var result = true
        if (title.isBlank()){
            _errorInputTitle.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputTitle(){
        _errorInputTitle.value = false
    }

    private fun finishWork(){
        _shouldCloseScreen.value = Unit
    }

}