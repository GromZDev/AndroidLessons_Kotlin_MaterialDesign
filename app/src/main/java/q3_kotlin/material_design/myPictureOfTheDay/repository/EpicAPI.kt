package q3_kotlin.material_design.myPictureOfTheDay.repository

import q3_kotlin.material_design.myPictureOfTheDay.BuildConfig
import q3_kotlin.material_design.myPictureOfTheDay.model.EpicPhotosData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpicAPI {
    @GET("natural/date/{date}")
    fun getPictureOfTheDay(
        @Path("date") date: String,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): Call<List<EpicPhotosData>>
}