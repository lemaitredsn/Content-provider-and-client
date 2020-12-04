package ru.lemaitre.clientmycontentprovider.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.lemaitre.clientmycontentprovider.R
import ru.lemaitre.clientmycontentprovider.data.Course



class CourseAdapterDelegate(
) :
    AbsListItemAdapterDelegate<Course, Course, CourseAdapterDelegate.CourseHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): CourseHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_course, parent, false)
        return CourseHolder(
            view
        )
    }

    override fun isForViewType(
        item: Course,
        items: MutableList<Course>,
        position: Int
    ): Boolean {
        return item is Course
    }

    override fun onBindViewHolder(
        item: Course,
        holder: CourseHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)

    }

    class CourseHolder(
        override val containerView: View
    ) : BaseHolder(containerView) {
        fun bind(course: Course) {
            bindMainInfo(course)
        }

    }
}