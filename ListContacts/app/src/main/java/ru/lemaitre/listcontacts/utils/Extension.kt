package ru.lemaitre.listcontacts.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.lemaitre.listcontacts.R

fun Fragment.toast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(text: Int) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}