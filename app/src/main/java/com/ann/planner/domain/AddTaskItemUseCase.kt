package com.ann.planner.domain

import javax.inject.Inject

class AddTaskItemUseCase @Inject constructor(private val taskListRepository: TaskListRepository) {
    suspend fun addTaskItem(taskItem: TaskItem){
        taskListRepository.addTaskItem(taskItem)
    }
}