package com.tanveer.recyclertask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoItemRecycler(
    var todoEntity: ArrayList<ToDoEntity>,
    var toDoInterface: ToDoInterface
) :
    RecyclerView.Adapter<ToDoItemRecycler.ViewHolder>() {
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var tvTodo = view.findViewById<TextView>(R.id.tvToDO)
        var cbIsCompleted = view.findViewById<CheckBox>(R.id.cbIsCompleted)
        var btnToDoUpdate = view.findViewById<Button>(R.id.btnToDoUpdate)
        var btnToDoDelete = view.findViewById<Button>(R.id.btnToDoDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_todo_recycler,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoEntity.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTodo.setText(todoEntity[position].todo.toString())
        if (todoEntity[position].isCompleted == true) {
            holder.cbIsCompleted.isChecked
        }
        holder.btnToDoUpdate.setOnClickListener {
            toDoInterface.updateToDoItem(position)
        }
        holder.btnToDoDelete.setOnClickListener {
            toDoInterface.deleteToDoItem(position)
        }
    }
}