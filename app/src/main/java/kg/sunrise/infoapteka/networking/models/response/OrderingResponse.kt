package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class OrderingResponse(

	@SerializedName("cart_id")
	val cartId: Int,

	@SerializedName("address")
	val address: String? = null,

	@SerializedName("phones")
	val phones: List<PhonesItem>,

	@SerializedName("last_name")
	val lastName: String? = null,

	@SerializedName("comment")
	val comment: String? = null,

	@SerializedName("id")
	val orderId: Int,

	@SerializedName("first_name")
	val firstName: String,

	@SerializedName("geolocation")
	val geolocation: String? = null,

	@SerializedName("pay_method")
	val paymentMethod: String
)

data class PhonesItem(

	@SerializedName("phone")
	val phone: String
)
