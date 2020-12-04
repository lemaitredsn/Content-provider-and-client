package ru.lemaitre.clientmycontentprovider.data

import android.net.Uri

const val AUTHORITIES = "ru.lemaitre.listcontacts.provider"
private const val PATH_COURSES = "courses"
//"content://$AUTHORITIES/$PATH_COURSES/$id"
const val baseUri: String = "content://$AUTHORITIES/$PATH_COURSES"

val bUri = Uri.parse(baseUri)
