package kg.sunrise.infoapteka.ui.shared.helpFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentHelpBinding
import kg.sunrise.infoapteka.networking.models.response.HelpInfoResponse
import kg.sunrise.infoapteka.ui.shared.InfoViewModel
import kg.sunrise.infoapteka.ui.shared.helpFragment.adapter.HelpAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HelpFragment : BaseFragmentWithVM<FragmentHelpBinding, InfoViewModel>() {

    override val viewModel: InfoViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    private val helpAdapter = HelpAdapter()

    override fun makeRequests() {
        viewModel.getHelpInfo()
    }

    override fun findTypeOfObject(data: Any?) {
        val info = (data as? ArrayList<HelpInfoResponse>)
        info?.let {
            setUpHelpQuestions(it)
        }
    }

    override fun successRequest() {
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentHelpBinding {
        return FragmentHelpBinding.inflate(inflater)
    }

    override fun init() {
        initListeners()
        initUI()
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.rvHelp.adapter = helpAdapter
    }

    private fun initListeners() {
        binding.inclToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initUI() {
        binding.inclToolbar.tvToolbar.setText(getString(R.string.Help))
    }

    private fun setUpHelpQuestions(info: ArrayList<HelpInfoResponse>) {
        helpAdapter.setData(info)
    }
}