package com.ann.planner.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ann.planner.domain.TaskItem
import com.ann.planner.domain.TaskListRepository
import java.lang.RuntimeException

class TaskListRepositoryImpl :TaskListRepository{

    private val taskListLD = MutableLiveData<List<TaskItem>>()
    private val taskList = mutableListOf<TaskItem>()

    private var autoIncrementId = 0

    init{
        for(i in 0 until 10){
            val item = TaskItem("Title $i", true, i)
            addTaskItem(item)
        }
    }

    override fun addTaskItem(taskItem: TaskItem) {
        if(taskItem.id == TaskItem.UNDEFINED_ID){
            taskItem.id = autoIncrementId++
        }
        taskList.add(taskItem)
        updateList()
    }

    override fun deleteTaskItem(taskItem: TaskItem) {
        taskList.remove(taskItem)
        updateList()
    }

    override fun editTaskItem(taskItem: TaskItem) {
        val oldElement = getTaskItem(taskItem.id)
        taskList.remove(oldElement)
        addTaskItem(taskItem)
    }

    override fun getTaskItem(taskItemId: Int): TaskItem {
        return taskList.find{
            it.id == taskItemId
        }?: throw RuntimeException("Element with id $taskItemId not found")
    }

    override fun getTaskList(): LiveData<List<TaskItem>> {
        return taskListLD
    }

    private fun updateList(){
        taskListLD.value = taskList.toList()
    }
}