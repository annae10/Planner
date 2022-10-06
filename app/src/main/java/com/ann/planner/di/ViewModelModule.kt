package com.ann.planner.di

import androidx.lifecycle.ViewModel
import com.ann.planner.presentation.MainViewModel
import com.ann.planner.presentation.TaskItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskItemViewModel::class)
    fun bindTaskItemViewModel(viewModel: TaskItemViewModel): ViewModel
}