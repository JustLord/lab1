package com.sfsas.lab1.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Contact")
data class ContactDataModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String = "",
    val number: String = ""
)