package project.note

import java.util.*

data class Note(
    val id: Int = 0,
    val ownerId: Int = 0,
    val title: String,
    val text: String,
    val date: Int = 0,
    val comments: Int = 0,
    val deleted: Boolean = false
) {
}