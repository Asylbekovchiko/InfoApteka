package kg.sunrise.infoapteka.ui.main.basket.ordering.adapter

import android.view.ViewGroup
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.networking.models.request.PhonesItem
import kg.sunrise.infoapteka.ui.views.PhoneNumberInputView
import timber.log.Timber

class PhoneNumberAdapter(private val phoneList: PhoneChangeListener): BaseAdapter<PhonesItem, PhoneNumberAdapter.PhoneNumberVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneNumberVH {
        return PhoneNumberVH(parent, R.layout.rv_profile_number)
    }

    fun removeItemInPosition(position: Int){
        items.removeAt(position)
        notifyItemRemoved(position)
        phoneList.removePhoneAt(position)
    }

    fun addNewPhone(){
        items.add(PhonesItem())
        notifyItemInserted(items.size-1)
        phoneList.addPhone()
    }

    inner class PhoneNumberVH(parent: ViewGroup, viewType: Int): BaseVH<PhonesItem>(parent, viewType){

        private val phone = itemView.findViewById<PhoneNumberInputView>(R.id.phoneNumber)

        override fun bind(item: PhonesItem) {
            phone.setFullPhoneNumber(item.phone)
            if (layoutPosition != 0){ // 0 - это первый элемент
                phone.setRightIcon(R.drawable.ic_close_red)
            }else
                phone.setLayoutEnabled(false)

            phone.setOnRightIconClickListener { removeItemInPosition(layoutPosition)}

            phone.addTextChangedListener {
                phone.isPhoneNumberValid.value?.let { phoneList.onPhoneTextChangeListener(phone.getPhone(), layoutPosition, it) }
            }
        }

    }
}