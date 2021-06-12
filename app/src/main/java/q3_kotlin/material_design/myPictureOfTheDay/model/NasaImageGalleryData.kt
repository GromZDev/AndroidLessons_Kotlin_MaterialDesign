package q3_kotlin.material_design.myPictureOfTheDay.model

import com.google.gson.annotations.SerializedName

data class NasaImageGalleryData(
    @field:SerializedName("collection") val rrr: NasaPhotosDescriptionData?

)

data class NasaPhotosDescriptionData(
    @field:SerializedName("items") val items: List<Items?>,
    @field:SerializedName("href") val href: String?
)

data class Items(
    @field:SerializedName("href") val href: String?
)