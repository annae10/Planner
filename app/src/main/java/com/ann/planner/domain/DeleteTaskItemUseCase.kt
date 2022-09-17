package com.ann.planner.domain

class DeleteTaskItemUseCase(private val taskListRepository: TaskListRepository) {
    fun deleteTaskItem(taskItem: TaskItem){
        taskListRepository.deleteTaskItem(taskItem)
    }
}