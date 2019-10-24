package com.sfsas.lab1.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sfsas.lab1.R
import com.sfsas.lab1.data.db.repository.ContactRepositoryImpl
import com.sfsas.lab1.databinding.ActivityMainBinding
import com.sfsas.lab1.domain.repositories.ContactRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: ContactRepository
    private lateinit var adapter: ContactsAdapter
    private val compasiteDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        repository = ContactRepositoryImpl((application as Lab1Application).database.contactDao())
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        adapter = ContactsAdapter()
        binding.contactsList.layoutManager = LinearLayoutManager(this)
        binding.contactsList.adapter = adapter
        binding.floatingActionButton.setOnClickListener{
            val intent = Intent(this, EditContactActivity::class.java)
            intent.putExtra("contactId", -1)
            startActivity(intent)
        }

        val disposable = repository.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { contracts ->
                adapter.setValue(contracts)
            }
        compasiteDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compasiteDisposable.dispose()
    }
}
