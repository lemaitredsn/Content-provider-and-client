package ru.lemaitre.listcontacts.custom_content_provider

import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.squareup.moshi.Moshi
import ru.lemaitre.listcontacts.BuildConfig

class SkillboxContentProvider : ContentProvider() {

    private lateinit var userPrefs: SharedPreferences
    private lateinit var coursePrefs: SharedPreferences

    private val userAdapter = Moshi.Builder().build().adapter(User::class.java)
    private val courseAdapter = Moshi.Builder().build().adapter(Course::class.java)

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITIES, PATH_USERS, TYPE_USERS)
        addURI(AUTHORITIES, PATH_COURSES, TYPE_COURSES)
        addURI(AUTHORITIES, "$PATH_USERS/#", TYPE_USERS_ID)
        addURI(AUTHORITIES, "$PATH_COURSES/#", TYPE_COURSES_ID)
    }



    override fun onCreate(): Boolean {
        userPrefs = context!!.getSharedPreferences("user_shared_prefs", Context.MODE_PRIVATE)
        coursePrefs = context!!.getSharedPreferences("course_shared_prefs", Context.MODE_PRIVATE)
        return true
    }

    override fun insert(p0: Uri, values: ContentValues?): Uri? {
        values ?: return null
        return when (uriMatcher.match(p0)) {
            TYPE_USERS -> saveUser(values)
            TYPE_COURSES -> saveCourses(values)
            else -> null
        }
    }

    private fun saveCourses(contentValues: ContentValues): Uri? {
        val id = contentValues.getAsLong(COLUMN_COURSE_ID) ?: return null
        val title = contentValues.getAsString(COLUMN_COURSE_TITLE) ?: return null
        val course = Course(id = id, title = title)
        coursePrefs.edit()
            .putString(id.toString(), courseAdapter.toJson(course))
            .commit()
        return Uri.parse("content://$AUTHORITIES/$PATH_COURSES/$id")
    }

    private fun saveUser(contentValues: ContentValues): Uri? {
        val id = contentValues.getAsLong(COLUMN_USER_ID) ?: return null
        val name = contentValues.getAsString(COLUMN_USER_NAME) ?: return null
        val age = contentValues.getAsInteger(COLUMN_USER_AGE) ?: return null
        val user = User(id, name, age)
        userPrefs.edit()
            .putString(id.toString(), userAdapter.toJson(user))
            .commit()
        return Uri.parse("content://$AUTHORITIES/$PATH_USERS/$id")
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            TYPE_USERS -> getAllUsersCursor()
            TYPE_COURSES -> getAllCoursesCursor()
            else -> null
        }
    }

    private fun getAllCoursesCursor(): Cursor {
        val allCourses = coursePrefs.all.mapNotNull {
            val courseJsonString = it.value as String
            courseAdapter.fromJson(courseJsonString)
        }
        val cursor = MatrixCursor(arrayOf(COLUMN_COURSE_ID, COLUMN_COURSE_TITLE))
        allCourses.forEach {
            cursor.newRow()
                .add(it.id)
                .add(it.title)
        }
        return cursor
    }

    private fun getAllUsersCursor(): Cursor {
        val allUsers = userPrefs.all.mapNotNull {
            val userJsonString = it.value as String
            userAdapter.fromJson(userJsonString)
        }
        val cursor = MatrixCursor(arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_AGE))
        allUsers.forEach {
            cursor.newRow()
                .add(it.id)
                .add(it.name)
                .add(it.age)
        }
        return cursor
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        p2: String?,
        p3: Array<out String>?
    ): Int {
        values ?: return 0
        return when (uriMatcher.match(uri)) {
            TYPE_USERS_ID -> updateUser(uri, values)
            TYPE_COURSES_ID -> updateCourse(uri, values)
            else -> 0
        }
    }

    private fun updateCourse(uri: Uri, contentValues: ContentValues): Int {
        val courseId = uri.lastPathSegment?.toLongOrNull().toString() ?: return 0
        return if (coursePrefs.contains(courseId)) {
            saveCourses(contentValues)
            1
        } else {
            0
        }
    }

    private fun updateUser(uri: Uri, contentValues: ContentValues): Int {
        val userId = uri.lastPathSegment?.toLongOrNull().toString() ?: return 0
        return if (userPrefs.contains(userId)) {
            saveUser(contentValues)
            1
        } else {
            0
        }
    }

    override fun delete(uri: Uri, p1: String?, p2: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            TYPE_USERS_ID -> deleteUser(uri)
            TYPE_COURSES_ID -> deleteCourse(uri)
            else -> 0
        }
    }

    private fun deleteCourse(uri: Uri): Int{
        val courseId = uri.lastPathSegment?.toLongOrNull().toString() ?: return 0
        return if(coursePrefs.contains(courseId)){
            coursePrefs.edit()
                .remove(courseId)
                .commit()
            1
        }else{
            0
        }
    }
    private fun deleteUser(uri: Uri): Int {
        val userId = uri.lastPathSegment?.toLongOrNull().toString() ?: return 0
        return if (userPrefs.contains(userId)) {
            userPrefs.edit()
                .remove(userId)
                .commit()
            1
        } else {
            0
        }
    }

    override fun getType(p0: Uri): String? {
        return null
    }


    companion object {
        private const val AUTHORITIES = "${BuildConfig.APPLICATION_ID}.provider"

        private const val PATH_USERS = "users"
        private const val PATH_COURSES = "courses"

        private const val TYPE_USERS = 1
        private const val TYPE_COURSES = 2
        private const val TYPE_USERS_ID = 3
        private const val TYPE_COURSES_ID = 4

        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USER_NAME = "name"
        private const val COLUMN_USER_AGE = "age"

        private const val COLUMN_COURSE_ID = "id"
        private const val COLUMN_COURSE_TITLE = "title"

        val test = "content://$AUTHORITIES/$PATH_COURSES"

    }
}