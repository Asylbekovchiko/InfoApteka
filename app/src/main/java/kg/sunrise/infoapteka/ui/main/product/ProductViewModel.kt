package kg.sunrise.infoapteka.ui.main.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kg.sunrise.infoapteka.base.viewModel.BaseViewModel
import kg.sunrise.infoapteka.networking.models.request.ProductRequest
import kg.sunrise.infoapteka.networking.repositories.DrugRepository
import kg.sunrise.infoapteka.utils.constants.IMAGES_UPLOADED_FAILED
import kg.sunrise.infoapteka.utils.constants.IMAGES_UPLOADED_SUCCESSFULLY
import kg.sunrise.infoapteka.utils.constants.MAX_PRODUCT_PHOTO_NUMBER
import kg.sunrise.infoapteka.utils.constants.MIN_PRODUCT_PHOTO_NUMBER
import kg.sunrise.infoapteka.utils.extensions.toArrayListOrEmpty
import kg.sunrise.infoapteka.utils.extensions.updateSuccessObjectValue
import kg.sunrise.infoapteka.utils.extensions.updateValue
import kg.sunrise.infoapteka.utils.network.State
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

open class ProductViewModel(
    protected val repo: DrugRepository
) : BaseViewModel() {

    val categoryLD = MutableLiveData<Category?>()
    val productNameLD = MutableLiveData<String>()
    val productDescriptionLD = MutableLiveData<String>()
    val productPriceLD = MutableLiveData<Int>()
    val imagesLD = MutableLiveData<ArrayList<Photo>>()
    var drugId: Int = -1
        protected set

    val isAllRequirementsFilledLiveData: LiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            val validator = getValidator(this::postValue)
            addSource(
                categoryLD, validator as Observer<in Category?>
            )
            addSource(
                productNameLD, validator as Observer<in String>
            )
            addSource(
                productDescriptionLD, validator as Observer<in String>
            )
            addSource(
                productPriceLD, validator as Observer<in Int>
            )
            addSource(
                imagesLD, validator as Observer<in ArrayList<Photo>>
            )
        }

    protected open fun getValidator(consumer: (Boolean) -> Unit) =
        ProductValidator(consumer)

    open inner class ProductValidator(
        protected val consumer: (Boolean) -> Unit
    ) : Observer<Any> {

        override fun onChanged(t: Any?) {
            consumer(isAllFieldsFilled())
        }

        protected fun isAllFieldsFilled(): Boolean {
            val imageCount = imagesLD.value?.size ?: 0
            return categoryLD.value != null
                    && productNameLD.value?.isNotBlank() ?: false
                    && productDescriptionLD.value?.isNotBlank() ?: false
                    && (productPriceLD.value ?: -1) > 0
                    && imageCount >= MIN_PRODUCT_PHOTO_NUMBER
                    && imageCount <= MAX_PRODUCT_PHOTO_NUMBER
        }

    }

    fun addImage(image: Photo) {
        val currentImages = imagesLD.value ?: ArrayList()
        currentImages.add(image)
        imagesLD.value = currentImages
    }

    fun removeImage(image: Photo) {
        val currentImages = imagesLD.value ?: ArrayList()
        currentImages.remove(image)
        imagesLD.value = currentImages
    }

    fun postProduct() = getViewModelScope {
        val response = repo.postProduct(generateProductRequest())

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            drugId = response.body()?.id ?: drugId
            _state updateValue State.SuccessState.NoItemState
        }
    }

    protected fun generateProductRequest() = ProductRequest(
        categoryLD.value?.id,
        productNameLD.value,
        productDescriptionLD.value,
        productPriceLD.value,
        getPhotoUrlIds()
    )

    protected fun getPhotoUrlIds() = ArrayList<Int>().apply {
        imagesLD.value?.forEach {
            if (it is PhotoUrl) add(it.id)
        }
    }

    open fun uploadImages() = getViewModelScope {
        val response = repo.uploadImages(
            drugId, generateImageMultipartBodies()
        )

        if (!response.hasBody()) return@getViewModelScope

        _state updateSuccessObjectValue
                if (response!!.isSuccessful) {
                    IMAGES_UPLOADED_SUCCESSFULLY
                } else {
                    IMAGES_UPLOADED_FAILED
                }
    }

    protected fun generateImageMultipartBodies() =
        getImageUris().map {
            val file = File(it.path ?: "")
            val requestBody = file.asRequestBody(
                "multipart/form-data".toMediaTypeOrNull()
            )
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestBody
            )
        }.toArrayListOrEmpty()


    protected fun getImageUris() = (imagesLD.value ?: ArrayList())
        .filter { it is PhotoUri }
        .map { (it as PhotoUri).imageUri }
}