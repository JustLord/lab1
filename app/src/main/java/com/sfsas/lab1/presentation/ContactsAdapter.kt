package com.sfsas.lab1.presentation

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.sfsas.lab1.databinding.ContactItemLayoutBinding
import com.sfsas.lab1.domain.entities.Contact

class ContactsAdapter(private var items: List<Contact>? = null) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun getItemCount(): Int {
        return items?.count() ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items != null)
            holder.bind(items!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val dataBinding = ContactItemLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(dataBinding)
    }

    fun setValue(items: List<Contact>?) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(private var dataBinding: ContactItemLayoutBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(item: Contact) {
            dataBinding.container.setOnClickListener {
                val intent = Intent(context, EditContactActivity::class.java)
                intent.putExtra("contactId", item.id)
                startActivity(context, intent, null)
            }
            dataBinding.name.text = item.name
            dataBinding.number.text = item.number
            dataBinding.url.text = item.url
        }
    }
}