package com.tanveer.recyclertask

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanveer.recyclertask.databinding.ActivityMainBinding
import com.tanveer.recyclertask.databinding.CustomDialogLayoutBinding
import com.tanveer.recyclertask.databinding.ItemTaskBinding

class MainActivity : AppCompatActivity(), TaskInterface {
    var binding: ActivityMainBinding? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var todoDatabase: TodoDatabase
    var list = arrayListOf<TaskDataClass>()
    var adapter = TaskRecyclerAdapter(this, list, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        todoDatabase = TodoDatabase.getInstance(this)
        linearLayoutManager = LinearLayoutManager(this)
        binding?.recyclerView?.layoutManager = linearLayoutManager
        binding?.recyclerView?.adapter = adapter
        binding?.btnFab?.setOnClickListener {
            var dialog = Dialog(this)
            var dialogBinding = CustomDialogLayoutBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            getWindow()?.setLayout(
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
                        this@MainActivity,
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
                    list.add(
                        TaskDataClass(
                           priority = priority,
                            title = dialogBinding.etTitle.text.toString(),
                            description = dialogBinding.etDescription.text.toString()
                        )
                    )
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
        Toast.makeText(this, "update clicked", Toast.LENGTH_SHORT).show()
        Dialog(this).apply {
            var dialogBinding = CustomDialogLayoutBinding.inflate(layoutInflater)
            setContentView(dialogBinding.root)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            show()
            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.etTitle.text.toString().isEmpty()) {
                    dialogBinding.etTitle.error = resources.getString(R.string.enter_title)
                } else if (dialogBinding.etDescription.text.toString().isEmpty()) {
                    dialogBinding.etDescription.error =
                        resources.getString(R.string.enter_description)
                } else if (dialogBinding.radioGroup.checkedRadioButtonId == -1) {
                    Toast.makeText(
                        this@MainActivity,
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
                    list.set(
                        position, TaskDataClass(
                           priority =  priority,
                          title =   dialogBinding.etTitle.text.toString(),
                           description =  dialogBinding.etDescription.text.toString(),

                            )
                    )
                    todoDatabase.todoDao().updateToDo(
                        TaskDataClass(
                            id = 0
                        )
                    )
                    getList()
                    adapter.notifyDataSetChanged()
                    dismiss()
                }
            }
        }

    }
    fun getList(){
    list.addAll(todoDatabase.todoDao().getList())
        adapter.notifyDataSetChanged()
    }


    override fun deleteTask(position: Int){
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(resources.getString(R.string.Do_you_want_to_delete_this_item))
            alertDialog.setPositiveButton("Yes") { _, _ ->
                list?.removeAt(position)
                adapter.notifyDataSetChanged()
            }
            alertDialog.setNegativeButton("no") { _, _ ->
            }
         todoDatabase.todoDao().delete(
             TaskDataClass(

                 )
         )
        alertDialog.show()
        }
    }



