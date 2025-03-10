package com.tanveer.recyclertask

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tanveer.recyclertask.databinding.CustomDialogLayoutBinding
import com.tanveer.recyclertask.databinding.FragmentSingleNotesBinding
import com.tanveer.recyclertask.databinding.TodoDialogBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SingleNotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingleNotesFragment : Fragment(), ToDoInterface{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentSingleNotesBinding? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    var toDoEntity = arrayListOf<ToDoEntity>()
    lateinit var todoAdapter: ToDoItemRecycler
    var taskDataClass = TaskDataClass()
    var todoDatabase: TodoDatabase? = null
    private val TAG = "SingleNotesFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleNotesBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return (binding?.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoAdapter = ToDoItemRecycler(toDoEntity,this)
        todoDatabase = TodoDatabase.getInstance(requireContext())
        arguments?.let {
            var notes = it.getString("notes")
            Log.e(TAG,"notes $notes")
            taskDataClass = Gson().fromJson(notes, TaskDataClass::class.java)
            Log.e(TAG,"onViewCreated:$taskDataClass")
            binding?.tvTitle?.setText(taskDataClass.title)
            binding?.tvDescription?.setText(taskDataClass.description)
            getToDoList()
        }

        linearLayoutManager = LinearLayoutManager(requireContext())
        binding?.rvToDo?.adapter = todoAdapter
        binding?.rvToDo?.layoutManager = linearLayoutManager
        binding?.fabToDo?.setOnClickListener {
            var dialog = Dialog(requireContext()).apply {
                var dialogBinding = TodoDialogBinding.inflate(layoutInflater)
                setContentView(dialogBinding.root)
                show()
                dialogBinding.add.setOnClickListener {
                    if (dialogBinding.etTodo.text?.toString().isNullOrEmpty()) {
                        dialogBinding.etTodo.error = resources.getString(R.string.Enter_todo_item)
                    } else {
                        todoDatabase?.todoDao()?.insertToDoItem(
                            ToDoEntity(
                                taskId = taskDataClass.id,
                                todo = dialogBinding.etTodo.text.toString(),
                                isCompleted = dialogBinding.cbIsCompleted.isChecked
                            )
                        )
                        getToDoList()
                        dismiss()

                    }
                }
            }
        }

    }

    private fun getToDoList() {
        toDoEntity.clear()
        toDoEntity.addAll(
            todoDatabase?.todoDao()?.getToDoList(taskId = taskDataClass.id)?: toDoEntity
        )
        todoAdapter.notifyDataSetChanged()
    }

    override fun updateToDoItem(position: Int) {
        var dialogBinding:TodoDialogBinding? = null
        Dialog(requireContext()).apply {
            var dialogBinding = TodoDialogBinding.inflate(layoutInflater)
            setContentView(dialogBinding.root)
            dialogBinding.add.setText(resources.getString(R.string.update))
            show()
            dialogBinding.add.setOnClickListener {
                if (dialogBinding.etTodo.text.toString().isNullOrEmpty()) {
                    dialogBinding.etTodo.error = resources.getString(R.string.Enter_todo_item)
                } else {
                    todoDatabase?.todoDao()?.updateToDoItem(
                        ToDoEntity(
                            taskId = taskDataClass.id,
                            id = toDoEntity[position].taskId?:0,
                            todo = dialogBinding.etTodo.text.toString(),
                            isCompleted = dialogBinding.cbIsCompleted.isChecked
                        )
                    )
                    getToDoList()
                    todoAdapter.notifyDataSetChanged()
                    dismiss()
                }
            }

        }

    }

    override fun deleteToDoItem(position: Int) {
        var alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(resources.getString(R.string.Do_you_want_to_delete_this_todo_item))
        alertDialog.setPositiveButton("Yes") { _, _ ->
            toDoEntity.removeAt(position)
            getToDoList()
        }
        alertDialog.setNegativeButton("no") { _, _ ->
        }
        todoDatabase?.todoDao()?.deleteToDoItem(
            toDoEntity[position]
        )
        alertDialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SingleNotesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SingleNotesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}