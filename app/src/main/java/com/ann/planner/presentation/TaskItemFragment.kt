package com.ann.planner.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ann.planner.R
import com.ann.planner.TaskItemActivity
import com.ann.planner.domain.TaskItem.Companion.UNDEFINED_ID
import com.google.android.material.textfield.TextInputLayout

class TaskItemFragment(
    private val screenMode: String = MODE_UNKNOWN,
    private val taskItemId: Int = UNDEFINED_ID
): Fragment() {

    private lateinit var viewModel: TaskItemViewModel

    private lateinit var tilTitle: TextInputLayout
    private lateinit var etTitle: EditText
    private lateinit var buttonSave: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_item, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseParams()
        viewModel = ViewModelProvider(this)[TaskItemViewModel::class.java]
        initViews(view)
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }


    private fun observeViewModel(){
        viewModel.errorInputTitle.observe(viewLifecycleOwner){
            val message = if (it){
                getString(R.string.error_input_title)
            } else { null}
            tilTitle.error = message
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner){
            activity?.onBackPressed()
        }
    }

    private fun launchRightMode(){
        when(screenMode){
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addTextChangeListeners(){
        etTitle.addTextChangedListener(object: TextWatcher {
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
        viewModel.taskItem.observe(viewLifecycleOwner){
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

    private fun parseParams(){
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD){
            throw RuntimeException("Param screen mode is absent: $screenMode")
        }
        if(screenMode == MODE_EDIT && taskItemId == UNDEFINED_ID){
            throw RuntimeException("Param task item id is absent")
        }
    }

    private fun initViews(view: View){
        tilTitle = view.findViewById(R.id.til_title)
        etTitle = view.findViewById(R.id.et_title)
        buttonSave = view.findViewById(R.id.save_button)
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_TASK_ITEM_ID = "extra_task_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): TaskItemFragment {
            return TaskItemFragment(MODE_ADD)
        }

        fun newInstanceEditItem( taskItemId: Int): TaskItemFragment{
            return TaskItemFragment(MODE_EDIT, taskItemId)
        }

    }
}