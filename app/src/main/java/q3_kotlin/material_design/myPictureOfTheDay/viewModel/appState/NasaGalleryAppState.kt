package q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState

import q3_kotlin.material_design.myPictureOfTheDay.model.NasaImageGalleryData

sealed class NasaGalleryAppState {
    data class Success(val serverResponseData: NasaImageGalleryData) : NasaGalleryAppState()
    data class Error(val error: Throwable) : NasaGalleryAppState()
    data class Loading(val progress: Int?) : NasaGalleryAppState()
}