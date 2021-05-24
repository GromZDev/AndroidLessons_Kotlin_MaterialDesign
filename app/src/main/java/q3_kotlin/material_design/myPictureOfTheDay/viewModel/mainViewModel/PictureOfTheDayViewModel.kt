package q3_kotlin.material_design.myPictureOfTheDay.viewModel.mainViewModel

import q3_kotlin.material_design.myPictureOfTheDay.BuildConfig
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import q3_kotlin.material_design.myPictureOfTheDay.model.PictureServerResponseData
import q3_kotlin.material_design.myPictureOfTheDay.repository.PODRepositoryRetrofitImpl
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.PODAppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PODAppState> =
        MutableLiveData(),
    private val retrofitImpl: PODRepositoryRetrofitImpl = PODRepositoryRetrofitImpl()
) :
    ViewModel() {

    fun getData(): LiveData<PODAppState> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    private fun sendServerRequest() {
        liveDataForViewToObserve.value = PODAppState.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PODAppState.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey).enqueue(object :
                Callback<PictureServerResponseData> {
                override fun onResponse(
                    call: Call<PictureServerResponseData>,
                    response: Response<PictureServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value =
                            PODAppState.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                PODAppState.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                PODAppState.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PictureServerResponseData>, t: Throwable) {
                    liveDataForViewToObserve.value = PODAppState.Error(t)
                }
            })
        }
    }


}



