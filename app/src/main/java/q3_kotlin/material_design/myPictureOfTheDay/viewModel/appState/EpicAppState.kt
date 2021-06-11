package q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState

import q3_kotlin.material_design.myPictureOfTheDay.model.EpicPhotosData

sealed class EpicAppState {
    data class Success(val serverResponseData: List<EpicPhotosData>) : EpicAppState()
    data class Error(val error: Throwable) : EpicAppState()
    data class Loading(val progress: Int?) : EpicAppState()
}