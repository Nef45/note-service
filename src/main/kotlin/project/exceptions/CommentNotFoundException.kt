package project.exceptions

class CommentNotFoundException: RuntimeException("Comment doesn't exist or deleted") {
}