package kg.sunrise.infoapteka.networking.models.response


import com.google.gson.annotations.SerializedName

data class AppVersion(
        @SerializedName("android_force_update")
        val androidForceUpdate: Boolean,
        @SerializedName("android_version")
        val androidVersion: String
)