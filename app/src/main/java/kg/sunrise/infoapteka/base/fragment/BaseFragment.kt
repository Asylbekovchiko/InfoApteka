package kg.sunrise.infoapteka.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<Binding : ViewBinding> : Fragment() {

    private var _binding : Binding? = null
    val binding : Binding
        get() =  _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflateView(inflater, container)

        onCreateViewSetup(savedInstanceState)

        return binding.root
    }

    protected open fun onCreateViewSetup(savedInstanceState: Bundle?) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup?): Binding

    abstract fun init()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

