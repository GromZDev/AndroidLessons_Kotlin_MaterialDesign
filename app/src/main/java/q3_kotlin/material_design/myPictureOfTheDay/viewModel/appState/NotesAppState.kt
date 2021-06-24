package q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState

import q3_kotlin.material_design.myPictureOfTheDay.model.NotesData

sealed class NotesAppState {
    data class Success(val serverResponseData: List<NotesData>) : NotesAppState()
    data class Error(val error: Throwable) : NotesAppState()
    data class Loading(val progress: Int?) : NotesAppState()
}