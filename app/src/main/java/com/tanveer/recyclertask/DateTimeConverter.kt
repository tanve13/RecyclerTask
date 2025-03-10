package com.tanveer.recyclertask

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

class DateTimeConverter {
    @TypeConverter
fun fromTimestamp(value: Long?) : Date?{
    return value?.let{Date(it)}
}
    @TypeConverter
    fun dateToTimestamp(date: Date?) : Long?{
        return date?.time?.toLong()
    }
}