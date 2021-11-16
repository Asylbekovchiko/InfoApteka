package kg.sunrise.infoapteka.ui.auth.city

import android.view.ViewGroup
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter

class CitiesAdapter(
    private val delegate: CityAdapterDelegate
) : BaseAdapter<CityRadioBtnItem, CitiesVH>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ) = CitiesVH(parent, R.layout.rv_item_single_choise, delegate)
}