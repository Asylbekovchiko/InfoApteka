package kg.sunrise.infoapteka.ui.main.home.farmAdresses.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.mainFarm.FarmAdressFragment
import kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.mapAdresses.MapFarmAdressFragment

class FarmAdressMainAdapter(mainFragmentManager : FragmentManager, lifecycle: Lifecycle) :FragmentStateAdapter(mainFragmentManager , lifecycle) {
    private val pagesCount = 2
    override fun getItemCount(): Int = pagesCount

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FarmAdressFragment()
            1 -> MapFarmAdressFragment()
            else -> throw IllegalStateException()
        }
    }
}