package com.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myapplication.*
import com.myapplication.databinding.InfoOrderLayoutBinding
import kotlinx.coroutines.*
import java.util.*


class OrderCardFragment : Fragment() {
    private lateinit var binding: InfoOrderLayoutBinding
    private lateinit var nowOrder: OrderInformation
    private var scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nowOrder =
            if (savedInstanceState?.get(NOW_ORDER) != null) savedInstanceState.get(NOW_ORDER) as OrderInformation
            else {
                val newAddress = ADDRESS_LIST.random()
                OrderInformation(
                    SHOP_LIST.random(),
                    newAddress.name,
                    getRandomTime(), OrderStatus.WAITING,
                    (0..Int.MAX_VALUE).random(),
                    newAddress.x, newAddress.y
                )
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InfoOrderLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStart() {
        super.onStart()

        scope.launch {
            val waitingOrder =
                MainApp.instance.dataBase.orderDao()?.getInProgressOrder(OrderStatus.IN_PROGRESS)
            withContext(Dispatchers.Main) {
                if (waitingOrder != null) {
                    navigate(
                        OrderCardFragmentDirections.actionOrderCardFragmentToActiveOrder(
                            waitingOrder
                        )
                    )
                } else {
                    showNewOrder()
                }

            }
        }

    }

    private fun showNewOrder() {
        drawCard()
        binding.dismiss.setOnClickListener {
            scope.launch {
                checkNowOrder()
                MainApp.instance.dataBase.orderDao()?.insertOrder(nowOrder)
                withContext(Dispatchers.Main) {
                    navigate(
                        OrderCardFragmentDirections.actionOrderCardFragmentSelf(nowOrder)
                    )
                }
            }
        }

        binding.accept.setOnClickListener {
            navigate(
                OrderCardFragmentDirections.actionOrderCardFragmentToDialogOrderedFragment(nowOrder)
            )
        }
    }


    private fun checkNowOrder() {
        var nowRandomValue = nowOrder.orderNumber
        while (MainApp.instance.dataBase.orderDao()?.getById(nowRandomValue) != null) {
            nowRandomValue = (0..Int.MAX_VALUE).random()
        }
        with(nowOrder) {
            orderNumber = nowRandomValue
            status = OrderStatus.DISMISS
        }
    }

    private fun drawCard() {
        with(binding.orderCardMain) {
            shopName.text = nowOrder.shopName
            address.text = nowOrder.address
            time.text = nowOrder.time
        }

    }

    private fun getRandomTime(): String {
        val c = Calendar.getInstance()
        val nowHour = c.get(Calendar.HOUR_OF_DAY)
        val nowMinute = c.get(Calendar.HOUR_OF_DAY)
        val newHour = (nowHour..23).random()
        val startNewMinute = if (newHour > nowHour) 0 else nowMinute
        val newMinute = (startNewMinute..59).random()
        return "%02d:%02d".format(newHour, newMinute)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(NOW_ORDER, nowOrder)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}