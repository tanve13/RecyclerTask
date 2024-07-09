package com.tanveer.recyclertask

import androidx.room.Embedded
import androidx.room.Relation

data class TaskList(
    @Embedded
    var taskDataClass: TaskDataClass,
    @Relation(entity = ToDoEntity::class,
    parentColumn = "id",
    entityColumn = "taskId")
    var todo: List<ToDoEntity>
)
