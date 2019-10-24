package com.sfsas.lab1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sfsas.lab1.data.db.datastore.ContactDao
import com.sfsas.lab1.data.db.entities.ContactDataModel

@Database(entities = [ContactDataModel::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}