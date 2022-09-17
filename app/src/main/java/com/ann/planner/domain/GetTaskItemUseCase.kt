package com.ann.planner.domain

class GetTaskItemUseCase(private val taskListRepository: TaskListRepository){
    fun getTaskItem(taskItemId: Int): TaskItem{
        return taskListRepository.getTaskItem(taskItemId)
    }
}