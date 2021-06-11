package q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState

import q3_kotlin.material_design.myPictureOfTheDay.model.MarsRoverPhotosData

sealed class MarsRoverAppState {
    data class Success(val serverResponseData: MarsRoverPhotosData) : MarsRoverAppState()
    data class Error(val error: Throwable) : MarsRoverAppState()
    data class Loading(val progress: Int?) : MarsRoverAppState()
}