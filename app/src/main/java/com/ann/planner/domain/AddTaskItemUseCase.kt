package com.ann.planner.domain

class AddTaskItemUseCase(private val taskListRepository: TaskListRepository) {
    fun addTaskItem(taskItem: TaskItem){
        taskListRepository.addTaskItem(taskItem)
    }
}