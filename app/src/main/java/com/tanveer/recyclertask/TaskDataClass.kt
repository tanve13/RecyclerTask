package com.tanveer.recyclertask

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date

@Entity()
data class TaskDataClass(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var priority: Int = 0,
    var title: String? = "",
    var description: String? = "",
    var createdDate: Date?= Calendar.getInstance().time

    )