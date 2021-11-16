package kg.sunrise.infoapteka.ui.main.home.aboutCompany

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentAboutCompanyBinding
import kg.sunrise.infoapteka.enums.AboutCompanyType
import kg.sunrise.infoapteka.networking.models.response.AboutCompanyInfo
import kg.sunrise.infoapteka.ui.main.home.aboutCompany.adapter.AboutAdapter
import kg.sunrise.infoapteka.ui.main.home.aboutCompany.adapter.AboutDTO
import kg.sunrise.infoapteka.ui.shared.InfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutCompanyFragment : BaseFragmentWithVM<FragmentAboutCompanyBinding, InfoViewModel>() {

    private val phoneAdapter = AboutAdapter()
    private val emailAdapter = AboutAdapter()
    private val socialAdapter = AboutAdapter()

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAboutCompanyBinding {
        return FragmentAboutCompanyBinding.inflate(inflater)
    }

    override fun init() {
        setupAdapter()
        setupUI()
    }

    private fun setupUI() {
        binding.inclToolbar.apply {
            ivBack.setOnClickListener { findNavController().navigateUp() }

            tvToolbar.setText(R.string.About_company)
        }
    }

    private fun setupAdapter() {
        binding.apply {
            rvEmails.adapter = emailAdapter
            rvPhones.adapter = phoneAdapter
            rvSocials.adapter = socialAdapter
        }
    }

    override val viewModel: InfoViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    override fun makeRequests() {
        viewModel.getCompanyInfo()
    }

    override fun findTypeOfObject(data: Any?) {
        when (data) {
            is AboutCompanyInfo -> {
                fillInfo(data)
            }
        }
    }

    private fun fillInfo(info: AboutCompanyInfo) {
        binding.apply {
            tvTitle.text = info.title
            tvDescription.text = info.description

            inclAddress.ivImage.setImageResource(R.drawable.ic_pin)
            inclAddress.tvContent.text = info.address

            initializeAdapters(info)
        }
    }

    private fun initializeAdapters(info: AboutCompanyInfo) {
        val phones = info.phones.map { AboutDTO.AboutDrawDTO(it, R.drawable.ic_phone, AboutCompanyType.PHONE) }
        val emails = arrayListOf<String>(info.email).map { AboutDTO.AboutDrawDTO(it, R.drawable.ic_mail, AboutCompanyType.EMAIL) }
        val socials = info.socials.map { AboutDTO.AboutUrlDTO("Facebook", it.logo, it.link) }

        phoneAdapter.setData(ArrayList(phones))
        emailAdapter.setData(ArrayList(emails))
        socialAdapter.setData(ArrayList(socials))
    }

    override fun successRequest() {
    }
}