package ru.lemaitre.listcontacts.presentation.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.lemaitre.listcontacts.data.Contact
import ru.lemaitre.listcontacts.data.ContactRepository
import ru.lemaitre.listcontacts.utils.SingleLiveEvent

class ContactListViewModel(application: Application) : AndroidViewModel(application) {

    private val contactRepository = ContactRepository(application)

    private val isLoadingMutableLiveData = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
            get() = isLoadingMutableLiveData

    private val contactsMutableLiveData = MutableLiveData<List<Contact>>()
    val contactsLiveData: LiveData<List<Contact>>
        get() = contactsMutableLiveData

    fun loadList() {
        viewModelScope.launch {
            isLoadingMutableLiveData.postValue(true)
            try {
                contactsMutableLiveData.postValue(contactRepository.getAllContacts())
            }catch (t:Throwable){
                Log.e("ContactListViewModel", "contact list error", t)
                contactsMutableLiveData.postValue(emptyList())
            }finally {
                isLoadingMutableLiveData.postValue(false)
            }
        }
    }

    private val detailInfoMutableLiveData = SingleLiveEvent<Contact>()
    val detailLiveData:LiveData<Contact>
    get() = detailInfoMutableLiveData

    fun getDetailInfo(contact: Contact){
        contact.let { detailInfoMutableLiveData.postValue(it) }
    }
}