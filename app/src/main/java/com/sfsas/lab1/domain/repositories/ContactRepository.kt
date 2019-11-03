package com.sfsas.lab1.domain.repositories

import com.sfsas.lab1.domain.entities.Contact
import io.reactivex.Completable
import io.reactivex.Observable

interface ContactRepository {
    fun get(id: Int): Observable<Contact>
    fun getAll(): Observable<List<Contact>>
    fun save(contact: Contact) : Completable
    fun delete(id: Int) : Completable
}