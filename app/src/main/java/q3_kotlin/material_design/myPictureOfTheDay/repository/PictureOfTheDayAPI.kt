package q3_kotlin.material_design.myPictureOfTheDay.repository

import q3_kotlin.material_design.myPictureOfTheDay.BuildConfig
import q3_kotlin.material_design.myPictureOfTheDay.model.PictureServerResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {
    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY,
    ): Call<PictureServerResponseData>
}