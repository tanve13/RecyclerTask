package com.tanveer.recyclertask

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TaskDataClass::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ToDoEntity(
    @PrimaryKey
    var id: Int = 0,
    var taskId: Int? = 0,
    var todo: String? = null,
    var isCompleted: Boolean? = false
)
