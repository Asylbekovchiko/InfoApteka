package kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.mainFarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import android.Manifest.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData

import androidx.paging.LoadState
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithPaging
import kg.sunrise.infoapteka.databinding.FragmentFarmAdressBinding
import kg.sunrise.infoapteka.networking.models.response.BranchAdressesResponse
import kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.adapter.FarmAdressAdapterPaging
import kg.sunrise.infoapteka.ui.shared.farmAdressDivider.DividerItemDecoration
import kg.sunrise.infoapteka.utils.extensions.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.util.jar.Manifest

class FarmAdressFragment :
    BaseFragmentWithPaging<BranchAdressesResponse, FragmentFarmAdressBinding, FarmAdressViewModel>() {
    private val farmAdressAdapter = FarmAdressAdapterPaging {
        navigate(FarmAdressFragmentDirections.actionGlobalDetailFarmFragment(it))
    }
    private val locationRequest = LocationRequest.create().apply {
        interval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
        }
    }

    private var fusedLocation: FusedLocationProviderClient? = null

    override val viewModel by sharedViewModel<FarmAdressViewModel>()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    override fun findTypeOfObject(data: Any?) {

    }


    override fun successRequest() {
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFarmAdressBinding {
        return FragmentFarmAdressBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun init() {
        initListeners()
        getUserLocation()
        getAdresses()
        setupAdressesAdapter()
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        if (hasLocationPermission(requireContext())) {
            fusedLocation?.requestLocationUpdates(
                locationRequest,
                locationCallBack,
                null
            )
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocation?.removeLocationUpdates(locationCallBack)
    }

    private fun initListeners() {
        binding.spRefresh.setOnRefreshListener {
            binding.spRefresh.isRefreshing = false
            getAdresses()
            viewModel.clearPaging()
        }
    }

    private fun setupAdressesAdapter() {
        val itemLineDivider = DividerItemDecoration(
            requireActivity().getDrawable(R.drawable.rv_divider_farm),
            true,
            true
        )
        binding.rvFarmItems.addItemDecoration(itemLineDivider)
        binding.rvFarmItems.adapter = farmAdressAdapter
        lifecycleScope.launch {
            farmAdressAdapter.loadStateFlow.apply {
                distinctUntilChangedBy { it.refresh }
                filter { it.refresh is LoadState.NotLoading }
                collect()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getAdresses() {
        requestJob = lifecycleScope.launch {
            val getLocation = fusedLocation?.lastLocation?.asDeferred()?.await()
            viewModel.getAdressesPaging(
                getLocation?.latitude,
                getLocation?.longitude
            ).collectLatest {
                farmAdressAdapter.submitData(it)
            }
        }
        viewModel.clearDataIfNeed()
    }
}





