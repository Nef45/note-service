package project.note

import project.exceptions.CommentNotFoundException
import project.exceptions.NoteNotFoundException

object NotesService {

    private var listOfNotes: MutableList<Note> = ArrayList()
    private var listOfComments: MutableList<NoteComment> = ArrayList()


    /**
     * Добавляет заметку в [listOfNotes], присваивая ей уникальный id.
     * @param <note> Заметка для добавления в список.
     * @return id добавленной заметки.
     */
    fun add(note: Note): Int {
        val newNote: Note
        if (listOfNotes.isEmpty()) {
            newNote = note.copy(id = 1)
            listOfNotes.add(newNote)
        } else {
            newNote = note.copy(id = listOfNotes.last().id + 1)
            listOfNotes.add(newNote)
        }
        return listOfNotes.last().id
    }

    /**
     * Добавляет комментарий в [listOfComments] по id заметки из [listOfNotes], и присваивает ему уникальный id,
     * а также id заметки, к которой был оставлен комментарий. Увеличивает счетчик комментариев указанной заметки на 1.
     * @param <comment> Комментарий для добавления в список.
     * @param <noteId> id заметки, к которой привязан комментарий.
     * @return id добавленного комментария.
     * @throws <[NoteNotFoundException]> Список заметок пуст, заметки с указанным id не существует либо она уже помечена
     * как удаленная.
     */
    fun createComment(comment: NoteComment, noteId: Int): Int {
        val newComment: NoteComment
        for ((i, noteInList) in listOfNotes.withIndex()) {
            if (noteId == noteInList.id && !noteInList.deleted) {
                if (listOfComments.isEmpty()) {
                    newComment = comment.copy(id = 1, noteId = noteId)
                    listOfComments.add(newComment)
                    listOfNotes[i] = noteInList.copy(comments = noteInList.comments + 1)
                    return listOfComments.last().id
                } else {
                    newComment = comment.copy(id = listOfComments.last().id + 1, noteId = noteId)
                    listOfComments.add(newComment)
                    listOfNotes[i] = noteInList.copy(comments = noteInList.comments + 1)
                    return listOfComments.last().id
                }
            }
        }
        throw NoteNotFoundException()
    }

    /**
     * Находит в [listOfNotes] заметку по id и присваивает ее свойству [Note.deleted], помечающему удаление, значение true.
     * Заметка из списка при этом не удаляется. Аналогично находит в [listOfComments] все комментарии к данной заметке,
     * помечая их удаление в свойстве [NoteComment.deleted]. Комментарии из списка не удаляется.
     * @param <noteId> id удаляемой заметки.
     * @return True при успешной пометке удаления заметки и ее комментариев.
     * @throws <[NoteNotFoundException]> Список заметок пуст, заметки с указанным id не существует либо она уже помечена
     * как удаленная.
     */
    fun delete(noteId: Int): Boolean {
        for ((i, noteInList) in listOfNotes.withIndex()) {
            if (noteInList.id == noteId) {
                if (noteInList.deleted) {
                    throw NoteNotFoundException()
                }
                listOfNotes[i] = noteInList.copy(deleted = true)
                for ((j, commentInList) in listOfComments.withIndex()) {
                    if (commentInList.noteId == noteId) {
                        listOfComments[j] = commentInList.copy(deleted = true)
                    }
                }
                return true
            }
        }
        throw NoteNotFoundException()
    }

    /**
     * Находит в [listOfComments] комментарий по id и присваивает ее свойству [NoteComment.deleted], помечающему удаление,
     * значение true. Комментарий из списка при этом не удаляется. Уменьшает счетчик комментариев указанной заметки на 1.
     * @param <comment> Комментарий для добавления в список.
     * @param <commentId> id удаляемого комментария.
     * @return True при успешном пометке удаления комментария.
     * @throws <[CommentNotFoundException]> Список комментариев пуст, комментария с указанным id не существует либо
     * он уже помечен как удаленный.
     */
    fun deleteComment(commentId: Int): Boolean {
        for ((i, commentInList) in listOfComments.withIndex()) {
            if (commentId == commentInList.id) {
                if (commentInList.deleted) {
                    throw CommentNotFoundException()
                }
                listOfComments[i] = commentInList.copy(deleted = true)
                for ((j, noteInList) in listOfNotes.withIndex()) {
                    if (noteInList.id == commentInList.noteId)
                        listOfNotes[j] = noteInList.copy(comments = noteInList.comments - 1)
                }
                return true
            }
        }
        throw CommentNotFoundException()
    }

    /**
     * Находит в [listOfNotes] заметку по id и обновляет ее свойства на свойства заметки, переданной в параметре,
     * за исключением [Note.id], [Note.ownerId] и [Note.deleted].
     * @param <note> Обновленная заметка.
     * @param <noteId> id заметки, которая будет обновлена.
     * @return True при успешном обновлении заметки.
     * @throws <[NoteNotFoundException]> Список заметок пуст, заметки с указанным id не существует либо она помечена
     * как удаленная.
     */
    fun edit(note: Note, noteId: Int): Boolean {
        for ((i, noteInList) in listOfNotes.withIndex()) {
            if (noteInList.id == noteId) {
                if (noteInList.deleted) {
                    throw NoteNotFoundException()
                }
                listOfNotes[i] = note.copy(
                    id = noteInList.id,
                    ownerId = noteInList.ownerId,
                    date = noteInList.date,
                    deleted = false
                )
                return true
            }
        }
        throw NoteNotFoundException()
    }

    /**
     * Находит в [listOfComments] комментарий по id и обновляет его свойства на свойства комментария, переданного в параметре,
     * за исключением [NoteComment.id], [NoteComment.noteId], [NoteComment.ownerId] и [NoteComment.deleted].
     * @param <comment> Обновленный комментарий.
     * @param <commentId> id комментарий, который будет обновлен.
     * @return True при успешном обновлении комментария.
     * @throws <[CommentNotFoundException]> Список комментариев пуст, комментария с указанным id не существует либо
     * он помечен как удаленный.
     */
    fun editComment(comment: NoteComment, commentId: Int): Boolean {
        for ((i, commentInList) in listOfComments.withIndex()) {
            if (commentInList.id == commentId) {
                if (commentInList.deleted) {
                    throw CommentNotFoundException()
                }
                listOfComments[i] = comment.copy(
                    id = commentInList.id,
                    noteId = commentInList.noteId,
                    ownerId = commentInList.ownerId,
                    date = commentInList.date,
                    deleted = false
                )
                return true
            }
        }
        throw CommentNotFoundException()
    }

    /**
     * Сортирует по дате создания заметки из [listOfNotes] и возвращает их в виде списка.
     * @param <sort>
     * true - сортировка заметок по дате создания в порядке возрастания,
     * false - сортировка заметок по дате создания в порядке убывания.
     * @return Отсортированный список заметок.
     */
    fun get(sort: Boolean = true): MutableList<Note> {
        val actualNoteList: MutableList<Note> = listOfNotes
        for (noteInList in actualNoteList) {
            if (noteInList.deleted) {
                actualNoteList.remove(noteInList)
            }
        }
        return if (sort) {
            actualNoteList.sortedBy { it.date } as MutableList<Note>
        } else {
            actualNoteList.sortedByDescending { it.date } as MutableList<Note>
        }
    }

    /**
     * Возвращает заметку из [listOfNotes] по ее id.
     * @param <noteId> id заметки
     * @return Заметка по ее id.
     * @throws <[NoteNotFoundException]> Список заметок пуст, заметки с указанным id не существует либо она помечена
     * как удаленная.
     */
    fun getById(noteId: Int): Note {
        for (noteInList in listOfNotes) {
            if (noteInList.deleted) {
                throw NoteNotFoundException()
            }
            if (noteInList.id == noteId) {
                return noteInList
            }
        }
        throw NoteNotFoundException()
    }

    /**
     * Возвращает список комментариев из [listOfComments] id заметки с сортировкой по дате создания.
     * @param <noteId> id заметки
     * @param <sort>
     * true - сортировка комментариев по дате создания в порядке убывания,
     * false - сортировка комментариев по дате создания в порядке возрастания.
     * @return Отсортированный список комметнтариев.
     * @throws <[NoteNotFoundException]> Список заметок пуст, указанного id заметки для поиска комментариев не существует
     * либо заметка помечена как удаленная.
     * @throws <[CommentNotFoundException]> Список комментариев пуст, комментариев по указанному id заметки не существует
     * либо они помечены как удаленные.
     */
    fun getComments(noteId: Int, sort: Boolean = false): MutableList<NoteComment> {
        val actualListOfComments: MutableList<NoteComment> = ArrayList()

        for (commentInList in listOfComments) {
            if (commentInList.noteId == noteId && !commentInList.deleted) {
                actualListOfComments.add(commentInList)
            }
        }
        for (noteInList in listOfNotes) {
            var i: Int = 0
            if (noteInList.id != noteId) {
                i++
            }
            if (i == listOfNotes.size) {
                throw NoteNotFoundException()
            }
        }
        if (actualListOfComments.isEmpty()) {
            throw CommentNotFoundException()
        }

        return if (!sort) {
            actualListOfComments.sortedBy { it.date } as MutableList<NoteComment>
        } else {
            actualListOfComments.sortedByDescending { it.date } as MutableList<NoteComment>
        }
    }

    /**
     * Находит комментарий в [listOfComments] по его id и убирает отметку об удалении [NoteComment.deleted], при этом
     * увеличивая счетчик комментариев указанной заметки на 1.
     * @param <commentId> id комментария, подлежащего восстановлению
     * @return
     * true -  пометка об удалении комментария успешно изменена с true на false,
     * false - комментарий не был помечен как удаленный
     * @throws <[CommentNotFoundException]> Список комментариев пуст либо комментария c указанным id не существует
     */
    fun restoreComment(commentId: Int): Boolean {
        for ((i, commentInList) in listOfComments.withIndex()) {
            if (commentInList.id == commentId) {
                if (commentInList.deleted) {
                    listOfComments[i] = commentInList.copy(deleted = false)
                    for ((j, noteInList) in listOfNotes.withIndex()) {
                        if (noteInList.id == commentInList.noteId) {
                            listOfNotes[j] = noteInList.copy(comments = noteInList.comments + 1)
                        }
                    }
                    return true
                } else return false
            }
        }
        throw CommentNotFoundException()
    }


    // === FUNCTIONS FOR UNIT TESTS ===

    fun resetTest() {
        listOfNotes.removeAll(listOfNotes)
        listOfComments.removeAll(listOfComments)
    }

    fun getNoteTest(noteId: Int): Note {
        for (listOfNote in listOfNotes) {
            if (listOfNote.id == noteId)
                return listOfNote
        }
        return Note(title = "", text = "")
    }

    fun getCommentTest(commentId: Int): NoteComment {
        for (listOfComment in listOfComments) {
            if (listOfComment.id == commentId)
                return listOfComment
        }
        return NoteComment(message = "")
    }
}