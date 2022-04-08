package project.note

data class NoteComment(
    val id: Int = 0,
    val noteId: Int = 0,
    val ownerId: Int = 0,
    val replyTo: Int? = 0,
    val message: String,
    val date: Int = 0,
    val deleted: Boolean = false
) {
}