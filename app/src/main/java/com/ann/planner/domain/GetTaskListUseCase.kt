package com.ann.planner.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GetTaskListUseCase @Inject constructor (
    private val taskListRepository: TaskListRepository
    ) {
    fun getTaskList(): LiveData<List<TaskItem>> {
        return taskListRepository.getTaskList()
    }
}