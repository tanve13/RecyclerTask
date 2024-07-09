package com.tanveer.recyclertask

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar


class TaskRecyclerAdapter(
    var context: Context,
    var list: ArrayList<TaskList>,
    var taskInterface: TaskInterface
) : RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder>() {


    private val TAG = "adapter"

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var tvDescription: TextView = view.findViewById(R.id.tvDescription)
        var tvDate: TextView = view.findViewById(R.id.tvDate)
        var btnUpdate: Button = view.findViewById(R.id.btnUpdate)
        var btnDelete: Button = view.findViewById(R.id.btndelete)
        var todo :TextView = view.findViewById(R.id.tvToDO)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_task,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e(TAG, "onBindViewHolder:$position")
        holder.tvTitle.setText(list[position].taskDataClass.title)
        holder.tvDescription.setText(list[position].taskDataClass.description)
        var createdDate = Calendar.getInstance()
        createdDate.time = list[position].taskDataClass.createdDate
        holder.tvDate.setText(SimpleDateFormat("dd/MMM/YYYY").format(createdDate.time))
        when (list[position].taskDataClass.priority) {
            0 -> {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
            }

            1 -> {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.blue))
            }

            2 -> {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
            }
        }
        holder.btnUpdate.setOnClickListener {
            taskInterface.updateTask(position)
        }
        holder.btnDelete.setOnClickListener {
            taskInterface.deleteTask(position)
        }
        holder.itemView.setOnClickListener {
            taskInterface.itemClick(position)
        }
        holder.todo.setText(
            list[position].todo.toString()
        )
    }


}