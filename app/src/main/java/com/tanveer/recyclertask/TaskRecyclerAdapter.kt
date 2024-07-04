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



class TaskRecyclerAdapter(
    var context: Context,
    var list: ArrayList<TaskDataClass>,
    var taskInterface: TaskInterface
): RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder>(){


    private val TAG = "adapter"

    class ViewHolder(var view:View):RecyclerView.ViewHolder(view) {

        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var tvDescription : TextView = view.findViewById(R.id.tvDescription)
        var btnUpdate: Button = view.findViewById(R.id.btnUpdate)
        var btnDelete :Button = view.findViewById(R.id.btndelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_task,
            parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e(TAG,"onBindViewHolder:$position")
    holder.tvTitle.setText(list[position].title)
        holder.tvDescription.setText(list[position].description)
        when(list[position].priority){
            0 ->{
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.red))
            }
            1 ->{
                holder.itemView.setBackgroundColor( ContextCompat.getColor(context,R.color.blue))
            }
            2 ->{
                holder.itemView.setBackgroundColor (ContextCompat.getColor(context,R.color.yellow))
            }
        }
      holder.btnUpdate.setOnClickListener {
         taskInterface.updateTask(position)
      }
        holder.btnDelete.setOnClickListener{
            taskInterface.deleteTask(position)
        }
    }


}