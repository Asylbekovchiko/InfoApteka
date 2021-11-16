package kg.sunrise.infoapteka.ui.main.product

import android.net.Uri

sealed class Photo(
    open val name: String,
    open val size: String
)

data class PhotoUri(
    val imageUri: Uri,
    override val name: String,
    override val size: String
) : Photo(name, size)

data class PhotoUrl(
    val id: Int,
    val imageUrl: String,
    override val name: String = "",
    override val size: String = ""
) : Photo(name, size)