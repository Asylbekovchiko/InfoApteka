package kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.sortBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseBottomSheetDialogFragment
import kg.sunrise.infoapteka.databinding.FragmentSortBottomSheetBinding
import kg.sunrise.infoapteka.ui.main.menu.profile.customerProfile.CustomerProfileFragment
import kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.RadioBtnItem
import kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.sortBottomSheet.adapter.SortAdapter
import kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.sortBottomSheet.adapter.SortAdapterDelegate

class SortBottomSheetFragment(
    val delegate: SortAdapterDelegate,
    val sortItems: ArrayList<RadioBtnItem>
)
    : BaseBottomSheetDialogFragment<FragmentSortBottomSheetBinding>() {

    val sortAdapter = SortAdapter(delegate)

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSortBottomSheetBinding {
        return FragmentSortBottomSheetBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sortAdapter.setData(sortItems)
        binding.rvItems.adapter = sortAdapter
    }
}