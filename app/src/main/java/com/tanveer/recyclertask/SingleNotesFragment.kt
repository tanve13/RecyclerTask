package com.tanveer.recyclertask

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
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
class SingleNotesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentSingleNotesBinding? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    var toDoEntity = arrayListOf<ToDoEntity>()
    lateinit var todoAdapter: ToDoItemRecycler
    var todoItemRecycler = ToDoItemRecycler(toDoEntity)
    var taskDataClass = TaskDataClass()
    var todoDatabase: TodoDatabase? = null

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
        todoDatabase = TodoDatabase.getInstance(requireContext())
        arguments?.let {
            var notes = it.getString("notes")
            taskDataClass = Gson().fromJson(notes, TaskDataClass::class.java)
            binding?.tvTitle?.setText(taskDataClass.title)
            binding?.tvDescription?.setText(taskDataClass.description)
            getList()
        }
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding?.rvToDo?.adapter = todoItemRecycler
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
                                id = taskDataClass.id,
                                taskId = taskDataClass.id,
                                todo = dialogBinding.etTodo.text.toString(), isCompleted = true
                            )
                        )
                        toDoEntity.clear()
                        getToDoList()
                        getList()
                        todoAdapter.notifyDataSetChanged()
                        dismiss()

                    }
                }
            }
        }

    }

    private fun getList() {
        todoDatabase?.todoDao()?.getList()
    }

    private fun getToDoList() {
        toDoEntity.clear()
        toDoEntity.addAll(
            todoDatabase?.todoDao()?.getToDoList(taskId = taskDataClass.id)
        )
        todoAdapter.notifyDataSetChanged()
    }

    override fun updateToDoItem(position: Int) {
        Dialog(requireContext()).apply {
            var dialogBinding = TodoDialogBinding.inflate(layoutInflater)
            setContentView(dialogBinding.root)
            show()
            dialogBinding.add.setOnClickListener {
                if (dialogBinding.etTodo.text.toString().isNullOrEmpty()) {
                    dialogBinding.etTodo.error = resources.getString(R.string.Enter_todo_item)
                } else {
                    todoDatabase?.todoDao()?.updateToDoItem(
                        ToDoEntity(
                            id = taskDataClass.id,
                            taskId = toDoEntity[position].taskId,
                            todo = dialogBinding.etTodo.toString(),
                            isCompleted = true
                        )
                    )
                    toDoEntity.clear()
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
            todoAdapter.notifyDataSetChanged()
        }
        alertDialog.setNegativeButton("no") { _, _ ->
        }
        todoDatabase?.todoDao()?.deleteToDoItem(
            toDoEntity[position]
        )
        getToDoList()
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