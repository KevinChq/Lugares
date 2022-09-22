package com.lugares.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lugares.model.Lugar
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Lugar::class], version = 1, exportSchema = false)
abstract class LugarDatabase: RoomDatabase() {
    abstract fun lugarDao() : LugarDao

    companion object {
        @Volatile
        private var INSTANCE: LugarDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: android.content.Context) : LugarDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LugarDatabase::class.java,
                    "lugar_databse"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}