package com.tanveer.recyclertask

import java.text.FieldPosition

interface TaskInterface {

    fun updateTask(position: Int)
    fun deleteTask(position: Int)
    fun itemClick(position: Int)
}