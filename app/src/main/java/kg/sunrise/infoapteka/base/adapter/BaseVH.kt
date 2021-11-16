package kg.sunrise.infoapteka.base.adapter

import android.content.ClipData
import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kg.sunrise.infoapteka.utils.extensions.inflate


abstract class BaseVH<Item>(parent: ViewGroup, @LayoutRes id: Int) :
    RecyclerView.ViewHolder(parent inflate id) {
    abstract fun bind(item: Item)
}

