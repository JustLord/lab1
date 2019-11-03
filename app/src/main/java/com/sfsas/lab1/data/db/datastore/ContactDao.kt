package com.sfsas.lab1.data.db.datastore

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.sfsas.lab1.data.db.entities.ContactDataModel
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface ContactDao {
    @Query("SELECT * FROM Contact")
    fun getAll(): Flowable<List<ContactDataModel>>

    @Query("SELECT * FROM Contact WHERE id = :id LIMIT 1")
    fun get(id: Int) : Flowable<ContactDataModel>

    @Insert(onConflict = REPLACE)
    fun insert(contact: ContactDataModel) : Completable

    @Delete
    fun delete(contact: ContactDataModel) : Completable
}

