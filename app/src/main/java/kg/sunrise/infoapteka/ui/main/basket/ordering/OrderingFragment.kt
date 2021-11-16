package kg.sunrise.infoapteka.ui.main.basket.ordering

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentOrderingBinding
import kg.sunrise.infoapteka.networking.models.request.PhonesItem
import kg.sunrise.infoapteka.networking.models.response.OrderingResponse
import kg.sunrise.infoapteka.networking.models.response.SellerProfileResponse
import kg.sunrise.infoapteka.ui.main.basket.ordering.adapter.PhoneChangeListener
import kg.sunrise.infoapteka.ui.main.basket.ordering.adapter.PhoneNumberAdapter
import kg.sunrise.infoapteka.utils.enums.OrderingResult
import kg.sunrise.infoapteka.utils.extensions.navigate
import kg.sunrise.infoapteka.utils.extensions.setDisabled
import kg.sunrise.infoapteka.utils.extensions.setEnabled
import kg.sunrise.infoapteka.utils.extensions.snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OrderingFragment : BaseFragmentWithVM<FragmentOrderingBinding, OrderingViewModel>(), PhoneChangeListener{
    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderingBinding {
        return FragmentOrderingBinding.inflate(inflater)
    }

    override val viewModel: OrderingViewModel by sharedViewModel()
    override val progressBar: ConstraintLayout by lazy{binding.inclProgress.clProgress}
    private val adapter by lazy { PhoneNumberAdapter(this) }
    private val args by navArgs<OrderingFragmentArgs>()

    override fun init() {
        setUpUI()
        setupObservers()
        initToolbar()
        initListeners()
        initAdapter()
    }

    private fun setupObservers() {
        viewModel.progressBarVisibility.observe(viewLifecycleOwner) {
            binding.inclProgress.clProgress.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.redirectToElsom.observe(viewLifecycleOwner) {
            if (it) {
                val uri = Uri.parse(viewModel.elsomLink)
                startActivity(Intent(Intent.ACTION_VIEW, uri))

                viewModel.redirectedToElsom()
            }
        }
        viewModel.toPopUpNavigation.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.popUpedNavigation()
                findNavController().navigateUp()
            }
        }

        viewModel.snackbarMessage.observe(viewLifecycleOwner) { stringResourceId ->
            if (stringResourceId != null) {
                snackbar(requireView(), stringResourceId)
                viewModel.clearSnackbarMessage()
            }
        }
    }

    override fun makeRequests() {
        viewModel.getUser()

        viewModel.orderNumber = args.orderID
    }

    override fun findTypeOfObject(data: Any?) {
        when(data){
            is OrderingResponse -> successOrder(data)
            is SellerProfileResponse -> restoreData()
        }
    }

    private fun successOrder(orderInfo: OrderingResponse) {
        when (orderInfo.paymentMethod) {
            elsom -> {
                viewModel.payWithElsom(orderInfo.orderId)
            }
        }
    }

    override fun successRequest() {
    }

    private fun setUpUI() {
        binding.tivAddPhone.hideDivider()
        binding.tvValueTotalSum.text = args.totalAmoun.toString()
        binding.tvValueGoodsInBasket.text = args.totalCount.toString()
    }

    private fun restoreData() {
        with(viewModel){
            isValid.observe(viewLifecycleOwner) {
                if (it == true) {
                    setEnableBtn()
                } else {
                    setDisableBtn()
                }
            }

            cartId.value = args.id
            firstName.value?.let { binding.tivFirstName.setInputText(it) }
            lastName.value?.let { binding.tivLastName.setInputText(it) }
            phones.value?.let { adapter.setData(it) }
            map.value?.let { binding.tivAddressInMap.setInputText(getString(R.string.choosen)) }
            deliveryAddress.value?.let { binding.tivDeliveryAddress.setInputText(it) }
            comments.value?.let { binding.inlcOrderingComment.etInputText.setText(it) }
            radioBtnItem.value?.let {
                when (it) {
                    elsom -> binding.rbgPaymentMethod.check(R.id.rb_elsom)
                }
            }
        }
    }

    private fun setEnableBtn() {
        binding.btnOrder.setEnabled(R.color.green)
    }
    private fun setDisableBtn(){
        binding.btnOrder.setDisabled(R.color.secondary_gray)
    }

    private fun initToolbar() {
        binding.inclToolbar.tvToolbar.text = getString(R.string.ordering_processing)
    }

    private fun initListeners() = with(binding){
        inclToolbar.ivBack.setOnClickListener { findNavController().navigateUp() }
        tivAddPhone.setOnLayoutClickListener { adapter.addNewPhone() }
        tvDeliveryCondition.setOnClickListener { findNavController().navigate(OrderingFragmentDirections.actionOrderingFragmentToDeliveryConditionFragment()) }
        tivAddressInMap.setOnLayoutClickListener { findNavController().navigate(OrderingFragmentDirections.actionOrderingFragmentToChooseAddressFragment()) }

        tivFirstName.addTextChangedListener { viewModel.firstName.value = it.text.toString() }
        tivLastName.addTextChangedListener { viewModel.lastName.value = it.text.toString() }
        tivDeliveryAddress.addTextChangedListener { viewModel.deliveryAddress.value = it.text.toString() }
        inlcOrderingComment.etInputText.addTextChangedListener { viewModel.comments.value = it.toString() }

        rbgPaymentMethod.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_elsom -> viewModel.radioBtnItem.value = elsom
            }
        }

        btnOrder.setOnClickListener { viewModel.setOrdering() }
    }

    private fun initAdapter() {
        binding.rvPhones.adapter = adapter
    }

    override fun onPhoneTextChangeListener(phone: String, position: Int, isValid: Boolean) {
        viewModel.phones.value?.get(position)?.phone = phone
        viewModel.phones.value?.get(position)?.isValid = isValid
        viewModel.wasPhoneChanged.value = true
    }

    override fun removePhoneAt(position: Int) {
        viewModel.phones.value?.removeAt(position)
        viewModel.wasPhoneChanged.value = true
    }

    override fun addPhone() {
        viewModel.phones.value?.add(PhonesItem())
        viewModel.wasPhoneChanged.value = true
    }

    companion object {
        private const val elsom = "elsom"
    }
}