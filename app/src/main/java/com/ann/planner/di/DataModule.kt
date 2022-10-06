package com.ann.planner.di

import android.app.Application
import com.ann.planner.data.AppDatabase
import com.ann.planner.data.TaskListDao
import com.ann.planner.data.TaskListRepositoryImpl
import com.ann.planner.domain.TaskListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindTaskListRepository(impl: TaskListRepositoryImpl): TaskListRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideTaskListDao(
            application: Application
        ): TaskListDao{
            return AppDatabase.getInstance(application).taskListDao()
        }
    }
}