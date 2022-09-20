package com.ann.planner

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.ann.planner.domain.TaskItem.Companion.UNDEFINED_ID
import com.ann.planner.presentation.TaskItemViewModel
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class TaskItemActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskItemViewModel

    private lateinit var tilTitle: TextInputLayout
    private lateinit var etTitle: EditText
    private lateinit var buttonSave: Button

    private var screenMode = MODE_UNKNOWN
    private var taskItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_item)
        parseIntent()
        viewModel = ViewModelProvider(this)[TaskItemViewModel::class.java]
        initViews()
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.errorInputTitle.observe(this){
            val message = if (it){
                getString(R.string.error_input_title)
            } else { null}
            tilTitle.error = message
        }
        viewModel.shouldCloseScreen.observe(this){
            finish()
        }
    }

    private fun launchRightMode(){
        when(screenMode){
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addTextChangeListeners(){
        etTitle.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){

            }
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){
                viewModel.resetErrorInputName()
            }
            override fun afterTextChanged(s: Editable?){

            }
        })
    }

    private fun launchEditMode(){

        viewModel.getTaskItem(taskItemId)
        viewModel.taskItem.observe(this){
            etTitle.setText(it.title)
        }
        buttonSave.setOnClickListener {
            viewModel.editTaskItem(etTitle.text?.toString())
        }
    }

    private fun launchAddMode(){

        buttonSave.setOnClickListener {
            viewModel.addTaskItem(etTitle.text?.toString())
        }
    }

    private fun parseIntent(){
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if(mode != MODE_EDIT && mode != MODE_ADD){
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if(screenMode == MODE_EDIT){
            if(!intent.hasExtra(EXTRA_TASK_ITEM_ID)){
                throw RuntimeException("Param shop item id is absent")
            }
            taskItemId = intent.getIntExtra(EXTRA_TASK_ITEM_ID, UNDEFINED_ID)
        }
    }

    private fun initViews(){
        tilTitle = findViewById(R.id.til_title)
        etTitle = findViewById(R.id.et_title)
        buttonSave = findViewById(R.id.save_button)
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_TASK_ITEM_ID = "extra_task_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, TaskItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, taskItemId: Int): Intent{
            val intent = Intent(context, TaskItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_TASK_ITEM_ID, taskItemId)
            return intent
        }

    }
}