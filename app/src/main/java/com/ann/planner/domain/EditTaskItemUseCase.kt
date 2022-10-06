package com.ann.planner.domain

class EditTaskItemUseCase(private val taskListRepository: TaskListRepository) {
    suspend fun editTaskItem(taskItem: TaskItem){
        taskListRepository.editTaskItem(taskItem)
    }
}