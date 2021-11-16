package kg.sunrise.infoapteka.ui.main.menu.profile.adapter


import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.ui.views.PhoneNumberInputView
import kg.sunrise.infoapteka.ui.views.TextInputView
import kg.sunrise.infoapteka.utils.enums.ProfileInfoType


class BaseProfileAdapterOption(private val profile: ProfileInfo) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = ArrayList<MenuProfileOption>()
    var changeCity: (String) -> Unit = { }
    var dateChanged: (String) -> Unit = { }


    fun setData(items: ArrayList<MenuProfileOption>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.rv_item_profile -> ProfileMainVH(parent, R.layout.rv_item_profile)
            R.layout.rv_profile_city -> ProfileCityVH(parent, R.layout.rv_profile_city)
            R.layout.rv_profile_birth -> ProfileBirthDateVH(parent, R.layout.rv_profile_birth)
            R.layout.rv_profile_number -> ProfilePhoneVH(parent, R.layout.rv_profile_number)
            else -> throw IllegalStateException("layout doesnt exists")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is ProfileMainVH -> {
                holder.bind(item as MenuProfileOption.ProfileMainItem)
            }
            is ProfileCityVH -> {
                holder.bind(item as MenuProfileOption.ProfileCityItem)
                changeCity = {
                    holder.changeCity(it)
                }

            }
            is ProfileBirthDateVH -> {
                holder.bind(item as MenuProfileOption.ProfileBirthDate)
                dateChanged = {
                    holder.dateChanged(it)
                }
            }
            is ProfilePhoneVH -> holder.bind(item as MenuProfileOption.ProfileNumber)
        }
    }

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is MenuProfileOption.ProfileMainItem -> R.layout.rv_item_profile
        is MenuProfileOption.ProfileCityItem -> R.layout.rv_profile_city
        is MenuProfileOption.ProfileBirthDate -> R.layout.rv_profile_birth
        is MenuProfileOption.ProfileNumber -> R.layout.rv_profile_number
    }

    override fun getItemCount(): Int = items.count()


    inner class ProfilePhoneVH(parent: ViewGroup, @LayoutRes id: Int) :
        BaseVH<MenuProfileOption.ProfileNumber>(parent, id) {
        private val tvPhone = itemView.findViewById<PhoneNumberInputView>(R.id.phoneNumber)
        override fun bind(item: MenuProfileOption.ProfileNumber) {
            tvPhone.setLabel(item.title)
            tvPhone.setFullPhoneNumber(item.description ?: "")
            tvPhone.setLayoutEnabled(false)
        }

    }

    inner class ProfileMainVH(parent: ViewGroup, @LayoutRes res: Int) :
        BaseVH<MenuProfileOption.ProfileMainItem>(parent, res) {
        private val tvProfileItem: TextInputView = itemView.findViewById(R.id.tv_profile_item)
        override fun bind(item: MenuProfileOption.ProfileMainItem) {
            tvProfileItem.setLabel(item.title)
            when (item.type) {
                ProfileInfoType.FIRST_NAME -> tvProfileItem.setInputText(item.description)
                ProfileInfoType.LAST_NAME -> tvProfileItem.setInputText(item.description ?: "")
                ProfileInfoType.PATRONYMIC -> {
                    tvProfileItem.setInputText(item.description ?: "")
                    tvProfileItem.setRequire(false)
                }
                ProfileInfoType.ADDRESS -> {
                    tvProfileItem.setInputText(item.description ?: "")
                }
            }
            tvProfileItem.addTextChangedListener {
                profile.updateProfileInfo(it.text.toString(), item.type)
            }
        }
    }

    inner class ProfileCityVH(parent: ViewGroup, @LayoutRes id: Int) :
        BaseVH<MenuProfileOption.ProfileCityItem>(parent, id) {
        var changeCity: (String) -> Unit = { }
        val tvProfileItem: TextInputView = itemView.findViewById(R.id.tv_profile_item)
        override fun bind(item: MenuProfileOption.ProfileCityItem) {
            tvProfileItem.setLabel(item.title ?: "")
            tvProfileItem.setInputText(item.description ?: "")
            tvProfileItem.setRequire(true)
            changeCity = {
                tvProfileItem.setInputText(it ?: "")
            }
            tvProfileItem.setOnLayoutClickListener {
                profile.onItemProfileClick(item.type)
            }


        }
    }


    inner class ProfileBirthDateVH(parent: ViewGroup, @LayoutRes id: Int) :
        BaseVH<MenuProfileOption.ProfileBirthDate>(parent, id) {
        val tvProfileItem: TextInputView = itemView.findViewById(R.id.tv_profile_item)
        var dateChanged: (String) -> Unit = { }
        override fun bind(item: MenuProfileOption.ProfileBirthDate) {
            tvProfileItem.setLabel(item.title)
            tvProfileItem.setInputText(item.description ?: "")
            tvProfileItem.setRequire(true)
            tvProfileItem.setOnLayoutClickListener {
                profile.onItemProfileClick(item.type)
            }
            dateChanged = {
                tvProfileItem.setInputText(it)
                profile.updateProfileInfo(item.description, item.type)
            }
        }
    }
}







