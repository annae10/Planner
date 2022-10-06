package com.ann.planner.domain

import javax.inject.Inject

class GetTaskItemUseCase @Inject constructor (
    private val taskListRepository: TaskListRepository
    ){
    suspend fun getTaskItem(taskItemId: Int): TaskItem{
        return taskListRepository.getTaskItem(taskItemId)
    }
}