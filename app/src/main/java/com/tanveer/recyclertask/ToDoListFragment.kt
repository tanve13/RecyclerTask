package com.tanveer.recyclertask

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tanveer.recyclertask.databinding.ActivityMainBinding
import com.tanveer.recyclertask.databinding.CustomDialogLayoutBinding
import com.tanveer.recyclertask.databinding.FragmentTodoListBinding
import com.tanveer.recyclertask.databinding.ItemTaskBinding

class ToDoListFragment : Fragment(), TaskInterface {
    var binding: FragmentTodoListBinding? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var todoDatabase: TodoDatabase
    var list = arrayListOf<TaskDataClass>()
    lateinit var adapter: TaskRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(layoutInflater)
        return (binding?.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoDatabase = TodoDatabase.getInstance(requireContext())
        adapter = TaskRecyclerAdapter(requireContext(), list, this)
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding?.recyclerView?.layoutManager = linearLayoutManager
        binding?.recyclerView?.adapter = adapter
        binding?.rbAll?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getList()
            }
        }
        binding?.Low?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                list.clear()
                list.addAll(todoDatabase.todoDao().taskAccPriority(0))
                adapter.notifyDataSetChanged()
            }
        }
        binding?.Medium?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                list.clear()
                list.addAll(todoDatabase.todoDao().taskAccPriority(1))
                adapter.notifyDataSetChanged()
            }
        }
        binding?.High?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                list.clear()
                list.addAll(todoDatabase.todoDao().taskAccPriority(2))
                adapter.notifyDataSetChanged()
            }
        }
        binding?.btnFab?.setOnClickListener {
            var dialog = Dialog(requireContext())
            var dialogBinding = CustomDialogLayoutBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.getWindow()?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            dialog.show()
            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.etTitle.text.toString().isNullOrEmpty()) {
                    dialogBinding.etTitle.error = resources.getString(R.string.enter_title)
                } else if (dialogBinding.etDescription.text.toString().isNullOrEmpty()) {
                    dialogBinding.etDescription.error =
                        resources.getString(R.string.enter_description)
                } else if (dialogBinding.radioGroup.checkedRadioButtonId == -1) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.select_priority),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    var priority = if (dialogBinding.rbHigh.isChecked)
                        2
                    else if (dialogBinding.rbMedium.isChecked) {
                        1
                    } else {
                        0
                    }
                    /* list.add(
                         TaskDataClass(
                            priority = priority,
                             title = dialogBinding.etTitle.text.toString(),
                             description = dialogBinding.etDescription.text.toString()
                         )
                     )*/
                    todoDatabase.todoDao().insertToDo(
                        TaskDataClass(
                            priority = priority,
                            title = dialogBinding.etTitle.text.toString(),
                            description = dialogBinding.etDescription.text.toString()
                        )
                    )

                    getList()
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }

            }
        }
        getList()
    }

    override fun updateTask(position: Int) {
        Toast.makeText(requireContext(), "update clicked", Toast.LENGTH_SHORT).show()
        Dialog(requireContext()).apply {
            var dialogBinding = CustomDialogLayoutBinding.inflate(layoutInflater)
            setContentView(dialogBinding.root)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            show()
            dialogBinding.etTitle.setText(list[position].title)
            dialogBinding.etDescription.setText(list[position].description)
            when (list[position].priority) {
                0 -> dialogBinding.rbLow.isChecked = true
                1 -> dialogBinding.rbMedium.isChecked = true
                2 -> dialogBinding.rbHigh.isChecked = true
            }
            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.etTitle.text.toString().isEmpty()) {
                    dialogBinding.etTitle.error = resources.getString(R.string.enter_title)
                } else if (dialogBinding.etDescription.text.toString().isEmpty()) {
                    dialogBinding.etDescription.error =
                        resources.getString(R.string.enter_description)
                } else if (dialogBinding.radioGroup.checkedRadioButtonId == -1) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.select_priority),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    var priority = if (dialogBinding.rbLow.isChecked) {
                        1
                    } else if (dialogBinding.rbMedium.isChecked) {
                        2
                    } else if (dialogBinding.rbHigh.isChecked) {
                        3
                    } else {
                        0
                    }
                    /*  list.set(
                          position, TaskDataClass(
                             priority =  priority,
                            title =   dialogBinding.etTitle.text.toString(),
                             description =  dialogBinding.etDescription.text.toString(),

                              )
                      )*/
                    todoDatabase.todoDao().updateToDo(
                        TaskDataClass(
                            id = list[position].id,
                            priority = priority,
                            title = dialogBinding.etTitle.text.toString(),
                            description = dialogBinding.etDescription.text.toString()
                        )
                    )
                    getList()
                    /* adapter.notifyDataSetChanged()*/
                    dismiss()
                }
            }
        }
    }

    fun getList() {
        list.clear()
        list.addAll(todoDatabase.todoDao().getList())
        adapter.notifyDataSetChanged()
    }

    override fun deleteTask(position: Int) {
        var alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(resources.getString(R.string.Do_you_want_to_delete_this_item))
        alertDialog.setPositiveButton("Yes") { _, _ ->
            list?.removeAt(position)
            adapter.notifyDataSetChanged()
        }
        alertDialog.setNegativeButton("no") { _, _ ->
        }
        todoDatabase.todoDao().deleteToDo(
            list[position]
        )
        getList()
        alertDialog.show()
    }

    override fun itemClick(position: Int) {
        var convertToString = Gson().toJson(list[position])
        findNavController().navigate(R.id.singleNotesFragment, bundleOf("notes" to convertToString))

    }


}



