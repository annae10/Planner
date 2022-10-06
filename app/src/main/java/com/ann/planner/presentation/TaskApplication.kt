package com.ann.planner.presentation

import android.app.Application

class TaskApplication: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}