package ru.lemaitre.listcontacts.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_contact.*
import kotlinx.android.synthetic.main.item_contact.view.*
import ru.lemaitre.listcontacts.R
import ru.lemaitre.listcontacts.data.Contact

abstract class BaseHolder(
    view: View,
    onItemClicked: (contact: Contact) -> Unit
) : RecyclerView.ViewHolder(view), LayoutContainer {

    private lateinit var mContact:Contact


    init {
        view.setOnClickListener {
            onItemClicked(mContact)
        }
    }

    protected fun bindMainInfo(
//            name:String,
//            number: String
        contact: Contact
    ) {

        nameTextView.text = contact.name
        numberTextView.text = contact.phones.joinToString("\n")
        mContact = contact
    }


}