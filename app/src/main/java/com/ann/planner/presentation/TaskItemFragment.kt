package com.ann.planner.presentation

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ann.planner.databinding.FragmentTaskItemBinding
import com.ann.planner.domain.TaskItem
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.concurrent.thread

class TaskItemFragment: Fragment() {

    private lateinit var viewModel: TaskItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var _binding: FragmentTaskItemBinding? = null
    private val binding: FragmentTaskItemBinding
    get() = _binding ?: throw RuntimeException("FragmentTaskItemBinding == null")

    private var screenMode: String = MODE_UNKNOWN
    private var taskItemId: Int = TaskItem.UNDEFINED_ID

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy{
        (requireActivity().application as TaskApplication).component
    }

    override fun onAttach(context: Context){

        component.inject(this)

        super.onAttach(context)
        if (context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskItemBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[TaskItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }


    private fun observeViewModel(){
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
        binding.etTitle.addTextChangedListener(object: TextWatcher {
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
        binding.saveButton.setOnClickListener{
            viewModel.editTaskItem(
                binding.etTitle.text?.toString())
        }
    }

    private fun launchAddMode(){

        binding.saveButton.setOnClickListener {

            thread{
                context?.contentResolver?.insert(
                    Uri.parse("content://com.ann.planner/task_items"),
                    ContentValues().apply {
                        put("id", 0)
                        put("title", binding.etTitle.text?.toString())
                        put("enabled", true)
                    }
                )
            }
        }
    }

    private fun parseParams(){
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)){
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

    }
}