package ru.lemaitre.clientmycontentprovider.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class Repository(
        val context: Context
) {
    suspend fun getAllCourses(): List<Course> = withContext(Dispatchers.IO) {
        context.contentResolver.query(
                bUri,
                null,
                null,
                null,
                null
        )?.use {
            getCoursesFromCursor(it)
        }.orEmpty()
    }

    private fun getCoursesFromCursor(cursor: Cursor): List<Course> {
        if (!cursor.moveToFirst()) return emptyList()
        val list = mutableListOf<Course>()
        do {
            val titleIndex = cursor.getColumnIndex("title")
            val title = cursor.getString(titleIndex).orEmpty()

            val idIndex = cursor.getColumnIndex("id")
            val id = cursor.getLong(idIndex)

            list.add(Course(id = id, title = title))
        } while (cursor.moveToNext())
        return list
    }

    suspend fun deleteCourseById(id: Long) {
        val list = getAllCourses()
        if (list.isEmpty()) return
        if (!list.any { it.id == id }) return
        val uriDelete = Uri.parse(baseUri + "/${id.toString()}")
        context.contentResolver.delete(uriDelete,
                null,
                null)
    }

    suspend fun deleteAllCourses() {
        val list = getAllCourses()
        list.forEach {
            val uri = Uri.parse(baseUri + "/${it.id}")
            context.contentResolver.delete(uri, null, null)
        }
    }

    suspend fun addCourse(title: String) = withContext(Dispatchers.IO) {
        if (title.isNullOrBlank()) return@withContext
        val list = getAllCourses()
        if (list.any { it.title == title }) return@withContext
        var courseId = list.maxBy { it.id }?.id ?: 0
        saveTitle(courseId + 1, title)
    }


    private fun saveTitle(courseId: Long, title: String) {
        val contentValues = ContentValues().apply {
            //TODO заменить!
            put("id", courseId)
            put("title", title)
        }
        context.contentResolver.insert(bUri, contentValues)
    }

    fun updateCourseById(id: Long, nameCourse: String) {
        val uriUpdate = Uri.parse(baseUri + "/${id}")
        val contentValues = ContentValues().apply {
            put("id", id)
            put("title", nameCourse)
        }
        context.contentResolver.update(uriUpdate, contentValues, null, null)
    }

    suspend fun getCourseById(id: Long): List<Course> = withContext(Dispatchers.IO) {
        context.contentResolver.query(
                bUri,
                null,
                "id =?",
                arrayOf(id.toString()),
                null
        )?.use {
            getCourseFromCursor(it, id)
        }.orEmpty()
    }

    private fun getCourseFromCursor(cursor: Cursor, id:Long): List<Course> {
        if (!cursor.moveToFirst()) return emptyList()
        val list = mutableListOf<Course>()
        do {
            val titleIndex = cursor.getColumnIndex("title")
            val title = cursor.getString(titleIndex).orEmpty()

            val idIndex = cursor.getColumnIndex("id")
            val id = cursor.getLong(idIndex)

            list.add(Course(id = id, title = title))
        } while (cursor.moveToNext())
        return list.filter { it.id == id }
    }
}
