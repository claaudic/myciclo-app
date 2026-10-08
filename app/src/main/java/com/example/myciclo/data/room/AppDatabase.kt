package com.example.myciclo.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        CicloEntity::class,
        RegistroDiarioEntity::class,
        RegistroEvaEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cicloDao(): CicloDao
    abstract fun registroDiarioDao(): RegistroDiarioDao
    abstract fun registroEvaDao(): RegistroEvaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton: toda la app usa la misma instancia de la base de datos.
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "myciclo.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
