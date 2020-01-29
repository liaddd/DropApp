package com.liad.droptask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.liad.droptask.models.Address
import com.liad.droptask.models.Bag
import com.liad.droptask.models.Contact

@Database(entities = [Contact::class, Address::class, Bag::class], version = 1)
abstract class DropDatabase : RoomDatabase() {

    abstract fun dropDao(): DropDao

    companion object {

        @Volatile
        private var instance: DropDatabase? = null

        fun getDatabase(context: Context): DropDatabase {
            val tempInstance = instance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                return Room.databaseBuilder(
                    context.applicationContext,
                    DropDatabase::class.java,
                    "drop.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
        }

    }

}