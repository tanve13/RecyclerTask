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

    @Query("DELETE FROM TaskDataClass where id = id")
   fun deleteTodo(taskDataClass: TaskDataClass)

    @Query("SELECT * FROM TaskDataClass")
    fun getList():List<TaskDataClass>
}