package com.tanveer.recyclertask

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class TaskDataClass(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var priority: Int = 0,
    var title: String? = "",
    var description: String? = "",

    )