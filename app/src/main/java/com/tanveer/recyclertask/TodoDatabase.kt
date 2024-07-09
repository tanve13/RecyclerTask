package com.tanveer.recyclertask

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.lang.NullPointerException
@TypeConverters(DateTimeConverter::class)
@Database(entities = [TaskDataClass::class,ToDoEntity::class], version = 1, exportSchema = true)
abstract class TodoDatabase:RoomDatabase() {
    abstract fun todoDao(): TodoDaoInterface
    companion object{
        private var todoDatabase: TodoDatabase? = null
        fun getInstance(context: Context): TodoDatabase{
            if(todoDatabase == null){
                todoDatabase = Room.databaseBuilder(context,
                TodoDatabase::class.java,
                "ToDoDataBase").allowMainThreadQueries()
                    .build()
            }
            return todoDatabase!!
        }
    }
}
