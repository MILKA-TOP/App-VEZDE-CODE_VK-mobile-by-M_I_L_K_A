package com.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myapplication.MainApp
import com.myapplication.OrderAdapter
import com.myapplication.OrderInformation
import com.myapplication.databinding.HistoryOrdersBinding
import kotlinx.coroutines.*

class HistoryOrdersFragment : Fragment() {

    private lateinit var binding: HistoryOrdersBinding
    private var scope = CoroutineScope(Dispatchers.Default)
    private lateinit var mAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HistoryOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun adapterUpdate(list: ArrayList<OrderInformation>) {
        adapterInitialization(list)
        mAdapter.setList(list)
    }

    private fun adapterInitialization(li: ArrayList<OrderInformation>?) {
        if (!::mAdapter.isInitialized) {
            mAdapter = OrderAdapter(li!!)
            connectAdapterToRecycler()
        }
    }

    private fun connectAdapterToRecycler() {
        binding.recycler.adapter = mAdapter
    }


    override fun onStart() {
        super.onStart()
        scope.launch {
            val nowList = MainApp.instance.dataBase.orderDao()?.getAll()
            withContext(Dispatchers.Main) {
                adapterUpdate(nowList as ArrayList<OrderInformation>)
            }
        }

        binding.deleteHistoryButton.setOnClickListener {
            deleteHistory()
        }
    }

    private fun deleteHistory() {
        scope.launch {
            MainApp.instance.dataBase.orderDao()?.deleteAll()
            withContext(Dispatchers.Main) {
                adapterUpdate(ArrayList())
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

}