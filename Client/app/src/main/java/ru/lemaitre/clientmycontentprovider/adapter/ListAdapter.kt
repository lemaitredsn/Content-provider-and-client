package ru.lemaitre.clientmycontentprovider.adapter


import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import ru.lemaitre.clientmycontentprovider.data.Course
import java.io.Serializable


class ListAdapter(
) : AsyncListDifferDelegationAdapter<Course>(CourseDiffUtilCallback()), Serializable {

    init {
        delegatesManager
            .addDelegate(
                CourseAdapterDelegate()
            )
    }

    class CourseDiffUtilCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return when {
                oldItem is Course && newItem is Course -> oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}