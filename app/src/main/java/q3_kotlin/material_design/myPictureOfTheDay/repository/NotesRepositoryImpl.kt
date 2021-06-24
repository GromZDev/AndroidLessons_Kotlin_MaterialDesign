package q3_kotlin.material_design.myPictureOfTheDay.repository

import q3_kotlin.material_design.myPictureOfTheDay.model.NotesData
import q3_kotlin.material_design.myPictureOfTheDay.model.getMyNotes

class NotesRepositoryImpl: NotesRepository {
    override fun getAllNotesFromStorage(): List<NotesData> = getMyNotes()
}