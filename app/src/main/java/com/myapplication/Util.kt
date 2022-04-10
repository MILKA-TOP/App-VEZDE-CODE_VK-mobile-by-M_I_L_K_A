package com.myapplication

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

const val NOW_SHOP_INDEX = "NOW_SHOP_INDEX"
const val ORDERED_NUMBER = "ORDERED_NUMBER"
const val NOW_ORDER = "NOW_ORDER"
const val DATABASE_NAME = "ORDER_LIST"
val SHOP_LIST = listOf("Четвёрочка", "Полоска", "Не-магнит", "Пешеходник", "FixSale")

val ADDRESS_LIST = listOf(
    ShopInformation(
        "город Москва, Сокольническая площадь, 4А",
        55.78995229156817,
        37.67861622907788
    ),
    ShopInformation(
        "город Москва, улица Шолохова, д. 13, 1А",
        55.64108513549249, 37.3468865431321
    ),
    ShopInformation(
        "город Москва, Б.Грузинская улица, д. 60, стр.1",
        55.7720902370107, 37.58509029155094
    ),
    ShopInformation(
        "город Москва, Беговая улица, 13/2",
        55.780856014872924, 37.557939207984276
    ),
    ShopInformation(
        "Республика Калмыкия, город Элиста, улица В.и.ленина, д. 301, кв. 15",
        46.3106188353408, 44.28011700252499
    ),
    ShopInformation(
        "город Санкт-петербург, Железноводская улица, 17/5",
        59.952674777876275, 30.255551880665916
    )
)

data class ShopInformation(val name: String, val x: Double, val y: Double)

fun Fragment.navigate(
    navDirection: NavDirections,
    navOptions: NavOptions? = null
) {
    NavHostFragment
        .findNavController(this)
        .navigate(navDirection, navOptions)
}

enum class OrderStatus {
    DISMISS, ACCEPT, IN_PROGRESS, WAITING
}

@Parcelize
@Entity(tableName = DATABASE_NAME)
class OrderInformation(
    @ColumnInfo(name = "shopName") val shopName: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "status") var status: OrderStatus,
    @PrimaryKey var orderNumber: Int,
    @ColumnInfo(name = "x") var x: Double,
    @ColumnInfo(name = "y") var y: Double
    ) : Parcelable

class OrderDiffUtilCallback(
    private val oldList: ArrayList<OrderInformation>,
    private val newList: ArrayList<OrderInformation>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

}
