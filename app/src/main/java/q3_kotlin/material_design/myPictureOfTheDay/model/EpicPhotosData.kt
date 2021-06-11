package q3_kotlin.material_design.myPictureOfTheDay.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class EpicPhotosData(
    @field:SerializedName("caption") val caption: String,
    @field:SerializedName("image") val image: String,
    @field:SerializedName("date") val date: String
)

data class EpicListData2(
    @field:SerializedName("caption") val caption: String?,
    @field:SerializedName("image") val image: String?,
    @field:SerializedName("date") val date: String?
)

data class EpicListData3(
    @field:SerializedName("caption") val caption: String?,
    @field:SerializedName("image") val image: String?,
    @field:SerializedName("date") val date: String?
)

