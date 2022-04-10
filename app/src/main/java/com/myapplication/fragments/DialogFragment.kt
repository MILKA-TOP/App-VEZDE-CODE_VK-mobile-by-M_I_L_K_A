package com.myapplication.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.myapplication.*
import kotlinx.coroutines.*

class OrderedDialogFragment : DialogFragment() {

    private lateinit var nowOrder: OrderInformation
    private var scope = CoroutineScope(Dispatchers.Default)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        nowOrder =
            if (savedInstanceState?.getParcelable<OrderInformation>(ORDERED_NUMBER) == null) {
                OrderedDialogFragmentArgs.fromBundle(requireArguments()).orderNow
            } else {
                savedInstanceState.getParcelable(ORDERED_NUMBER)!!
            }


        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Заказ №${nowOrder.orderNumber}")
                .setMessage("Вы точно желаете принять этот заказ?")
                .setPositiveButton("Ок") { dialog, id ->
                    scope.launch {
                        nowOrder.status = OrderStatus.IN_PROGRESS
                        MainApp.instance.dataBase.orderDao()?.insertOrder(nowOrder)
                        withContext(Dispatchers.Main) {
                            navigate(
                                OrderedDialogFragmentDirections.actionDialogOrderedFragmentToActiveOrder(
                                    nowOrder
                                )
                            )
                            dialog.dismiss()
                        }
                    }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ORDERED_NUMBER, nowOrder)
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

}