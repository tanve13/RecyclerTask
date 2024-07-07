package com.tanveer.recyclertask

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDaoInterface {
    @Insert
    fun insertToDo(taskDataClass: TaskDataClass)

    @Update
    fun updateToDo(taskDataClass: TaskDataClass)

    @Delete
    fun deleteToDo(taskDataClass: TaskDataClass)
/*
    @Query("DELETE FROM TaskDataClass where id = :id")
   fun deleteTodo(taskDataClass: TaskDataClass)*/

    @Query("SELECT * FROM TaskDataClass WHERE priority =  :priority")
    fun taskAccPriority(priority: Int) : List<TaskDataClass>

    @Query("SELECT * FROM TaskDataClass")
    fun getList():List<TaskDataClass>

    @Insert
    fun insertToDoItem(toDoEntity: ToDoEntity)

    @Query("SELECT * FROM ToDoEntity where taskId= :taskId")
    fun getToDoList(taskId: Int):List<ToDoEntity>

    @Update
    fun updateToDoItem(toDoEntity: ToDoEntity)

    @Delete
    fun deleteToDoItem(toDoEntity: ToDoEntity)
}