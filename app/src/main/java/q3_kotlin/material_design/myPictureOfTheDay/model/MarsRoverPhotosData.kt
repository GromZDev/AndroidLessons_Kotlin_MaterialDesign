package q3_kotlin.material_design.myPictureOfTheDay.model

import com.google.gson.annotations.SerializedName

data class MarsRoverPhotosData (
    @field:SerializedName("photos") val photos: List<MarsRoverPhotosDescription?>
        )

data class MarsRoverPhotosDescription (
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("img_src") val img_src: String?,
    @field:SerializedName("earth_date") val earth_date: String?,
    @field:SerializedName("rover") val rover: Rover?

)

data class Rover (
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("status") val status: String?
)
