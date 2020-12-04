package ru.lemaitre.clientmycontentprovider.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.lemaitre.clientmycontentprovider.data.Course
import ru.lemaitre.clientmycontentprovider.data.Repository

class ClientViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(application)

    private val coursesMutableLiveData = MutableLiveData<List<Course>>()
    val coursesLiveData: LiveData<List<Course>>
        get() = coursesMutableLiveData

    private val isLoadingMLD = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = isLoadingMLD
    
    fun addCourse(title:String){
        viewModelScope.launch { 
            try{
                repository.addCourse(title)
                coursesMutableLiveData.postValue(repository.getAllCourses())
            }catch (t:Throwable){
                Log.e("Client", "add course throwable", t)
            }
        }
    }

    fun deleteById(id:Long){
        viewModelScope.launch {
            try{
                repository.deleteCourseById(id)
                coursesMutableLiveData.postValue(repository.getAllCourses())
            }catch (t:Throwable){
                Log.e("Client", "delete by id fail")
            }
        }
    }

    fun loadList() {
        viewModelScope.launch {
            try {
                isLoadingMLD.postValue(true)

                coursesMutableLiveData.postValue(repository.getAllCourses())
                Log.d("Client", "list = ${repository.getAllCourses()}")

            }catch (t:Throwable){
                Log.e("Client", "list courses error", t)
                coursesMutableLiveData.postValue(emptyList())
            }finally {
                isLoadingMLD.postValue(false)
            }
        }
    }

    fun deleteAllCourses() {
        viewModelScope.launch {
            try {
                repository.deleteAllCourses()
                coursesMutableLiveData.postValue(repository.getAllCourses())
            }catch (t:Throwable){
                Log.e("Client", "fail delete all", t)
            }
        }
    }

    fun updateCourseById(id: Long, nameCourse:String) {
        viewModelScope.launch {
            try {
                repository.updateCourseById(id, nameCourse)
                coursesMutableLiveData.postValue(repository.getAllCourses())
            }catch (t:Throwable){
                Log.e("Client", "fail update course by id", t)
            }
        }
    }

    fun getCourseById(id: Long) {
        viewModelScope.launch {
            try {
                val byId = repository.getCourseById(id)
                coursesMutableLiveData.postValue(byId)
                Log.d("Client", "${repository.getCourseById(id)}")
            }catch (t:Throwable){
                Log.e("Client", "fail get courses by id", t)
            }
        }
    }
}