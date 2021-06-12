package q3_kotlin.material_design.myPictureOfTheDay.repository

import q3_kotlin.material_design.myPictureOfTheDay.BuildConfig
import q3_kotlin.material_design.myPictureOfTheDay.model.MarsRoverPhotosData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsRoverAPI {
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY,
        @Query("earth_date") date: String,
    ): Call<MarsRoverPhotosData>
}