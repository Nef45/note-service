**Опущенные поля:**

**1. data class Note**

1. *readComments*  
(Int) Количество прочитанных комментариев (только при запросе информации о заметке текущего пользователя).

2. *viewUrl*  
(String) URL страницы для отображения заметки.

3. *privacyView*  
(String[]) Настройки приватности просмотра заметки

4. *privacyComment*  
(String[]) Настройки приватности комментирования заметки

5. *canComment*  
(Int) Есть ли возможность оставлять комментарии

6. *textWiki*  
(String) Тэги ссылки на вики

**2. data class NoteComment**

1. *guid*  
(String) Уникальный идентификатор, предназначенный для предотвращения повторной отправки одинакового комментария.
