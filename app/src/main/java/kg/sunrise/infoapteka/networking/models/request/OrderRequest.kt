package kg.sunrise.infoapteka.networking.models.request

import com.google.gson.annotations.SerializedName

data class OrderRequest(

	@SerializedName("cart_id")
	val cartId: Int,

	@SerializedName("address")
	val address: String,

	@SerializedName("phones")
	val phones: List<PhonesItem>,

	@SerializedName("last_name")
	val lastName: String?,

	@SerializedName("comment")
	val comment: String?,

	@SerializedName("first_name")
	val firstName: String,

	@SerializedName("geolocation")
	val geolocation: String,
	@SerializedName("pay_method")
	val paymentMethod: String,
	@SerializedName("order_id")
	val orderNumber: String?
)

data class PhonesItem(
	@SerializedName("phone")
	var phone: String = "",

	var isValid: Boolean = false
)
