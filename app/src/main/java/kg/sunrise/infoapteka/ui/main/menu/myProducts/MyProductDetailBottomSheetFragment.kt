package kg.sunrise.infoapteka.ui.main.menu.myProducts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseBottomSheetDialogFragment
import kg.sunrise.infoapteka.databinding.BottomSheetChangeProfilePhotoBinding
import kg.sunrise.infoapteka.databinding.BottomSheetFragmentMyProductDetailBinding

class MyProductDetailBottomSheetFragment()
    : BaseBottomSheetDialogFragment<BottomSheetFragmentMyProductDetailBinding>() {

    private var delegate: MyProductDetailBottomSheetFragmentDelegate? = null
    private var position: Int? = null

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): BottomSheetFragmentMyProductDetailBinding {
        return BottomSheetFragmentMyProductDetailBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.btnEdit.setOnClickListener {
            position?.let {
                delegate?.onEditMyProductClick(it)
            }
        }

        binding.btnRemove.setOnClickListener {
            position?.let {
                delegate?.onRemoveMyProductClick(it)
            }
        }
    }

    fun configure(with: MyProductDetailBottomSheetFragmentDelegate) {
        delegate = with
    }

    fun show(position: Int, manager: FragmentManager, tag: String?) {
        this.position = position
        super.show(manager, tag)
    }
}

interface MyProductDetailBottomSheetFragmentDelegate {

    fun onRemoveMyProductClick(position: Int)

    fun onEditMyProductClick(position: Int)
}