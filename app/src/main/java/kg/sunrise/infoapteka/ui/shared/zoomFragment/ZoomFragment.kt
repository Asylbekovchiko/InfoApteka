package kg.sunrise.infoapteka.ui.shared.zoomFragment

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kg.sunrise.infoapteka.base.fragment.BaseFragment
import kg.sunrise.infoapteka.databinding.FragmentZoomBinding

class ZoomFragment() : BaseFragment<FragmentZoomBinding>() {

    val args by navArgs<ZoomFragmentArgs>()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentZoomBinding {
        return FragmentZoomBinding.inflate(inflater)
    }

    override fun init() {
        setupImage()
        initListeners()
    }

    private fun setupImage() {
        Glide.with(requireContext()).load(args.imageURL).into(binding.ivImage)
    }

    private fun initListeners() {
        binding.inclToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}