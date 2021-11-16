package kg.sunrise.infoapteka.ui.auth.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseBottomSheetDialogFragment
import kg.sunrise.infoapteka.databinding.FragmentSortBottomSheetBinding

class CitiesBottomSheetFragment(
    delegate: CityAdapterDelegate,
    private val cities: ArrayList<CityRadioBtnItem>
) : BaseBottomSheetDialogFragment<FragmentSortBottomSheetBinding>() {

    private val citiesAdapter = CitiesAdapter(delegate)

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSortBottomSheetBinding.inflate(inflater)

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        init()
        citiesAdapter.setData(cities)
        binding.rvItems.adapter = citiesAdapter
    }

    private fun init() {
        initViews()
    }

    private fun initViews() {
        binding.tvTitle.text = getString(R.string.choose_city)
    }
}