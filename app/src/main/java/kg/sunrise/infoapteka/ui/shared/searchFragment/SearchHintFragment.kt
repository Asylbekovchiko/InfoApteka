package kg.sunrise.infoapteka.ui.shared.searchFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentSearchHintBinding
import kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.SearchResultViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchHintFragment : BaseFragmentWithVM<FragmentSearchHintBinding, SearchResultViewModel>(),
    HintAdapterDelegate {

    override val viewModel: SearchResultViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    override var isToShowProgress: Boolean = false

    private val hintAdapter = HintAdapter(this)

    private var job: Job? = null

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchHintBinding {
        return FragmentSearchHintBinding.inflate(inflater)
    }

    override fun init() {
        setupUI()
        initListeners()
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.rvHints.adapter = hintAdapter

        val divider = ContextCompat.getDrawable(requireContext(), R.drawable.shape_light_divider)
        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        divider?.let {
            dividerItemDecoration.setDrawable(it)
            binding.rvHints.addItemDecoration(dividerItemDecoration)
        }
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.searchView.addTextChangedListener {
            it?.let {
                requestHints(it.toString())
            }
        }
    }

    private fun requestHints(searchText: String) {
        job?.cancel()
        job = lifecycleScope.launch {
            delay(500)
            viewModel.getSearchHints(searchText)
        }
    }

    private fun setupUI() {
        binding.searchView.requestFocus()
    }

    override fun makeRequests() {
        requestHints("")
    }

    override fun findTypeOfObject(data: Any?) {
        if ((data as? ArrayList<String>) != null) {
            fillAdapter(data)
        }
    }

    private fun fillAdapter(hints: ArrayList<String>) {
        hintAdapter.setData(hints)
    }

    override fun successRequest() {
    }

    private fun navigateToResult(productName: String) {
        val action = SearchHintFragmentDirections.actionSearchHintFragmentToSearchFragment(search = productName)
        findNavController().navigate(action)
    }

    // delegate implementation
    override fun onHintClicked(productName: String) {
        navigateToResult(productName)
    }
}

