package ru.lemaitre.listcontacts.presentation.save

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.lemaitre.listcontacts.data.FileRepository

class FileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FileRepository(application)

    private val loadingMutableLiveData = MutableLiveData(false)
    val loadingLiveData: LiveData<Boolean>
        get() = loadingMutableLiveData


    private var stateMutableLiveData = MutableLiveData<String>()
    val stateLiveData: LiveData<String>
        get() = stateMutableLiveData


    fun downloadFile(link: String) {
        viewModelScope.launch {
            loadingMutableLiveData.postValue(true)
            try {
                repository.downloadFile(link = link)
                //if success saveInSharedPreferences
                repository.saveInSharedPreference(link = link)
            } catch (t: Throwable) {
                Log.e("TAG", "exception ${t.message}", t)
            } finally {
                loadingMutableLiveData.postValue(false)
                stateMutableLiveData.postValue(repository.getState())
            }
        }
    }

    val uriMutableLiveData = MutableLiveData<Uri>()
    val uriLiveData: LiveData<Uri>
        get() = uriMutableLiveData

    fun shareFile() {
        viewModelScope.launch {
            try {
                val uri = repository.shareFile()
                uriMutableLiveData.postValue(uri)
            } catch (t: Throwable) {
                Log.e("shareFile", "error share file", t)

            }
        }
    }

}