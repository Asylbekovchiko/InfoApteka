package kg.sunrise.infoapteka.ui.auth.city

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.google.android.material.radiobutton.MaterialRadioButton
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseVH

class CitiesVH(
    parent: ViewGroup,
    @LayoutRes layoutResId: Int,
    private val delegate: CityAdapterDelegate
) : BaseVH<CityRadioBtnItem>(parent, layoutResId) {

    private val tvTitle: TextView =
        itemView.findViewById(R.id.tv_title)
    private val checkButton: MaterialRadioButton =
        itemView.findViewById(R.id.radio_button)

    override fun bind(item: CityRadioBtnItem) = with(item) {
        tvTitle.text = item.title
        checkButton.isClickable = false
        checkButton.isChecked = item.isChecked

        itemView.setOnClickListener {
            checkButton.isChecked = true
            delegate.onCityItemClicked(this, absoluteAdapterPosition)
        }
    }

}