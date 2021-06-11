package q3_kotlin.material_design.myPictureOfTheDay.viewModel.mainViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import q3_kotlin.material_design.myPictureOfTheDay.BuildConfig
import q3_kotlin.material_design.myPictureOfTheDay.model.EpicPhotosData
import q3_kotlin.material_design.myPictureOfTheDay.repository.EpicRepositoryRetrofitImpl
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.EpicAppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EpicViewModel(
    private val epicLiveDataForViewToObserve: MutableLiveData<EpicAppState> =
        MutableLiveData(),
    private val retrofitImpl: EpicRepositoryRetrofitImpl = EpicRepositoryRetrofitImpl()
): ViewModel() {

    fun getData(date: String): LiveData<EpicAppState> {
        sendServerRequest(date)
        return epicLiveDataForViewToObserve
    }

    private fun sendServerRequest(date: String) {
        epicLiveDataForViewToObserve.value = EpicAppState.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            EpicAppState.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(date).enqueue(object :
                Callback<List<EpicPhotosData>> {
                override fun onResponse(
                    call: Call<List<EpicPhotosData>>,
                    response: Response<List<EpicPhotosData>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        epicLiveDataForViewToObserve.value =
                            EpicAppState.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            epicLiveDataForViewToObserve.value =
                                EpicAppState.Error(Throwable("Unidentified error"))
                        } else {
                            epicLiveDataForViewToObserve.value =
                                EpicAppState.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<List<EpicPhotosData>>, t: Throwable) {
                    epicLiveDataForViewToObserve.value = EpicAppState.Error(t)
                }
            })
        }
    }
}