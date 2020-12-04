package ru.lemaitre.clientmycontentprovider.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_course.*
import ru.lemaitre.clientmycontentprovider.data.Course


abstract class BaseHolder(
    view: View
) : RecyclerView.ViewHolder(view), LayoutContainer {
    

    protected fun bindMainInfo(
        course: Course
    ) {
        idCourseTextView.text = course.id.toString()
        titleCourseTextView.text = course.title
    }
}