package project.note

import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import project.exceptions.CommentNotFoundException
import project.exceptions.NoteNotFoundException

class NotesServiceTest {

    @After
    fun reset() {
        NotesService.resetTest()
    }

    @Test
    fun add_returns_newNoteId() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val note2 = Note(title = "Заметка 2", text = "Текст 2", date = 1649311465)
        val note3 = Note(title = "Заметка 3", text = "Текст 3", date = 1649311475)
        val expectedResult = 3

        NotesService.add(note3)
        NotesService.add(note2)
        val actualResult = NotesService.add(note1)

        assertEquals(expectedResult, actualResult)
    }


    @Test
    fun createComment_returns_newCommentId_and_increases_commentsCounter() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)
        val comment2 = NoteComment(message = "Комментарий 2", date = 1649311450)
        val comment3 = NoteComment(message = "Комментарий 3", date = 1649311470)
        val expectedResultId = 3
        val expectedResultCommentsCounter = 3

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        NotesService.createComment(comment2, 1)
        val actualResultId = NotesService.createComment(comment3, 1)
        val actualResultCommentsCounter = NotesService.getById(1).comments

        assertEquals(expectedResultId, actualResultId)
        assertEquals(expectedResultCommentsCounter, actualResultCommentsCounter)
    }

    @Test(expected = NoteNotFoundException::class)
    fun createComment_throws_NoteNotFoundException_when_noteIdDoesntExist() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)

        NotesService.add(note1)
        NotesService.createComment(comment1, 2)
    }

    @Test(expected = NoteNotFoundException::class)
    fun createComment_throws_NoteNotFoundException_when_noteMarkedAsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)

        NotesService.add(note1)
        NotesService.delete(1)
        NotesService.createComment(comment1, 1)
    }


    @Test
    fun delete_returns_true_when_successfully_markedNoteAndItsCommentsAsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val note2 = Note(title = "Заметка 2", text = "Текст 2", date = 1649311465)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)
        val comment2 = NoteComment(message = "Комментарий 2", date = 1649311450)

        NotesService.add(note1)
        NotesService.add(note2)
        NotesService.createComment(comment1, 2)
        NotesService.createComment(comment2, 2)
        NotesService.delete(2)

        assertTrue(NotesService.getNoteTest(2).deleted)
        assertTrue(NotesService.getCommentTest(1).deleted)
        assertTrue(NotesService.getCommentTest(2).deleted)
    }

    @Test(expected = NoteNotFoundException::class)
    fun delete_throws_NoteNotFoundException_when_noteIdDoesntExist() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        NotesService.add(note1)

        NotesService.delete(3)
    }

    @Test(expected = NoteNotFoundException::class)
    fun delete_throws_NoteNotFoundException_when_noteIsAlreadyMarkedAsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        NotesService.add(note1)

        NotesService.delete(1)
        NotesService.delete(1)
    }


    @Test
    fun deleteComment_returns_true_when_successfully_markedCommentAsDeleted_and_decreases_commentsCounter() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        NotesService.deleteComment(1)

        assertTrue(NotesService.getCommentTest(1).deleted)
        assertEquals(NotesService.getNoteTest(1).comments, 0)
    }

    @Test(expected = CommentNotFoundException::class)
    fun deleteComment_throws_CommentNotFoundException_when_commentIdDoesntExist() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        NotesService.deleteComment(2)
    }

    @Test(expected = CommentNotFoundException::class)
    fun deleteComment_throws_CommentNotFoundException_when_commentIsAlreadyMarkedAsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        NotesService.deleteComment(1)
        NotesService.deleteComment(1)
    }


    @Test
    fun edit_returns_true_when_successfully_replacedOldNoteWithNew() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val note2 = Note(title = "Заметка 2", text = "Текст 2", date = 1649311465)
        val expectedResult = Note(id = 1, title = "Заметка 2", text = "Текст 2", date = 1649311455)

        NotesService.add(note1)
        NotesService.edit(note2, 1)
        val actualResult = NotesService.getNoteTest(1)

        assertEquals(expectedResult, actualResult)
    }

    @Test(expected = NoteNotFoundException::class)
    fun edit_throws_NoteNotFoundException_when_noteIdDoesntExist() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val note2 = Note(title = "Заметка 2", text = "Текст 2", date = 1649311465)

        NotesService.add(note1)
        NotesService.edit(note2, 2)
    }

    @Test(expected = NoteNotFoundException::class)
    fun edit_throws_NoteNotFoundException_when_noteMarkedAsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val note2 = Note(title = "Заметка 2", text = "Текст 2", date = 1649311465)

        NotesService.add(note1)
        NotesService.delete(1)
        NotesService.edit(note2, 1)
    }


    @Test
    fun editComment_returns_true_when_successfully_replacedOldCommentWithNew() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)
        val comment2 = NoteComment(message = "Комментарий 2", date = 1649311450)
        val expectedResult = NoteComment(id = 1, noteId = 1, message = "Комментарий 2", date = 1649311460)

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        NotesService.editComment(comment2, 1)
        val actualResult = NotesService.getCommentTest(1)

        assertEquals(expectedResult, actualResult)
    }

    @Test(expected = CommentNotFoundException::class)
    fun editComment_throws_CommentNotFoundException_when_commentIdDoesntExist() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)
        val comment2 = NoteComment(message = "Комментарий 2", date = 1649311450)

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        NotesService.editComment(comment2, 2)
    }

    @Test(expected = CommentNotFoundException::class)
    fun editComment_throws_CommentNotFoundException_when_commentMarkedAsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)
        val comment2 = NoteComment(message = "Комментарий 2", date = 1649311450)

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        NotesService.deleteComment(1)
        NotesService.editComment(comment2, 1)
    }


    @Test
    fun get_when_trueByDefault_returns_ascendingByDateListOfNotes_withoutNotesMarkedAsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val note2 = Note(title = "Заметка 2", text = "Текст 2", date = 1649311465, deleted = true)
        val note3 = Note(title = "Заметка 3", text = "Текст 3", date = 1649311475)
        val expectedResult: MutableList<Note> = ArrayList()
        expectedResult.add(Note(id = 3, title = "Заметка 1", text = "Текст 1", date = 1649311455))
        expectedResult.add(Note(id = 1, title = "Заметка 3", text = "Текст 3", date = 1649311475))

        NotesService.add(note3)
        NotesService.add(note2)
        NotesService.add(note1)
        val actualResult = NotesService.get()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun get_when_false_returns_descendingByDateListOfNotes_withoutNotesMarkedAsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val note2 = Note(title = "Заметка 2", text = "Текст 2", date = 1649311465, deleted = true)
        val note3 = Note(title = "Заметка 3", text = "Текст 3", date = 1649311475)
        val expectedResult: MutableList<Note> = ArrayList()
        expectedResult.add(Note(id = 3, title = "Заметка 3", text = "Текст 3", date = 1649311475))
        expectedResult.add(Note(id = 1, title = "Заметка 1", text = "Текст 1", date = 1649311455))

        NotesService.add(note1)
        NotesService.add(note2)
        NotesService.add(note3)
        val actualResult = NotesService.get(false)

        assertEquals(expectedResult, actualResult)
    }


    @Test
    fun getById_returns_noteByItsId() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val note2 = Note(title = "Заметка 2", text = "Текст 2", date = 1649311465)
        val note3 = Note(title = "Заметка 3", text = "Текст 3", date = 1649311475)
        val expectedResult = Note(id = 1, title = "Заметка 2", text = "Текст 2", date = 1649311465)

        NotesService.add(note2)
        NotesService.add(note3)
        NotesService.add(note1)
        val actualResult = NotesService.getById(1)

        assertEquals(expectedResult, actualResult)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getById_throws_NoteNotFoundException_when_noteIdDoesntExist() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)

        NotesService.add(note1)
        NotesService.getById(2)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getById_throws_NoteNotFoundException_when_noteMarkedAsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455, deleted = true)

        NotesService.add(note1)
        NotesService.getById(1)
    }


    @Test
    fun getComments_when_falseByDefault_returns_ascendingByDateListOfComments_toNoteByItsId_withoutCommentsMarkedAsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460, deleted = true)
        val comment2 = NoteComment(message = "Комментарий 2", date = 1649311450)
        val comment3 = NoteComment(message = "Комментарий 3", date = 1649311470)
        val expectedResult: MutableList<NoteComment> = ArrayList()
        expectedResult.add(NoteComment(id = 3, noteId = 1, message = "Комментарий 2", date = 1649311450))
        expectedResult.add(NoteComment(id = 2, noteId = 1, message = "Комментарий 3", date = 1649311470))

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        NotesService.createComment(comment3, 1)
        NotesService.createComment(comment2, 1)
        val actualResult = NotesService.getComments(1)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getComments_when_true_returns_descendingByDateListOfComments_toNoteByItsId_withoutCommentsMarkedAsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460, deleted = true)
        val comment2 = NoteComment(message = "Комментарий 2", date = 1649311450)
        val comment3 = NoteComment(message = "Комментарий 3", date = 1649311470)
        val expectedResult: MutableList<NoteComment> = ArrayList()
        expectedResult.add(NoteComment(id = 2, noteId = 1, message = "Комментарий 3", date = 1649311470))
        expectedResult.add(NoteComment(id = 3, noteId = 1, message = "Комментарий 2", date = 1649311450))

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        NotesService.createComment(comment3, 1)
        NotesService.createComment(comment2, 1)
        val actualResult = NotesService.getComments(1, true)

        assertEquals(expectedResult, actualResult)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getComments_throws_NoteNotFoundException_when_noteIdDoesntExist() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        NotesService.getComments(2)
    }

    @Test(expected = CommentNotFoundException::class)
    fun getComments_throws_CommentNotFoundException_when_commentsDontExist() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)

        NotesService.add(note1)
        NotesService.getComments(1)
    }

    @Test(expected = CommentNotFoundException::class)
    fun getComments_throws_CommentNotFoundException_when_allCommentsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460, deleted = true)

        NotesService.add(note1)
        NotesService.getComments(1)
    }

    @Test
    fun restoreComment_returns_true_when_successfully_changedNoteCommentDeletedProperty_and_increases_commentsCounter() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)
        val restoredComment =
            NoteComment(id = 1, noteId = 1, message = "Комментарий 1", date = 1649311460, deleted = false)

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        NotesService.deleteComment(1)
        val actualResult = NotesService.restoreComment(1)

        assertTrue(actualResult)
        assertEquals(restoredComment, NotesService.getCommentTest(1))
        assertEquals(NotesService.getNoteTest(1).comments, 1)
    }

    @Test
    fun restoreComment_returns_false_when_commentWasntMarkedAsDeleted() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        val actualResult = NotesService.restoreComment(1)

        assertFalse(actualResult)
    }

    @Test(expected = CommentNotFoundException::class)
    fun restoreComment_throws_CommentNotFoundException_when_commentIdDoesntExist() {
        val note1 = Note(title = "Заметка 1", text = "Текст 1", date = 1649311455)
        val comment1 = NoteComment(message = "Комментарий 1", date = 1649311460)

        NotesService.add(note1)
        NotesService.createComment(comment1, 1)
        NotesService.restoreComment(2)
    }
}