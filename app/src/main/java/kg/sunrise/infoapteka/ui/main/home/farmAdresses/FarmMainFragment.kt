package kg.sunrise.infoapteka.ui.main.home.farmAdresses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragment
import kg.sunrise.infoapteka.databinding.FragmentFarmMainBinding
import kg.sunrise.infoapteka.ui.main.home.farmAdresses.adapter.FarmAdressMainAdapter

class FarmMainFragment : BaseFragment<FragmentFarmMainBinding>() {

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFarmMainBinding {
        return FragmentFarmMainBinding.inflate(inflater)
    }

    override fun onCreateViewSetup(savedInstanceState: Bundle?) {
        super.onCreateViewSetup(savedInstanceState)
        initViewPager()
    }


    override fun init() {
        initToolbar()
    }

    private fun initToolbar() {
        binding.inclToolbar.tvToolbar.text = getString(R.string.farm_adresses)
        binding.inclToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun initViewPager() {
        val vpAdapter = FarmAdressMainAdapter(childFragmentManager, lifecycle)
        val tabs = binding.tlTabs
        binding.vpPager.isUserInputEnabled = false
        val pages = binding.vpPager
        pages.adapter = vpAdapter
        TabLayoutMediator(tabs, pages) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.list_type_adress)
                1 -> tab.text = getString(R.string.map_type_adress)
            }
        }.attach()
    }


}