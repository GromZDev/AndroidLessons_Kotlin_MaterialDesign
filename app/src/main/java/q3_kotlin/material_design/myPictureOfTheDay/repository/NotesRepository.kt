package q3_kotlin.material_design.myPictureOfTheDay.repository

import q3_kotlin.material_design.myPictureOfTheDay.model.NotesData

interface NotesRepository {

    fun getAllNotesFromStorage(): List<NotesData>
}