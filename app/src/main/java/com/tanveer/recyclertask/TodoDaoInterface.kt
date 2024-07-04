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
   fun delete(taskDataClass: TaskDataClass)

    @Query("SELECT * FROM TaskDataClass")
    fun getList():List<TaskDataClass>
}