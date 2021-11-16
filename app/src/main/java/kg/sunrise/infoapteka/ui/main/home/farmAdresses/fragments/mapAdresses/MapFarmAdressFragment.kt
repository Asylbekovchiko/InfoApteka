package kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.mapAdresses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragment
import kg.sunrise.infoapteka.databinding.FragmentMapAdressesBinding
import kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.mainFarm.FarmAdressViewModel
import kg.sunrise.infoapteka.utils.constants.BishkekLangtitude
import kg.sunrise.infoapteka.utils.extensions.bitmapFromVector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MapFarmAdressFragment :
    BaseFragment<FragmentMapAdressesBinding>(),
    GoogleMap.OnMarkerClickListener, OnMapReadyCallback {
    private val shareViewModel by sharedViewModel<FarmAdressViewModel>()

    private var googleMap: GoogleMap? = null

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMapAdressesBinding {
        return FragmentMapAdressesBinding.inflate(inflater)
    }

    override fun init() {
        observePositions()
    }


    private fun addMarker() {
        shareViewModel.branchData.forEach {
            val latLng = LatLng(it.latitude, it.longitude)
            val marker = googleMap?.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .icon(bitmapFromVector(requireContext(), R.drawable.ic_marker_green))
            )
            marker?.tag = it.id
        }
    }

    private fun observePositions() {
        shareViewModel.getBranchLive.observe(this) {
            addMarker()
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        shareViewModel.branchData.forEach {
            if (marker.tag == it.id) {
                MapAdressBottom(it).show(parentFragmentManager, null)
                return true
            }
        }
        return false
    }

    override fun onCreateViewSetup(savedInstanceState: Bundle?) {
        super.onCreateViewSetup(savedInstanceState)

        binding.mvBranches.onCreate(savedInstanceState)
        binding.mvBranches.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val zoom = 12f
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(BishkekLangtitude, zoom))
        googleMap?.setOnMarkerClickListener(this)
        addMarker()
    }

    override fun onResume() {
        super.onResume()
        binding.mvBranches.onResume()
    }

    override fun onDestroyView() {
        shareViewModel.branchData.clear()
        binding.mvBranches.onDestroy()
        super.onDestroyView()
    }
}
