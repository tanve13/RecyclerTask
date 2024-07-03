package com.tanveer.recyclertask

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanveer.recyclertask.databinding.ActivityMainBinding
import com.tanveer.recyclertask.databinding.CustomDialogLayoutBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    var list = arrayListOf<TaskDataClass>()
    var adapter = TaskRecyclerAdapter(this,list,this)


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
                } else if(dialogBinding.radioGroup.checkedRadioButtonId == -1){
                    Toast.makeText(this@MainActivity,
                        resources.getString(R.string.select_priority),
                        Toast.LENGTH_SHORT).show()
                }
                else { var priority = if (dialogBinding.rbHigh.isChecked)
                    2
                    else if (dialogBinding.rbMedium.isChecked)  {
                    1
                } else{
                    0
                }
                     list.add(TaskDataClass(
                         priority,
                         dialogBinding.etTitle.text.toString(),
                         dialogBinding.etDescription.text.toString()
                       )
                     )

                  adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }

            }
        }
    }


}