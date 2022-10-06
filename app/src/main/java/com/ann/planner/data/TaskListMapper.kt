package com.ann.planner.data

import com.ann.planner.domain.TaskItem
import javax.inject.Inject

class TaskListMapper @Inject constructor(){

    fun mapEntityToDbModel(taskItem: TaskItem) = TaskItemDbModel(
        id = taskItem.id,
        title = taskItem.title,
        enabled = taskItem.enabled
    )

    fun mapDbModelToEntity(taskItemDbModel: TaskItemDbModel) = TaskItem(
        id = taskItemDbModel.id,
        title = taskItemDbModel.title,
        enabled = taskItemDbModel.enabled
    )

    fun mapListDbModelToListEntity(list: List<TaskItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}
