package com.ann.planner.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ann.planner.R
import com.ann.planner.domain.TaskItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class TaskItemFragment: Fragment() {

    private lateinit var viewModel: TaskItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private lateinit var tilTitle: TextInputLayout
    private lateinit var etTitle: EditText
    private lateinit var buttonSave: Button

    private var screenMode: String = MODE_UNKNOWN
    private var taskItemId: Int = TaskItem.UNDEFINED_ID


    override fun onAttach(context: Context){
        super.onAttach(context)
        if (context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("TaskItemFragment", "onCreate")
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_item, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            onEditingFinishedListener.onEditingFinished()
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
                viewModel.resetErrorInputTitle()
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
        val args = requireArguments()
        if (args.containsKey(SCREEN_MODE)){
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if(mode != MODE_EDIT && mode != MODE_ADD){
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if(screenMode == MODE_EDIT){
            if(!args.containsKey((TASK_ITEM_ID))){
                throw RuntimeException("Param shop item id is absent")
            }
            taskItemId = args.getInt(TASK_ITEM_ID, TaskItem.UNDEFINED_ID)
        }
    }

    private fun initViews(view: View){
        tilTitle = view.findViewById(R.id.til_title)
        etTitle = view.findViewById(R.id.et_title)
        buttonSave = view.findViewById(R.id.save_button)
    }

    interface OnEditingFinishedListener {

        fun onEditingFinished()
    }

    companion object {

        private const val SCREEN_MODE = "extra_mode"
        private const val TASK_ITEM_ID = "extra_task_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""


        fun newInstanceAddItem(): TaskItemFragment {
            return TaskItemFragment().apply{
                arguments = Bundle().apply{
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem( taskItemId: Int): TaskItemFragment{
            return TaskItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(TASK_ITEM_ID, taskItemId)
                }
            }
        }

//        fun newInstanceAddItem(): TaskItemFragment{
//            return TaskItemFragment(MODE_ADD)
//        }

//        fun NewInstanceEditItem(taskItemId: Int): TaskItemFragment{
//            return TaskItemFragment(MODE_EDIT, taskItemId)
//        }
//
//        fun newIntentEditItem(context: Context, taskItemId: Int): Intent{
//            val intent = Intent(context, TaskItemActivity::class.java)
//            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
//        }

    }
}