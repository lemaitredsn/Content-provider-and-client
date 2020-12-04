package ru.lemaitre.listcontacts.presentation.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.lemaitre.listcontacts.data.ContactRepository

class DetailContactViewModel(application: Application):AndroidViewModel(application) {

    private val contactRepository = ContactRepository(application)

    fun delete(id: Long){
        contactRepository.deleteContact(id)
    }
}