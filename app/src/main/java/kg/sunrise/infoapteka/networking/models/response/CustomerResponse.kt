package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class CustomerResponse(

	@SerializedName("role")
	val role: String,

	@SerializedName("phone")
	val phone: String,

	@SerializedName("city")
	val city: CityItem,

	@SerializedName("birth_date")
	val birthDate: String,

	@SerializedName("last_name")
	val lastName: String,

	@SerializedName("avatar")
	val avatar: String? = null,

	@SerializedName("middle_name")
	val middleName: String? = null,

	@SerializedName("first_name")
	val firstName: String
)

data class CityItem(
	@SerializedName("id")
	val id: Int,
	@SerializedName("title")
	val title: String?= ""
)
