package kg.sunrise.infoapteka.ui.main.basket.deliveryCondition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentDeliveryConditionBinding
import kg.sunrise.infoapteka.networking.models.response.InfoResponse
import kg.sunrise.infoapteka.ui.shared.InfoViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class DeliveryConditionFragment : BaseFragmentWithVM<FragmentDeliveryConditionBinding, InfoViewModel>() {

    override val viewModel: InfoViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    override fun init() {
        binding.inclToolbar.tvToolbar.setText(R.string.Delivery_condition)
        binding.inclToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun makeRequests() {
        viewModel.getDeliveryCondition()
    }

    override fun findTypeOfObject(data: Any?) {
        if (data is InfoResponse) {
            lifecycleScope.launch {
                data.pdfFile?.let {
                    val file = viewModel.getFileFromPdfURL(it)

                    try {
                        binding.pdfContent.fromFile(file).show()
                    } catch (e: Throwable) {
                        Timber.e(e.localizedMessage)
                    }
                }
            }
            data.pdfFile
        }
    }

    override fun successRequest() {
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDeliveryConditionBinding {
        return FragmentDeliveryConditionBinding.inflate(inflater)
    }
}