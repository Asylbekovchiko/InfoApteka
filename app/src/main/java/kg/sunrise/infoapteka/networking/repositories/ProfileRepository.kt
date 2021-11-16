package kg.sunrise.infoapteka.networking.repositories

import kg.sunrise.infoapteka.base.repository.BaseRepository
import kg.sunrise.infoapteka.networking.api.ProfileApi
import kg.sunrise.infoapteka.networking.models.request.CustomerRequest
import kg.sunrise.infoapteka.networking.models.response.SellerProfileUpdate
import okhttp3.MultipartBody
import retrofit2.http.Part

class ProfileRepository(private val profileApi: ProfileApi) : BaseRepository() {

    suspend fun getAvatar() =
        makeRequest {
            profileApi.getAvatar()

        }

    suspend fun setAvatar(@Part avatar: MultipartBody.Part) =
        makeRequest {
            profileApi.setAvatar(avatar)
        }

    suspend fun setCertificates(@Part image: ArrayList<MultipartBody.Part>) = makeRequest {
        profileApi.setCertificates(image)
    }

    suspend fun deleteAvatar() = makeRequest {
        profileApi.deleteAvatar()
    }



    suspend fun getCities() = makeRequest {
        profileApi.getCities()
    }

    suspend fun setUpdateProfileSeller(profileUpdate: SellerProfileUpdate) = makeRequest {
        profileApi.setUpdateProfileSeller(profileUpdate)
    }

    suspend fun getSellerProfile() = makeRequest {
        profileApi.getSeller()
    }

    suspend fun getCustomer() = makeRequest {
        profileApi.getCustomer()
    }

    suspend fun updateCustomer(data: CustomerRequest) = makeRequest {
        profileApi.updateCustomer(data)
    }

}