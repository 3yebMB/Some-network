package dev.m13d.somenet.ui.extentions

import java.text.SimpleDateFormat
import java.util.Locale

private val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US)

fun Long.toDateTime(): String {
    return dateTimeFormat.format(this)
}
