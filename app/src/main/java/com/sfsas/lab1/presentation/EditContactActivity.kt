package com.sfsas.lab1.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sfsas.lab1.R
import com.sfsas.lab1.data.db.datastore.ContactDao
import com.sfsas.lab1.data.db.repository.ContactRepositoryImpl
import com.sfsas.lab1.databinding.ActivityEditContactBinding
import com.sfsas.lab1.domain.entities.Contact
import com.sfsas.lab1.domain.repositories.ContactRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EditContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditContactBinding
    private lateinit var repository: ContactRepository
    private val compasiteDisposable = CompositeDisposable()
    private var contactId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contactId = intent.getIntExtra("contactId", -1)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_edit_contact
        )
        repository = ContactRepositoryImpl((application as Lab1Application).database.contactDao())
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        if (contactId != -1) {
            val disposable = repository.get(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { contact ->
                    binding.name.setText(contact.name)
                    binding.number.setText(contact.number)
                }
            compasiteDisposable.add(disposable)
        }

        binding.save.setOnClickListener {
            val contact = Contact(
                0,
                binding.name.text.toString(),
                binding.number.text.toString()
            )

            val disposable = repository.save(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

            compasiteDisposable.add(disposable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compasiteDisposable.dispose()
    }
}
