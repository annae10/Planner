package com.ann.planner.di

import androidx.lifecycle.ViewModel
import dagger.MapKey

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey (val value: KClass<out ViewModel>)