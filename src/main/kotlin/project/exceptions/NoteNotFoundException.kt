package project.exceptions

class NoteNotFoundException: RuntimeException("Note doesn't exist or deleted") {
}