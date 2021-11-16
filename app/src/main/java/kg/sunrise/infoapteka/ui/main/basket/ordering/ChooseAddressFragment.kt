package kg.sunrise.infoapteka.ui.main.basket.ordering

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentChooseAddressMapBinding
import kg.sunrise.infoapteka.utils.extensions.setEnabled
import kg.sunrise.infoapteka.utils.extensions.bitmapFromVector
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.util.*


class ChooseAddressFragment: BaseFragmentWithVM<FragmentChooseAddressMapBinding, OrderingViewModel>(){
    override val viewModel: OrderingViewModel by sharedViewModel()
    override val progressBar: ConstraintLayout by lazy{binding.inclProgress.clProgress}
    private var latLngText: String? = null

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChooseAddressMapBinding {
        return FragmentChooseAddressMapBinding.inflate(inflater)
    }


    override fun init() {
        initToolbar()
        initMapFragment()
        initListeners()
    }

    override fun makeRequests() {
    }

    override fun successRequest() {
    }

    override fun findTypeOfObject(data: Any?) {
    }

    private fun initToolbar() {
        binding.inclToolbar.tvToolbar.text = getString(R.string.address_in_the_map)
    }

    private fun initListeners() = with(binding) {
        inclToolbar.ivBack.setOnClickListener { findNavController().navigateUp() }

        btnChoose.setOnClickListener {
            viewModel.map.value = latLngText
            findNavController().navigateUp()
        }
    }

    private fun initMapFragment() {
        viewModel.map.value = null
        val supportMapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        supportMapFragment.getMapAsync { googleMap ->
            val bishkekLatLng = LatLng(42.882004, 74.582748)
            val zoom = 12f
            val formatLatLng = "%.14f,%.14f"
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bishkekLatLng, zoom))

            googleMap.setOnMapClickListener { latLng ->
                latLngText = String.format(Locale.US, formatLatLng, latLng.latitude, latLng.longitude)
                googleMap.clear()
                googleMap.addMarker(MarkerOptions()
                    .position(latLng)
                    .icon(bitmapFromVector(requireContext(), R.drawable.ic_map_marker))
                )
                binding.btnChoose.setEnabled(R.color.green)
            }
        }
    }
}