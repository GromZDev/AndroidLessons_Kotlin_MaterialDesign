package q3_kotlin.material_design.myPictureOfTheDay.viewModel.mainViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import q3_kotlin.material_design.myPictureOfTheDay.repository.NotesRepository
import q3_kotlin.material_design.myPictureOfTheDay.repository.NotesRepositoryImpl
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.NotesAppState

class NotesViewModel(
    private val notesLiveDataToObserve: MutableLiveData<NotesAppState> =
        MutableLiveData(),
    private val notesImpl: NotesRepository = NotesRepositoryImpl()
) : ViewModel() {

    fun getLiveData() = notesLiveDataToObserve

    fun getDataFromLocalSource() {
        notesLiveDataToObserve.value = NotesAppState.Loading(null)
        Thread {
            Thread.sleep(1000) // Теперь берем данные из списков:
            notesLiveDataToObserve.postValue(
                NotesAppState.Success(notesImpl.getAllNotesFromStorage()))

        }.start()
    }

}