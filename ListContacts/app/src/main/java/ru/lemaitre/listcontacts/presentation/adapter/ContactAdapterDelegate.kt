package ru.lemaitre.listcontacts.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.lemaitre.listcontacts.R
import ru.lemaitre.listcontacts.data.Contact


class ContactAdapterDelegate(
    private val onItemClicked: (contact: Contact) -> Unit
) :
    AbsListItemAdapterDelegate<Contact, Contact, ContactAdapterDelegate.ContactHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ContactHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_contact, parent, false)
        return ContactHolder(
            view,
            onItemClicked
        )
    }

    override fun isForViewType(
        item: Contact,
        items: MutableList<Contact>,
        position: Int
    ): Boolean {
        return item is Contact
    }

    override fun onBindViewHolder(
        item: Contact,
        holder: ContactHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)

    }

    class ContactHolder(
        override val containerView: View,
        onItemClicked: (contact: Contact) -> Unit
    ) : BaseHolder(containerView, onItemClicked) {
        fun bind(contact: Contact) {
            bindMainInfo(contact)
        }

    }
}