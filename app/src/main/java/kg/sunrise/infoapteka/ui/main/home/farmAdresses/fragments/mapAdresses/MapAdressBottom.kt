package kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.mapAdresses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseBottomSheetDialogFragment
import kg.sunrise.infoapteka.databinding.BottomAdressBinding
import kg.sunrise.infoapteka.networking.models.response.BranchAdressesResponse
import kg.sunrise.infoapteka.utils.extensions.setImageWithPlaceHolder
import kg.sunrise.infoapteka.utils.extensions.setTextColorRes

class MapAdressBottom(private val data : BranchAdressesResponse) : BaseBottomSheetDialogFragment<BottomAdressBinding>() {

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): BottomAdressBinding {
        return BottomAdressBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        setUpUi()
        initListeners()
    }

    private fun initListeners() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun setUpUi()  = with(binding){
        setImageWithPlaceHolder(
            data.image,
            shivFarm,
            placeHolder = R.drawable.ic_item_placeholder
        )
        if (data.isOpen){
            tvStatus.setTextColorRes(R.color.green_secondary)
            tvStatus.text = getString(R.string.farm_open)
        }
        else
            tvStatus.text = getString(R.string.farm_close)
        tvDistance.text = getString(R.string.distancePattern , data.distance)
        tvNameFarm.text = data.name
        tvAddress.text = data.address

    }
}