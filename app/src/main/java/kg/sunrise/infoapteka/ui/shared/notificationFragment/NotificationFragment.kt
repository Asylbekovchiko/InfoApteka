package kg.sunrise.infoapteka.ui.shared.notificationFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithPaging
import kg.sunrise.infoapteka.databinding.FragmentNotificationBinding
import kg.sunrise.infoapteka.ui.shared.notificationFragment.paging.NotificationAdapter
import kg.sunrise.infoapteka.ui.shared.notificationFragment.paging.NotificationAdapterDelegate
import kg.sunrise.infoapteka.ui.shared.notificationFragment.paging.NotificationResponse
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.visible
import kg.sunrise.infoapteka.utils.notifications.NotificationType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment
    : BaseFragmentWithPaging<NotificationResponse, FragmentNotificationBinding, NotificationViewModel>(),
    NotificationAdapterDelegate {

    override val viewModel: NotificationViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    private val notificationAdapter = NotificationAdapter(this)

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationBinding {
        return FragmentNotificationBinding.inflate(inflater)
    }

    override fun init() {
        setupUI()
        setupAdapter()
        initListeners()
    }

    private fun setupUI() {
        binding.inclToolbar.tvToolbar.setText(R.string.Notifications)
    }

    private fun initListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            swipeRefresh()
        }

        binding.inclToolbar.ivBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.isRefreshing = false

        viewModel.clearPaging()

        makeRequests()
    }

    private fun setupAdapter() {
        binding.rvNotifications.adapter = notificationAdapter
        lifecycleScope.launch {
            notificationAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect()
        }

        notificationAdapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.refresh is LoadState.NotLoading && notificationAdapter.itemCount == 0

            if (isListEmpty) {
                binding.clNotificationEmpty.visible()
                binding.rvNotifications.gone()
            } else {
                binding.clNotificationEmpty.gone()
                binding.rvNotifications.visible()
            }
        }
    }

    override fun findTypeOfObject(data: Any?) {

    }

    override fun successRequest() {

    }

    override fun onNotificationClick(type: String) {
        when (type.uppercase()) {
            NotificationType.ACCOUNT_APPROVED.name, NotificationType.ACCOUNT_REJECTED.name -> {
                findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToSellerProfileFragment())
            }
            NotificationType.DRUG_APPROVED.name, NotificationType.DRUG_REJECTED.name -> {
                findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToMyProductsFragment())
            }
            NotificationType.ORDER_DELIVERED.name, NotificationType.ORDER_PROCESSED.name -> {
                findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToOrdersHistoryFragment())
            }
        }
    }

    override fun makeRequests() {
        viewModel.clearDataIfNeed()

        getNotifications()
    }

    private fun getNotifications() {
        requestJob = lifecycleScope.launch {
            viewModel.getNotificationPaging().collectLatest {
                notificationAdapter.submitData(it)
            }
        }
    }
}

