package kg.sunrise.infoapteka.ui.main.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseBottomSheetDialogFragment
import kg.sunrise.infoapteka.databinding.FragmentOrderingResultBottomSheetBinding
import kg.sunrise.infoapteka.utils.enums.OrderingResult

class OrderingResultBottomSheetFragment(private val resultType: OrderingResult, private val resultListener: OrderingResultListener ): BaseBottomSheetDialogFragment<FragmentOrderingResultBottomSheetBinding>() {
    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderingResultBottomSheetBinding {
        return FragmentOrderingResultBottomSheetBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        setUpOrderingResult()
        initListeners()
    }

    private fun initListeners() {
        binding.btnOkClose.setOnClickListener {
            resultListener.orderingResultBtnClicked()
            dismiss()
        }
    }

    private fun setUpOrderingResult() {
        when(resultType){
            OrderingResult.SUCCESS -> successOrder()
            OrderingResult.FAILURE -> errorOrder()
        }
    }

    private fun errorOrder() {
        binding.ivResultIcon.setImageResource(R.drawable.ic_error_ordering)
        binding.tvResultText.text = getString(R.string.failure)
        binding.btnOkClose.text = getString(R.string.Close)
    }

    private fun successOrder() {
        binding.ivResultIcon.setImageResource(R.drawable.ic_success_ordering)
        binding.tvResultText.text = getString(R.string.success_ordered)
        binding.btnOkClose.text = getString(R.string.good)
    }

}