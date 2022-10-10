package com.ann.planner.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.ann.planner.domain.TaskItem
import com.ann.planner.presentation.TaskApplication
import javax.inject.Inject

class TaskListProvider: ContentProvider() {

    private val component by lazy {
        (context as TaskApplication).component
    }

    @Inject
    lateinit var taskListDao: TaskListDao

    @Inject
    lateinit var mapper: TaskListMapper

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply{
        addURI("com.ann.planner", "task_lists", GET_TASK_ITEMS_QUERY)
        addURI("com.ann.planner", "task_items/#", GET_TASK_ITEM_BY_ID_QUERY)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        return when (uriMatcher.match(uri)){
            GET_TASK_ITEMS_QUERY -> {
                taskListDao.getTaskListCursor()
            }
            else ->{
                null
            }
        }

    }

    override fun getType(uri: Uri): String? {
        TODO()
    }

    override fun insert(uri: Uri, values: ContentValues?):Uri?{
        when (uriMatcher.match(uri)){
            GET_TASK_ITEMS_QUERY -> {
                if (values == null) return null
                val id = values.getAsInteger("id")
                val title = values.getAsString("title")
                val enabled = values.getAsBoolean("enabled")
                val taskItem = TaskItem(
                    id = id,
                    title = title,
                    enabled = enabled
                )
                taskListDao.addTaskItemSync(mapper.mapEntityToDbModel(taskItem))
            }
        }
        return null
    }
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?):Int{
        when (uriMatcher.match(uri)) {
            GET_TASK_ITEMS_QUERY -> {
                val id = selectionArgs?.get(0)?.toInt() ?: -1
                return taskListDao.deleteTaskItemSync(id)
            }
        }
        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    companion object {

        private const val GET_TASK_ITEMS_QUERY = 100
        private const val GET_TASK_ITEM_BY_ID_QUERY = 101
    }
}