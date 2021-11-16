package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class CustomerUpdateResponse(

	@SerializedName("city")
	val city: Int,

	@SerializedName("birth_date")
	val birthDate: String,

	@SerializedName("last_name")
	val lastName: String,

	@SerializedName("middle_name")
	val middleName: String,

	@SerializedName("first_name")
	val firstName: String
)
