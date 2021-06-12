package q3_kotlin.material_design.myPictureOfTheDay.viewModel.mainViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import q3_kotlin.material_design.myPictureOfTheDay.BuildConfig
import q3_kotlin.material_design.myPictureOfTheDay.model.MarsRoverPhotosData
import q3_kotlin.material_design.myPictureOfTheDay.model.PictureServerResponseData
import q3_kotlin.material_design.myPictureOfTheDay.repository.MarsRoverRepositoryRetrofitImpl
import q3_kotlin.material_design.myPictureOfTheDay.repository.PODRepositoryRetrofitImpl
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.MarsRoverAppState
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.PODAppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsRoverViewModel(
    private val marsRoverLiveDataForViewToObserve: MutableLiveData<MarsRoverAppState> =
        MutableLiveData(),
    private val retrofitImpl: MarsRoverRepositoryRetrofitImpl = MarsRoverRepositoryRetrofitImpl()
) : ViewModel() {

    fun getData(date: String): LiveData<MarsRoverAppState> {
        sendServerRequest(date)
        return marsRoverLiveDataForViewToObserve
    }

    private fun sendServerRequest(date: String) {
        marsRoverLiveDataForViewToObserve.value = MarsRoverAppState.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            MarsRoverAppState.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey, date).enqueue(object :
                Callback<MarsRoverPhotosData> {
                override fun onResponse(
                    call: Call<MarsRoverPhotosData>,
                    response: Response<MarsRoverPhotosData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        marsRoverLiveDataForViewToObserve.value =
                            MarsRoverAppState.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            marsRoverLiveDataForViewToObserve.value =
                                MarsRoverAppState.Error(Throwable("Unidentified error"))
                        } else {
                            marsRoverLiveDataForViewToObserve.value =
                                MarsRoverAppState.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<MarsRoverPhotosData>, t: Throwable) {
                    marsRoverLiveDataForViewToObserve.value = MarsRoverAppState.Error(t)
                }
            })
        }
    }
}