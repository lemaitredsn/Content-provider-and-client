package ru.lemaitre.listcontacts.presentation.add

import android.app.Application
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response.error
import ru.lemaitre.listcontacts.R
import ru.lemaitre.listcontacts.data.ContactRepository
import ru.lemaitre.listcontacts.data.InccorrectFormException
import ru.lemaitre.listcontacts.utils.SingleLiveEvent

class ContactAddViewModel(application: Application) : AndroidViewModel(application) {
    val contactRepository = ContactRepository(application)

    private val saveSuccessLiveEvent = SingleLiveEvent<Unit>()
    private val saveErrorLiveEvent = SingleLiveEvent<Int>()

    val saveSuccessLiveData: LiveData<Unit>
        get() = saveSuccessLiveEvent

    val saveErrorLiveData: LiveData<Int>
        get() = saveErrorLiveEvent

    fun save(name: String, phone: String, email: String) {
        viewModelScope.launch {
            try {
                contactRepository.saveContact(name, phone, email)
                saveSuccessLiveEvent.postValue(Unit)
            } catch (t: Throwable) {
                Log.e("ContactAddViewModel", "contact add error", t)
                showError(t)
            }
        }
    }

    private fun showError(t: Throwable) {
        saveErrorLiveEvent.postValue(
            when (t) {
                is InccorrectFormException -> R.string.error_show
                else -> R.string.exception_error
            }
        )
    }


}