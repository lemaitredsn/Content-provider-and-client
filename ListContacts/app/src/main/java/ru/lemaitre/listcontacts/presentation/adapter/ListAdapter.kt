package ru.lemaitre.listcontacts.presentation.adapter


import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import ru.lemaitre.listcontacts.data.Contact
import java.io.Serializable


class ListAdapter(
    private val onItemClicked: (contact: Contact) -> Unit
) : AsyncListDifferDelegationAdapter<Contact>(ContactDiffUtilCallback()), Serializable {

    init {
        delegatesManager
            .addDelegate(
                ContactAdapterDelegate(
                    onItemClicked
                )
            )
    }

    class ContactDiffUtilCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return when {
                oldItem is Contact && newItem is Contact -> oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
}