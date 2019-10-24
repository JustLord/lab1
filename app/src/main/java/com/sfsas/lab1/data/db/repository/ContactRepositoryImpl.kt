package com.sfsas.lab1.data.db.repository

import com.sfsas.lab1.data.db.datastore.ContactDao
import com.sfsas.lab1.data.db.entities.ContactDataModel
import com.sfsas.lab1.domain.entities.Contact
import com.sfsas.lab1.domain.repositories.ContactRepository
import io.reactivex.Completable
import io.reactivex.Observable

class ContactRepositoryImpl(private val contractDao: ContactDao) : ContactRepository {

    override fun get(id: Int): Observable<Contact> {
        return contractDao.get(id)
            .map { dao -> Contact(dao.id, dao.name, dao.number) }
            .toObservable()
    }

    override fun getAll(): Observable<List<Contact>> {
        return contractDao.getAll()
            .map { list -> list.map { Contact(it.id, it.name, it.number) } }
            .toObservable()
    }

    override fun save(contact: Contact): Completable {
        return contractDao.insert(ContactDataModel(contact.id, contact.name, contact.number))
    }
}