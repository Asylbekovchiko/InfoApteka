package kg.sunrise.infoapteka.ui.auth.privacyPolicy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentPrivacePolicyBinding
import kg.sunrise.infoapteka.networking.models.response.PrivacyPolicyResponse
import kg.sunrise.infoapteka.ui.auth.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PrivacyPolicyFragment :
    BaseFragmentWithVM<FragmentPrivacePolicyBinding, AuthViewModel>() {

    override val viewModel: AuthViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPrivacePolicyBinding.inflate(inflater)

    override fun init() {
        initViews()
        initListeners()
    }

    private fun initViews() = with(binding) {
        binding.inclToolbar.tvToolbar.setText(R.string.privacy_policy)
    }

    private fun initListeners() = with(binding) {
        inclToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun makeRequests() {
        viewModel.getPrivacyPolicy()
    }

    override fun findTypeOfObject(data: Any?) {
        when (data) {
            is PrivacyPolicyResponse -> onPrivacyPolicyResponse(data)
        }
    }

    private fun onPrivacyPolicyResponse(
        data: PrivacyPolicyResponse
    ) {
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

    override fun successRequest() {
    }
}