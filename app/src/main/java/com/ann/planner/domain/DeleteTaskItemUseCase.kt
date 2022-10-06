package com.ann.planner.domain

class DeleteTaskItemUseCase(private val taskListRepository: TaskListRepository) {
    suspend fun deleteTaskItem(taskItem: TaskItem){
        taskListRepository.deleteTaskItem(taskItem)
    }
}