package com.tanveer.recyclertask

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanveer.recyclertask.databinding.ActivityMainBinding
import com.tanveer.recyclertask.databinding.CustomDialogLayoutBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    var list = arrayListOf<TaskDataClass>()
    var adapter: TaskRecyclerAdapter = TaskRecyclerAdapter(list)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
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
                } else {
                     list.add(TaskDataClass(
                         dialogBinding.etTitle.text.toString(),
                         dialogBinding.etDescription.text.toString()
                     ))
                    dialogBinding.rbLow.setOnClickListener{
                        binding?.recyclerView?.setBackgroundColor(Color.RED)
                    }
                    dialogBinding.rbMedium.setOnClickListener{
                        binding?.recyclerView?.setBackgroundColor(Color.BLUE)
                    }
                    dialogBinding.rbHigh.setOnClickListener{
                        binding?.recyclerView?.setBackgroundColor(Color.YELLOW)
                    }

                    dialog.dismiss()
                }

            }
        }
    }
}