package com.sfsas.lab1.presentation

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Patterns
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sfsas.lab1.R
import com.sfsas.lab1.data.db.repository.ContactRepositoryImpl
import com.sfsas.lab1.databinding.ActivityEditContactBinding
import com.sfsas.lab1.domain.entities.Contact
import com.sfsas.lab1.domain.repositories.ContactRepository
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
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_edit_contact
        )
        repository = ContactRepositoryImpl((application as Lab1Application).database.contactDao())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val actionbar = supportActionBar
        actionbar!!.title = "New Activity"
        actionbar.setDisplayHomeAsUpEnabled(true)

        if (contactId != -1) {
            val disposable = repository.get(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { contact ->
                    binding.name.setText(contact.name)
                    binding.number.setText(contact.number)
                    binding.site.setText(contact.url)
                }
            compasiteDisposable.add(disposable)
        } else {
            contactId = 0
        }


        binding.save.setOnClickListener {
            if (contactId == -5)
                return@setOnClickListener

            if (!Patterns.WEB_URL.matcher(binding.site.text.toString()).matches()) {
                Toast.makeText(this, "URL is not valid " + binding.site.text.toString(), Toast.LENGTH_SHORT).show()
                binding.site.requestFocus()
                return@setOnClickListener
            }

            if (Regex("[^A-Za-zа-яА-Я ]+").matches(binding.name.text.toString())) {
                Toast.makeText(this, "Name is not valid " + binding.name.text.toString(), Toast.LENGTH_SHORT).show()
                binding.name.requestFocus()
                return@setOnClickListener
            }

            val contact = Contact(
                contactId,
                binding.name.text.toString(),
                binding.number.text.toString(),
                binding.site.text.toString()
            )

            val disposable = repository.save(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (contactId == 0)
                        contactId = -5

                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                }

            compasiteDisposable.add(disposable)
        }

        binding.delete.setOnClickListener {
            val disposable = repository.delete(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

            compasiteDisposable.add(disposable)
            finish()
        }

        binding.number.setOnLongClickListener {
            val clipboardManager = this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("number", (it as EditText).text.toString())
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(this, "Copped", Toast.LENGTH_SHORT).show()

            true
        }

        binding.site.setOnLongClickListener {
            val clipboardManager = this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("site", (it as EditText).text.toString())
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(this, "Copped", Toast.LENGTH_SHORT).show()

            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compasiteDisposable.dispose()
    }
}
