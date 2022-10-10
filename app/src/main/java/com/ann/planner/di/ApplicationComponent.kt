package com.ann.planner.di

import android.app.Application
import com.ann.planner.data.TaskListProvider
import com.ann.planner.presentation.MainActivity
import com.ann.planner.presentation.TaskItemFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
    ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: TaskItemFragment)

    fun inject(provider: TaskListProvider)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}