package q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState

import q3_kotlin.material_design.myPictureOfTheDay.model.PictureServerResponseData

sealed class PODAppState {
    data class Success(val serverResponseData: PictureServerResponseData) : PODAppState()
    data class Error(val error: Throwable) : PODAppState()
    data class Loading(val progress: Int?) : PODAppState()
}