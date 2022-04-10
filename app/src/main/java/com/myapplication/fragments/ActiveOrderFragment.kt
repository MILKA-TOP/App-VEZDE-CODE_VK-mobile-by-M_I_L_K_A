package com.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myapplication.*
import com.myapplication.databinding.ActiveOrderBinding
import kotlinx.coroutines.*

class ActiveOrderFragment : Fragment() {
    private lateinit var nowOrder: OrderInformation
    private lateinit var binding: ActiveOrderBinding
    private var scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nowOrder =
            if (savedInstanceState?.getParcelable<OrderInformation>(ORDERED_NUMBER) == null) {
                OrderedDialogFragmentArgs.fromBundle(requireArguments()).orderNow
            } else {
                savedInstanceState.getParcelable(ORDERED_NUMBER)!!
            }
        scope.launch {
            MainApp.instance.dataBase.orderDao()?.updateOrder(nowOrder)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActiveOrderBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        with(binding) {
            orderNumber.text = nowOrder.orderNumber.toString()
            endOrderButton.setOnClickListener {
                scope.launch {
                    nowOrder.status = OrderStatus.ACCEPT
                    MainApp.instance.dataBase.orderDao()?.updateOrder(nowOrder)
                    withContext(Dispatchers.Main) {
                        navigate(
                            ActiveOrderFragmentDirections.actionActiveOrderToOrderCardFragment(
                                null
                            )
                        )
                    }
                }

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ORDERED_NUMBER, nowOrder)
    }
}