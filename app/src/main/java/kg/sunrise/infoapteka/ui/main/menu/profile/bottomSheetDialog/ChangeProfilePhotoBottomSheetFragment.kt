package kg.sunrise.infoapteka.ui.main.menu.profile.bottomSheetDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.sunrise.infoapteka.base.fragment.BaseBottomSheetDialogFragment
import kg.sunrise.infoapteka.databinding.BottomSheetChangeProfilePhotoBinding

class ChangeProfilePhotoBottomSheetFragment(
    private val updateAvatarImage: UpdateAvatarImage,
    private val deleteImageClick: () -> Unit
) : BaseBottomSheetDialogFragment<BottomSheetChangeProfilePhotoBinding>() {

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): BottomSheetChangeProfilePhotoBinding {
        return BottomSheetChangeProfilePhotoBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.btnLoadImage.setOnClickListener {
            callImagePicker()
            dismiss()
        }

        binding.btnDeleteImage.setOnClickListener {
            deleteImageClick()
            dismiss()
        }
    }

    private fun callImagePicker() {
        updateAvatarImage.launchImagePicker()
    }
}


fun interface UpdateAvatarImage {
    fun launchImagePicker()
}