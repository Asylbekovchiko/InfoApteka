package kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.detailFarm


import android.Manifest
import android.annotation.SuppressLint
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentFarmDetailBinding
import kg.sunrise.infoapteka.enums.ImageRatioType
import kg.sunrise.infoapteka.networking.models.response.BranchResponseDetail
import kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.detailFarm.adapter.OnPhoneClickInterface
import kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.detailFarm.adapter.PhoneFarmAdapter
import kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.zoomAlert.ZoomAlertDialog
import kg.sunrise.infoapteka.ui.shared.imageAdapter.ImageAdapter
import kg.sunrise.infoapteka.ui.shared.imageAdapter.ImageAdapterDelegate
import kg.sunrise.infoapteka.utils.constants.IMAGE_SCROLLING_DURATION
import kg.sunrise.infoapteka.utils.constants.preloadImagesCount
import kg.sunrise.infoapteka.utils.extensions.bitmapFromVector
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.setOverScrollModeNever
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.activity.result.ActivityResultCallback
import kg.sunrise.infoapteka.utils.extensions.visible


class DetailFarmFragment : BaseFragmentWithVM<FragmentFarmDetailBinding, FarmDetailViewModel>(),
    ImageAdapterDelegate, OnPhoneClickInterface {

    private val mPermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission(), PermissionCallback()
    )
    private val phoneFarmAdapter = PhoneFarmAdapter(this)

    override val viewModel by viewModel<FarmDetailViewModel>()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }
    private var positionOfPhoneNumber = 0

    override var isToShowProgress: Boolean = false
    private val imageAdapter = ImageAdapter(this, mainPage = ImageRatioType.FARM_PAGE)
    private val args by navArgs<DetailFarmFragmentArgs>()

    override fun makeRequests() {
        viewModel.getDetailInfo(args.id)
    }

    override fun findTypeOfObject(data: Any?) {
        when (data) {
            is BranchResponseDetail -> setupUI(data)
        }
    }

    private fun setupUI(data: BranchResponseDetail) = with(binding) {
        swipeRefresh.isRefreshing = false
        tvDistance.text = data.distance ?: ""
        tvTitle.text = data.name
        if (data.isOpen)
            tvOpen.text = getString(R.string.farm_now_open)
        else
            tvOpen.text = getString(R.string.farm_nnow_close)
        tvAdress.text = data.address
        tvShecdle.text = data.isWorking
        setupDataForAdapters(data)
        initMap(data)
    }

    private fun setupDataForAdapters(data: BranchResponseDetail) {
        data.images?.let { imageAdapter.setData(it) }
        binding.rvImages.currentItem = 0
        if (data.phones.isEmpty())
            return

        phoneFarmAdapter.setData(data.phones)
        binding.tvPhoneTitle.visible()

    }

    @SuppressLint("WrongConstant")
    private fun initAdapters() = with(binding) {
        rvPhoneNumber.adapter = phoneFarmAdapter
        rvImages.apply {
            adapter = imageAdapter
            offscreenPageLimit = preloadImagesCount
            setOverScrollModeNever()
            setPageTransformer(
                MarginPageTransformer(
                    resources.getDimensionPixelOffset(
                        R.dimen.dp_2_5x
                    )
                )
            )
        }
        dotsIndicator.setViewPager2(binding.rvImages)
    }

    override fun successRequest() {

    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFarmDetailBinding {
        return FragmentFarmDetailBinding.inflate(inflater)
    }

    override fun init() {
        initToolbar()
        initAdapters()
        initListeners()
    }

    private fun initMap(data: BranchResponseDetail) {
        binding.cvContainerMap.apply {
            isClickable = false
            isEnabled = false
        }
        val supportMapFragment: SupportMapFragment? =
                childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment?
        supportMapFragment?.view?.isClickable = false
        supportMapFragment?.getMapAsync { googleMap ->
            val latLng = LatLng(data.latitude, data.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .icon(bitmapFromVector(requireContext(), R.drawable.ic_marker_green))
            )
        }
    }

    private fun initListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getDetailInfo(args.id)
        }
    }

    private fun initToolbar() {
        binding.inclToolbar.apply {
            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
            tvToolbar.gone()
        }
    }

    override fun onImageClick(position: Int) {
        ZoomAlertDialog(imageAdapter.getItem(position)).show(parentFragmentManager, null)
    }

    override fun phoneCardClickListener(position: Int) {
        positionOfPhoneNumber = position
        mPermissionResult.launch(Manifest.permission.CALL_PHONE)
    }

    inner class PermissionCallback : ActivityResultCallback<Boolean> {
        override fun onActivityResult(result: Boolean?) {
            if (result == true) {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                callIntent.data =
                    Uri.parse(getString(R.string.phone_number_pattern) + phoneFarmAdapter.getAllItems()[positionOfPhoneNumber])
                requireActivity().startActivity(callIntent)
            } else {
                val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
                    data =
                        Uri.parse("tel:${phoneFarmAdapter.getAllItems()[positionOfPhoneNumber]}")
                }
                ContextCompat.startActivity(requireContext(), phoneIntent, null)
            }
        }

    }
}

