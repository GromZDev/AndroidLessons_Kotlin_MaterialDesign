package q3_kotlin.material_design.myPictureOfTheDay.viewModel.mainViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import q3_kotlin.material_design.myPictureOfTheDay.model.NasaImageGalleryData
import q3_kotlin.material_design.myPictureOfTheDay.repository.NasaGalleryRepositoryRetrofitImpl
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.NasaGalleryAppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NasaGalleryViewModel(
    private val nasaGalleryLiveDataForViewToObserve: MutableLiveData<NasaGalleryAppState> =
        MutableLiveData(),
    private val retrofitImpl: NasaGalleryRepositoryRetrofitImpl = NasaGalleryRepositoryRetrofitImpl()
): ViewModel() {

    fun getData(): LiveData<NasaGalleryAppState> {
        sendServerRequest()
        return nasaGalleryLiveDataForViewToObserve
    }

    private fun sendServerRequest() {
        nasaGalleryLiveDataForViewToObserve.value = NasaGalleryAppState.Loading(null)

            retrofitImpl.getRetrofitImpl().getPictureOfTheDay().enqueue(object :
                Callback<NasaImageGalleryData> {
                override fun onResponse(
                    call: Call<NasaImageGalleryData>,
                    response: Response<NasaImageGalleryData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        nasaGalleryLiveDataForViewToObserve.value =
                            NasaGalleryAppState.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            nasaGalleryLiveDataForViewToObserve.value =
                                NasaGalleryAppState.Error(Throwable("Unidentified error"))
                        } else {
                            nasaGalleryLiveDataForViewToObserve.value =
                                NasaGalleryAppState.Error(Throwable(message))
                        }
                    }
                }


                override fun onFailure(call: Call<NasaImageGalleryData>, t: Throwable) {
                    nasaGalleryLiveDataForViewToObserve.value = NasaGalleryAppState.Error(t)
                }
            })
        }
    }

