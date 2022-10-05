package com.ann.planner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_items")
data class TaskItemDbModel (
    @PrimaryKey (autoGenerate = true)
    val id: Int,
    val title: String,
    val enabled: Boolean
        )