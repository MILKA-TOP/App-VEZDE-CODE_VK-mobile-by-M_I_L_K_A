package com.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.databinding.OrderCardBinding


class OrderAdapter(
    private var orders: ArrayList<OrderInformation>,
) : RecyclerView.Adapter<OrderAdapter.CurrentViewHolder>() {

    private lateinit var itemPersonBinding: OrderCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        itemPersonBinding = OrderCardBinding.inflate(layoutInflater, parent, false)

        return CurrentViewHolder(itemPersonBinding)

    }

    override fun onBindViewHolder(holder: CurrentViewHolder, position: Int) =
        holder.bind(orders[position])

    override fun getItemCount() = orders.size

    fun setList(list: ArrayList<OrderInformation>) {
        val productDiffUtilCallback =
            OrderDiffUtilCallback(orders, list)
        val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)
        this.orders = list
        productDiffResult.dispatchUpdatesTo(this)
    }

    inner class CurrentViewHolder(private val orderItem: OrderCardBinding) :
        RecyclerView.ViewHolder(orderItem.root) {

        fun bind(order: OrderInformation) {


            with(orderItem) {
                address.text = order.address
                shopName.text = order.shopName
                time.text = order.time
                when (order.status) {
                    OrderStatus.ACCEPT -> orderItem.orderCard.setBackgroundResource(R.drawable.custom_border_accept)
                    OrderStatus.DISMISS -> orderItem.orderCard.setBackgroundResource(R.drawable.custom_border_dismiss)
                    OrderStatus.IN_PROGRESS -> orderItem.orderCard.setBackgroundResource(R.drawable.custom_border_wait)
                    else -> {}
                }
            }

        }
    }
}