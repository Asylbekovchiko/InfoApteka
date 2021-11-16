package kg.sunrise.infoapteka.ui.main.product.editProduct

import kg.sunrise.infoapteka.networking.models.response.PhotoResponse
import kg.sunrise.infoapteka.networking.models.response.mapToPhotoUrl
import kg.sunrise.infoapteka.networking.repositories.DrugRepository
import kg.sunrise.infoapteka.ui.main.product.ProductViewModel
import kg.sunrise.infoapteka.utils.constants.DRUG_DELETED_SUCCESSFULLY
import kg.sunrise.infoapteka.utils.constants.IMAGES_UPLOADED_SUCCESSFULLY
import kg.sunrise.infoapteka.utils.extensions.toArrayListOrEmpty
import kg.sunrise.infoapteka.utils.extensions.updateSuccessObjectValue
import kg.sunrise.infoapteka.utils.extensions.updateValue
import kg.sunrise.infoapteka.utils.network.State

class EditProductViewModel(
    repo: DrugRepository
) : ProductViewModel(repo) {

    fun getDrug(drugId: Int) = getViewModelScope {
        if (super.drugId > -1) return@getViewModelScope
        super.drugId = drugId
        val response = repo.getDrug(super.drugId)

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            imagesLD.value = ArrayList(
                response.body()?.images?.map(
                    PhotoResponse::mapToPhotoUrl
                ).toArrayListOrEmpty()
            )
            _state updateSuccessObjectValue response.body()
        }
    }

    fun putDrug() = getViewModelScope {
        val response =
            repo.putDrug(drugId, generateProductRequest())

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state updateValue State.SuccessState.NoItemState
        }
    }

    override fun uploadImages() = getViewModelScope {
        if (!getImageUris().isEmpty()) {
            super.uploadImages()
            return@getViewModelScope
        }
        _state updateSuccessObjectValue IMAGES_UPLOADED_SUCCESSFULLY
    }

    fun deleteDrug() = getViewModelScope {
        val response = repo.deleteDrug(drugId)

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state updateSuccessObjectValue DRUG_DELETED_SUCCESSFULLY
        }
    }
}