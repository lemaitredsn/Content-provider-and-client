package ru.lemaitre.clientmycontentprovider.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(text: Int) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}