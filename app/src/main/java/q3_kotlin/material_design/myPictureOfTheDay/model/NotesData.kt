package q3_kotlin.material_design.myPictureOfTheDay.model

import q3_kotlin.material_design.myPictureOfTheDay.R

data class NotesData(
    val img: Int = 0,
    var title: String = "",
    var description: String? = ""
)

fun getMyNotes() = mutableListOf(
    NotesData(0,"Мои Заметки"),
    NotesData( R.drawable.fallout_1,"Первая заметка"),
    NotesData(R.drawable.fallout_4,"", "Описание заметки"),
    NotesData( R.drawable.fallout_2,"Еще заметка", "Третяя заметка"),
    NotesData( R.drawable.fallout_5,"", "Еще описание"),
    NotesData( R.drawable.fallout_3,"Еще одна заметка", "Описание еще одной заметки"),
    NotesData( R.drawable.fallout_6,"Еще одна заметка")
)