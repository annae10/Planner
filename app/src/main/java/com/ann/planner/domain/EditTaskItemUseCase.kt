package com.ann.planner.domain

class EditTaskItemUseCase(private val taskListRepository: TaskListRepository) {
    fun editTaskItem(taskItem: TaskItem){
        taskListRepository.editTaskItem(taskItem)
    }
}