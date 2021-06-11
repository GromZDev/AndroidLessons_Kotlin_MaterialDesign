package q3_kotlin.material_design.myPictureOfTheDay.repository

import q3_kotlin.material_design.myPictureOfTheDay.model.NasaImageGalleryData
import retrofit2.Call
import retrofit2.http.GET

interface NasaGalleryAPI {
    @GET("asset/as11-40-5874")
    fun getPictureOfTheDay(
    ): Call<NasaImageGalleryData>
}