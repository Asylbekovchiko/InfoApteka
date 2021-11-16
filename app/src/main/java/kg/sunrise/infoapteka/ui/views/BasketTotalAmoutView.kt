package kg.sunrise.infoapteka.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.visible

class BasketTotalAmoutView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet,
        defStyleAttr: Int = 0
) : MaterialCardView(context, attributeSet, defStyleAttr) {
    protected lateinit var tvCountItems: MaterialTextView
    protected lateinit var tvTotalSum: MaterialTextView
    private lateinit var tvDeliveryCondition: MaterialTextView
    private lateinit var cvRoot : MaterialCardView

    init {
        inflateView()
    }

    private fun inflateView() {
        LayoutInflater.from(context).inflate(R.layout.basket_total_count_view, this, true)
        tvCountItems = findViewById(R.id.tv_item_basket_count)
        tvTotalSum = findViewById(R.id.tv_total_amount)
        tvDeliveryCondition = findViewById(R.id.tvDeliveryCondition)
        cvRoot = findViewById(R.id.cv_root)
    }

    fun setContent(sum: String?, count: Int?) {
        tvCountItems.text = count.toString() ?: ""
        tvTotalSum.text = tvTotalSum.context.getString(R.string.Items_total_value, sum)
    }

    fun setOnDeleiveryClick(click: () -> Unit) {
        tvDeliveryCondition.setOnClickListener {
            click()
        }
    }

    fun setVisibility(isVisible: Boolean = true) {
        if (isVisible)
          cvRoot.visible()
        else
            cvRoot.gone()

    }

}